# API resource definition

from flask import request
from flask_restful import Resource
from models import FraudDetectionRequest, FraudDetectionResponse
from data.preprocessor import preprocess_data
from models import AnomalyDetectionModel

model = AnomalyDetectionModel()  # Initialize the anomaly detection model

class FraudDetection(Resource):
    def post(self):
        # Parse the request data
        request_data = FraudDetectionRequest.parse_raw(request.data)

        # Preprocess the data
        processed_data = preprocess_data(request_data.dict())

        # Get the prediction from the model
        prediction = model.predict(processed_data)

        # Create the response
        response = FraudDetectionResponse(is_fraud=prediction)
        return response.dict(), 200