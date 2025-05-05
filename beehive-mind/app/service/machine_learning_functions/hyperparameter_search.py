from typing import Literal
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.utils import class_weight
from sklearn.metrics import f1_score
import tensorflow as tf
import keras_tuner as kt
import keras
import warnings

warnings.filterwarnings("ignore", category=UserWarning)
# Label mapping and feature columns
label_dictionary = {
    "active": 0,
    "anomaly": 1,
    "QR": 0,
    "QL": 1,
}

feature_columns = [
    "zero_crossing_rate",
    "energy",
    "energy_entropy",
    "spectral_centroid",
    "spectral_spread",
    "spectral_entropy",
    "spectral_flux",
    "spectral_rolloff",
    *[f"mfcc_{i}" for i in range(1, 14)],
]


# Data loading
def load_data(csv_path, csv_path_kaggle):
    df1 = pd.read_csv(csv_path)
    df2 = pd.read_csv(csv_path_kaggle)
    df2 = df2.rename(columns={"zcr": "zero_crossing_rate"})
    combined = pd.concat([df1, df2], ignore_index=True)
    features = combined[feature_columns].values
    labels = combined["label"].map(label_dictionary)
    if labels.isnull().any():
        missing = combined["label"][labels.isnull()].unique()
        raise ValueError(f"Unknown labels: {missing}")
    return features, labels.values


# Loss functions
def focal_loss(gamma=2.0, alpha=0.25):
    def loss(y_true, y_pred):
        bce = keras.losses.binary_crossentropy(y_true, y_pred)
        p_t = y_true * y_pred + (1 - y_true) * (1 - y_pred)
        alpha_t = y_true * alpha + (1 - y_true) * (1 - alpha)
        return alpha_t * tf.pow(1 - p_t, gamma) * bce

    return loss


def tversky_loss(alpha=0.7, beta=0.3, smooth=1e-6):
    def loss(y_true, y_pred):
        y_true_f = tf.reshape(y_true, [-1])
        y_pred_f = tf.reshape(y_pred, [-1])
        tp = tf.reduce_sum(y_true_f * y_pred_f)
        fp = tf.reduce_sum((1 - y_true_f) * y_pred_f)
        fn = tf.reduce_sum(y_true_f * (1 - y_pred_f))
        t_index = (tp + smooth) / (tp + alpha * fn + beta * fp + smooth)
        return 1 - t_index

    return loss


# Experiment 2 results
# The following hyperparameter ranges were the best 20 (10%) out of 200 trials
# EXPERIMENT_2_BEST_HYPERPARAMETERS = {
#     "units1": {"min": 32, "max": 128},
#     "dropout1": {"min": 0.2, "max": 0.4},
#     "units2": {"min": 16, "max": 64},
#     "dropout2": {"min": 0.2, "max": 0.4},
#     "units3": {"min": 8, "max": 32},
#     "dropout3": {"min": 0.2, "max": 0.5},
#     "loss_fn": {"min": "focal", "max": "tversky"},
#     "focal_gamma": {"min": 1.0, "max": 5.0},
#     "focal_alpha": {"min": 0.1, "max": 0.9},
#     "lr": {"min": 0.00025784423780254027, "max": 0.003646043389686193},
#     "tversky_alpha": {"min": 0.1, "max": 0.8},
#     "tversky_beta": {"min": 0.15000000000000002, "max": 0.8},
# }

STEPS_PER_LAYER = [
    16,
    8,
    4,
    2,
]


