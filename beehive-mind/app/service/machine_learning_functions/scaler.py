import joblib
import os
from sklearn.preprocessing import StandardScaler, MinMaxScaler, RobustScaler


def generate_scaler(scaler_type, data):
    """
    Generate a scaler based on the specified type and fit it to the provided data.

    Args:
        scaler_type (str): The type of scaler to generate ('standard', 'minmax', 'robust').
        data (pd.DataFrame): The data to fit the scaler to.

    Returns:
        scaler: The fitted scaler object.
    """

    if scaler_type == "standard":
        scaler = StandardScaler()
    elif scaler_type == "minmax":
        scaler = MinMaxScaler()
    elif scaler_type == "robust":
        scaler = RobustScaler()
    else:
        raise ValueError(
            "Invalid scaler type. Choose from 'standard', 'minmax', or 'robust'."
        )

    # Fit the scaler to the data
    scaler.fit(data)

    # Ensure the directory exists before saving the scaler
    os.makedirs("data/scaler", exist_ok=True)
    joblib.dump(scaler, "data/scaler/base_scaler.pkl")

    return scaler
