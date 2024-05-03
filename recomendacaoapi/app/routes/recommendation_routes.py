from flask import Blueprint, jsonify, request
from app.services.recommendation_service import RecommendationService
import logging as log

recommendation_blueprint = Blueprint('recommendations', __name__, url_prefix='/recommend')
service = RecommendationService()

@recommendation_blueprint.route('/', methods=['POST'])
def recommend_products():
    # Expecting JSON data with two keys: 'global_data' and 'user_data'
    content = request.json

    # log.debug(f"Received JSON: {content}")  
    global_data = content.get('globalRatings', [])
    user_data = content.get('userRatings', [])

    # Check for empty datasets and return an error if necessary
    if not global_data or not user_data:
        return jsonify({"error": "Missing data for global ratings or user ratings"}), 400

    # Update data in the recommendation service
    service.update_data(global_data, user_data)

    # Train the model with global data
    service.train_model()

    # Generate recommendations for the logged-in user
    recommendations = service.recommend_products()
    
    return jsonify({"recommendations": recommendations})
