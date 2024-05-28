from sklearn.ensemble import IsolationForest
import numpy as np

class AnomalyDetectionModel:
    def __init__(self):
        self.model = IsolationForest(n_estimators=100, contamination=0.1)  # Adjust parameters as needed

    def fit(self, X):
        self.model.fit(X)

    def predict(self, data):
        """
        Predict if the given data point is an anomaly.

        Args:
            data (dict): Preprocessed data for the model.

        Returns:
            int: 1 if anomaly, 0 otherwise.
        """
        # Convert data to a NumPy array
        data_array = np.array(list(data.values()))  # Assuming data is a dictionary

        prediction = self.model.predict(data_array.reshape(1, -1))  # Reshape for single sample prediction
        return prediction[0]  # Extract the prediction value
    