from flask import Flask
from .routes.recommendation_routes import recommendation_blueprint
import logging as log

def create_app():
    app = Flask(__name__)
    app.register_blueprint(recommendation_blueprint)

    app.log.setLevel(log.DEBUG) 
    return app