def build_tuned_model(
    hp,
    input_dim,
    hp_ranges: dict[str, dict],
    residual: bool,
    loss_function: Literal["focal", "tversky"] | None = None,
):
    inputs = keras.Input(shape=(input_dim,))
    x = inputs

    num_layers = hp.Int("layers", 2, 4, step=1)

    def dense_block(x, idx):
        u = hp.Int(
            f"units{idx}",
            hp_ranges[f"units{idx}"]["min"],
            hp_ranges[f"units{idx}"]["max"],
            step=STEPS_PER_LAYER[idx - 1],
        )
        d = hp.Float(
            f"dropout{idx}",
            hp_ranges[f"dropout{idx}"]["min"],
            hp_ranges[f"dropout{idx}"]["max"],
            step=0.1,
        )
        y = keras.layers.Dense(u, activation="relu")(x)
        y = keras.layers.Dropout(d)(y)
        return y, u

    def merge_residual(prev, curr):
        prev_dim = int(prev.shape[-1])
        curr_dim = int(curr.shape[-1])
        if not residual:
            return curr
        if prev_dim != curr_dim:
            curr = keras.layers.Dense(prev_dim)(curr)
        return keras.layers.Add()([prev, curr])

    prev = x
    for i in range(1, num_layers + 1):
        curr, _units = dense_block(prev, i)
        # only merge for layers ≥3
        if i >= 3:
            curr = merge_residual(prev, curr)
        prev = curr

    outputs = keras.layers.Dense(1, activation="sigmoid")(prev)
    model = keras.Model(inputs, outputs)

    # loss choice
    choice = (
        hp.Choice("loss_fn", ["focal", "tversky"])
        if loss_function is None
        else loss_function
    )
    if choice == "focal":
        loss_fn = focal_loss(
            gamma=hp.Float(
                "focal_gamma",
                hp_ranges["focal_gamma"]["min"],
                hp_ranges["focal_gamma"]["max"],
                step=0.5,
            ),
            alpha=hp.Float(
                "focal_alpha",
                hp_ranges["focal_alpha"]["min"],
                hp_ranges["focal_alpha"]["max"],
                step=0.1,
            ),
        )
    else:
        loss_fn = tversky_loss(
            alpha=hp.Float(
                "tversky_alpha",
                hp_ranges["tversky_alpha"]["min"],
                hp_ranges["tversky_alpha"]["max"],
                step=0.05,
            ),
            beta=hp.Float(
                "tversky_beta",
                hp_ranges["tversky_beta"]["min"],
                hp_ranges["tversky_beta"]["max"],
                step=0.05,
            ),
        )

    lr = hp.Float("lr", hp_ranges["lr"]["min"], hp_ranges["lr"]["max"], sampling="log")
    model.compile(
        optimizer=keras.optimizers.Adam(lr), loss=loss_fn, metrics=["accuracy"]
    )
    return model


# Callback to compute F1 for class 1 on validation data
class F1MetricCallback(keras.callbacks.Callback):
    def __init__(self, validation_data, threshold=0.5):
        super().__init__()
        self.validation_data = validation_data
        self.threshold = threshold

    def on_epoch_end(self, epoch, logs=None):
        features_val, y_val = self.validation_data
        y_pred_prob = self.model.predict(features_val, verbose=0)
        y_pred = (y_pred_prob > self.threshold).astype(int).flatten()
        f1 = f1_score(y_val, y_pred, pos_label=1)
        logs = logs or {}
        logs["val_f1"] = f1
        print(f" — val_f1: {f1:.4f}")


# Main hyperparameter search function
def run_hyperparameter_search(
    features,
    labels,
    epochs,
    experiment_name,
    hp_ranges: dict[str, dict],
    max_trials=10,
    test_size=0.2,
    random_state=42,
    residual: bool = False,
    loss_function: Literal["focal", "tversky"] | None = None,
):
    # Load and prepare data
    features_train, features_validation, labels_train, labels_validation = (
        train_test_split(
            features,
            labels,
            test_size=test_size,
            stratify=labels,
            random_state=random_state,
        )
    )
    scaler = StandardScaler()
    features_train = scaler.fit_transform(features_train)
    features_validation = scaler.transform(features_validation)

    # Class weights
    weights = class_weight.compute_class_weight(
        "balanced", classes=np.unique(labels_train), y=labels_train
    )
    class_weights = {i: w for i, w in enumerate(weights)}

    # Keras Tuner setup: use val_f1 as objective
    tuner = kt.RandomSearch(
        hypermodel=lambda hp: build_tuned_model(
            hp, features_train.shape[1], hp_ranges, residual, loss_function
        ),
        objective=kt.Objective("val_f1", direction="max"),
        max_trials=max_trials,
        executions_per_trial=1,
        directory=experiment_name,
        project_name=experiment_name,
    )

    # Callbacks: F1-based early stopping
    f1_cb = F1MetricCallback((features_validation, labels_validation), threshold=0.5)

    tuner.search(
        features_train,
        labels_train,
        epochs=epochs,
        validation_data=(features_validation, labels_validation),
        class_weight=class_weights,
        callbacks=[f1_cb],
    )

    return tuner


# No top-level execution; call run_hyperparameter_search() from your code
