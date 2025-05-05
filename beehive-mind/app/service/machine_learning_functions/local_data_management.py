import pandas as pd
import numpy as np


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
    "mfcc_1",
    "mfcc_2",
    "mfcc_3",
    "mfcc_4",
    "mfcc_5",
    "mfcc_6",
    "mfcc_7",
    "mfcc_8",
    "mfcc_9",
    "mfcc_10",
    "mfcc_11",
    "mfcc_12",
    "mfcc_13",
]


def merge_csv_files(csv_path, csv_path_kaggle):
    df = pd.read_csv(csv_path)
    df2 = pd.read_csv(csv_path_kaggle)
    df2 = df2.rename(columns={"zcr": "zero_crossing_rate"})
    df_combined = pd.concat([df, df2], ignore_index=True)
    return df_combined


def load_data(csv_path, csv_path_kaggle):
    df = pd.read_csv(csv_path)
    df2 = pd.read_csv(csv_path_kaggle)
    df2 = df2.rename(columns={"zcr": "zero_crossing_rate"})
    features = np.vstack([df[feature_columns].values, df2[feature_columns].values])
    labels = pd.concat([df["label"], df2["label"]]).map(label_dictionary)
    if labels.isnull().any():
        missing = labels[labels.isnull()].unique()
        raise ValueError(f"Found unknown labels in CSV: {missing}")
    return features, labels.values
