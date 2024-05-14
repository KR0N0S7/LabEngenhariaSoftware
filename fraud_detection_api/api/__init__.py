# API initialization

from flask import Flask
from flask_restful import Api
from api.resources import FraudDetection

app = Flask(__name__)
api = Api(app)

api.add_resource(FraudDetection, '/fraud-detection')