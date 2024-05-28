from surprise import Dataset, Reader, KNNBasic
from surprise.model_selection import train_test_split
from collections import defaultdict
import pandas as pd
from flask import Flask, jsonify, request

# Sample data
data = {
    'userId': [1, 2, 1, 3, 4, 5, 2, 3, 4, 1, 5, 3],
    'productId': [101, 101, 102, 103, 102, 101, 103, 102, 103, 104, 104, 105],
    'rating': [5, 4, 5, 3, 2, 5, 2, 4, 3, 5, 4, 5]
}

# Convert the data to a DataFrame
ratings_df = pd.DataFrame(data)

# Filter out products with fewer than 50 ratings
filtered_products = ratings_df['productId'].value_counts() > 50
filtered_products = filtered_products[filtered_products].index.tolist()
filtered_ratings = ratings_df[ratings_df['productId'].isin(filtered_products)]

# Define a Reader and load the dataset from the DataFrame
reader = Reader(rating_scale=(1, 5))
data = Dataset.load_from_df(filtered_ratings[['userId', 'productId', 'rating']], reader)

# Split the dataset for training and testing
trainset = data.build_full_trainset()

# Use KNN to compute similarity between items
algo = KNNBasic(sim_options={'name': 'cosine', 'user_based': False})
algo.fit(trainset)

# Function to get recommendations for a given user
def get_recommendations(user_id, n=10):
    # Check if user exists in the dataset
    if user_id not in trainset.all_users():
        return []
    # Predict ratings for all pairs (user, item) that are NOT in the training set.
    testset = trainset.build_anti_testset()
    predictions = algo.test(testset)
    
    # Filter predictions for the user
    user_predictions = [pred for pred in predictions if pred.uid == user_id]
    
    # Get the top-N recommendations
    top_n_predictions = sorted(user_predictions, key=lambda x: x.est, reverse=True)[:n]
    
    # Extract product IDs
    recommended_product_ids = [pred.iid for pred in top_n_predictions]
    return recommended_product_ids

app = Flask(__name__)

@app.route('/recommend', methods=['GET'])
def recommend_products():
    user_id = request.args.get('userId', default=1, type=int)
    recommendations = get_recommendations(user_id)
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True)
