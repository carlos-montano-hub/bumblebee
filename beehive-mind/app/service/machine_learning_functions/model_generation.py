import keras
from matplotlib import pyplot as plt
from sklearn.model_selection import train_test_split
import tensorflow as tf
from app.service.machine_learning_functions.local_data_management import load_data
import seaborn as sns
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.utils import class_weight
from sklearn.metrics import classification_report, confusion_matrix
import os

layers = keras.layers
models = keras.models


def build_model(input_dim, loss_function: str):
    model = models.Sequential(
        [
            layers.Input(shape=(input_dim,)),
            layers.Dense(64, activation="relu"),
            layers.Dropout(0.5),
            layers.Dense(32, activation="relu"),
            layers.Dropout(0.5),
            layers.Dense(1, activation="sigmoid"),
        ]
    )
    if loss_function == "focal_loss":
        loss_function = focal_loss(gamma=2, alpha=0.75)
    elif loss_function == "tversky_loss":
        loss_function = tversky_loss(alpha=0.7, beta=0.3)

    model.compile(optimizer="adam", loss=loss_function, metrics=["accuracy"])
    return model


def generate_model(
    features,
    labels,
    test_size=0.2,
    random_state=42,
    epochs=10,
    batch_size=64,
    loss_function="binary_crossentropy",
):
    input_train, input_test, label_train, label_test = train_test_split(
        features,
        labels,
        test_size=test_size,
        random_state=random_state,
        stratify=labels,
    )
    scaler = StandardScaler()
    input_train = scaler.fit_transform(input_train)
    input_test = scaler.transform(input_test)
    weights = class_weight.compute_class_weight(
        "balanced", classes=np.unique(label_train), y=label_train
    )
    class_weights = {i: w for i, w in enumerate(weights)}
    model = build_model(input_train.shape[1], loss_function)
    model.fit(
        input_train,
        label_train,
        epochs=epochs,
        batch_size=batch_size,
        class_weight=class_weights,
        verbose=1,
    )
    os.makedirs("data/models", exist_ok=True)
    model.save("data/models/base_model.keras")
    evaluate_model(model, input_test, label_test)


def evaluate_model(model, input_test, label_test, threshold=0.5):
    scaler = StandardScaler()
    input_test = scaler.fit_transform(input_test)
    predictions = model.predict(input_test)
    predicted_labels = (predictions > threshold).astype(int)
    print("Classification Report:")
    print(classification_report(label_test, predicted_labels))
    print("Confusion Matrix:")
    confusion_matrix_result = confusion_matrix(label_test, predicted_labels)
    print(confusion_matrix_result)
    plt.figure(figsize=(6, 4))
    sns.heatmap(confusion_matrix_result, annot=True, fmt="d", cmap="Blues")
    plt.xlabel("Predicted")
    plt.ylabel("True")
    plt.title("Confusion Matrix")
    plt.show()


def focal_loss(gamma=2.0, alpha=0.25):
    def loss(y_true, y_pred):
        bce = keras.losses.binary_crossentropy(y_true, y_pred)
        p_t = y_true * y_pred + (1 - y_true) * (1 - y_pred)
        alpha_t = y_true * alpha + (1 - y_true) * (1 - alpha)
        return alpha_t * tf.pow(1 - p_t, gamma) * bce

    return loss


def tversky_loss(alpha=0.3, beta=0.7, smooth=1e-6):
    def loss(y_true, y_pred):
        y_true_f = tf.reshape(y_true, [-1])
        y_pred_f = tf.reshape(y_pred, [-1])
        tp = tf.reduce_sum(y_true_f * y_pred_f)
        fp = tf.reduce_sum((1 - y_true_f) * y_pred_f)
        fn = tf.reduce_sum(y_true_f * (1 - y_pred_f))
        tversky = (tp + smooth) / (tp + alpha * fn + beta * fp + smooth)
        return 1 - tversky

    return loss
