# Experiment 2 results
# The following hyperparameter ranges were the best 20 (10%) out of 200 trials
EXPERIMENT_2_BEST_HYPERPARAMETERS = {
    "units1": {"min": 32, "max": 128},
    "dropout1": {"min": 0.2, "max": 0.5},
    "units2": {"min": 16, "max": 64},
    "dropout2": {"min": 0.2, "max": 0.5},
    "loss_fn": {"min": "focal", "max": "tversky"},
    "lr": {"min": 0.00020838570998829784, "max": 0.008326641602942395},
    "focal_gamma": {"min": 1.0, "max": 5.0},
    "focal_alpha": {"min": 0.1, "max": 0.9},
    "tversky_alpha": {"min": 0.1, "max": 0.8},
    "tversky_beta": {"min": 0.1, "max": 0.9},
}
# Experiment 3 results
# The following hyperparameter ranges were the best 20 (10%) out of 200 trials
EXPERIMENT_3_BEST_HYPERPARAMETERS = {
    "units1": {"min": 32, "max": 128},
    "dropout1": {"min": 0.2, "max": 0.4},
    "units2": {"min": 16, "max": 64},
    "dropout2": {"min": 0.2, "max": 0.4},
    "units3": {"min": 8, "max": 32},
    "dropout3": {"min": 0.2, "max": 0.5},
    "units4": {"min": 4, "max": 16},
    "dropout4": {"min": 0.2, "max": 0.6},
    "loss_fn": {"min": "focal", "max": "tversky"},
    "focal_gamma": {"min": 1.0, "max": 5.0},
    "focal_alpha": {"min": 0.1, "max": 0.9},
    "lr": {"min": 0.00025784423780254027, "max": 0.003646043389686193},
    "tversky_alpha": {"min": 0.1, "max": 0.8},
    "tversky_beta": {"min": 0.15000000000000002, "max": 0.8},
}

# Experiment 4 results
# Ranges of the best 60.0 trials
# Ranges of the best 6.0 trials of 600 trials
EXPERIMENT_4_BEST_HYPERPARAMETERS = {
    "layers": {"min": 2, "max": 4},
    "units1": {"min": 32, "max": 128},
    "dropout1": {"min": 0.2, "max": 0.4},
    "units2": {"min": 16, "max": 56},
    "dropout2": {"min": 0.2, "max": 0.4},
    "loss_fn": {"min": "focal", "max": "focal"},
    "focal_gamma": {"min": 3.0, "max": 5.0},
    "focal_alpha": {"min": 0.2, "max": 0.5},
    "lr": {"min": 0.0003119829719176651, "max": 0.003494752050667823},
    "units3": {"min": 8, "max": 28},
    "dropout3": {"min": 0.2, "max": 0.4},
    "units4": {"min": 4, "max": 16},
    "dropout4": {"min": 0.2, "max": 0.5},
    "tversky_alpha": {"min": 0.15000000000000002, "max": 0.55},
    "tversky_beta": {"min": 0.15000000000000002, "max": 0.65},
}

# Experiment 5 results
# Ranges of the best 17 trials of 1743 trials
EXPERIMENT_5_BEST_HYPERPARAMETERS = {
    "layers": {"min": 2, "max": 4},
    "units1": {"min": 32, "max": 128},
    "dropout1": {"min": 0.2, "max": 0.4},
    "units2": {"min": 16, "max": 56},
    "dropout2": {"min": 0.2, "max": 0.4},
    "focal_gamma": {"min": 3.0, "max": 5.0},
    "focal_alpha": {"min": 0.2, "max": 0.4},
    "lr": {"min": 0.00040665970232275537, "max": 0.003259614136636821},
    "units3": {"min": 8, "max": 28},
    "dropout3": {"min": 0.2, "max": 0.4},
    "units4": {"min": 4, "max": 16},
    "dropout4": {"min": 0.2, "max": 0.4},
}

# Experiment 6 results
# Ranges of the best 15 trials of 1557 trials
EXPERIMENT_6_BEST_HYPERPARAMETERS = {
    "layers": {"min": 4, "max": 4},
    "units1": {"min": 32, "max": 128},
    "dropout1": {"min": 0.2, "max": 0.4},
    "units2": {"min": 16, "max": 56},
    "dropout2": {"min": 0.2, "max": 0.4},
    "focal_gamma": {"min": 4.0, "max": 5.0},
    "focal_alpha": {"min": 0.2, "max": 0.4},
    "lr": {"min": 0.0004974932159881361, "max": 0.0027950282635432248},
    "units3": {"min": 8, "max": 26},
    "dropout3": {"min": 0.2, "max": 0.4},
    "units4": {"min": 5, "max": 15},
    "dropout4": {"min": 0.2, "max": 0.4},
}
# Experiment 7 results
# Ranges of the best 15.0 trials of 1500 trials
# {
#     "units1": 88,
#     "dropout1": 0.22,
#     "units2": 48,
#     "dropout2": 0.24000000000000002,
#     "units3": 13,
#     "dropout3": 0.22,
#     "units4": 8,
#     "dropout4": 0.35,
#     "focal_gamma": 4.65,
#     "focal_alpha": 0.24000000000000002,
#     "lr": 0.001043400598498229,
# }
EXPERIMENT_7_BEST_HYPERPARAMETERS = {
    "units1": {"min": 36, "max": 124},
    "dropout1": {"min": 0.21000000000000002, "max": 0.4},
    "units2": {"min": 30, "max": 54},
    "dropout2": {"min": 0.24000000000000002, "max": 0.4},
    "units3": {"min": 8, "max": 25},
    "dropout3": {"min": 0.22, "max": 0.4},
    "units4": {"min": 5, "max": 15},
    "dropout4": {"min": 0.2, "max": 0.37},
    "focal_gamma": {"min": 4.1, "max": 4.9},
    "focal_alpha": {"min": 0.21000000000000002, "max": 0.39},
    "lr": {"min": 0.0005489167924828676, "max": 0.0025437070624448476},
}
