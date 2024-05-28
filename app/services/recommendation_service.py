from surprise import Dataset, Reader, KNNBasic
import pandas as pd
from collections import defaultdict
import logging as log
from collections import Counter

# O código de preparação dos dados, treinamento do modelo e a função get_recommendations podem ser movidos para cá
log.basicConfig(filename='recommendations.log', level=log.DEBUG, format='%(asctime)s %(message)s') 

class RecommendationService:
    def __init__(self):
        self.reader = Reader(rating_scale=(1, 10))
        self.algo = KNNBasic(sim_options={'name': 'cosine', 'user_based': True})  # User-based for nearest neighbors
        self.global_ratings_df = pd.DataFrame()
        self.user_ratings_df = pd.DataFrame()

    def update_data(self, global_data, user_data):
        """Update the dataset with global ratings and user-specific ratings."""
        self.global_ratings_df = pd.DataFrame(global_data)
        self.user_ratings_df = pd.DataFrame(user_data)
        log.debug("Data updated with global and user-specific ratings.")

    def train_model(self):
        """Train the KNN model with the global dataset excluding the logged-in user."""
        if not self.global_ratings_df.empty:
            data = Dataset.load_from_df(self.global_ratings_df[['clienteId', 'nomeProduto', 'nota']], self.reader)
            full_trainset = data.build_full_trainset()
            self.algo.fit(full_trainset)
            log.debug("Model trained with global data.")
        else:
            log.error("Global ratings DataFrame is empty, training aborted.")

    def recommend_products(self, n=5):
        if self.user_ratings_df.empty:
            log.error("User ratings DataFrame is empty, cannot generate recommendations.")
            return []

        user_id = self.user_ratings_df.iloc[0]['clienteId']
        try:
            inner_user_id = self.algo.trainset.to_inner_uid(str(user_id))
        except ValueError:
            log.warning(f"Logged user ID {user_id} not found in the dataset.")
            return []

        neighbors = self.algo.get_neighbors(inner_user_id, k=5)
        log.debug(f"Neighbors found: {neighbors}")

        product_ratings = []
        for neighbor in neighbors:
            neighbor_ratings = self.global_ratings_df[self.global_ratings_df['clienteId'] == self.algo.trainset.to_raw_uid(neighbor)]
            product_ratings.extend(neighbor_ratings[['nomeProduto', 'nota']].to_dict('records'))

        # Count how many times each product has been rated
        product_count = Counter([p['nomeProduto'] for p in product_ratings])
        log.debug(f"Product rating counts: {product_count}")

        # Filter products that have been rated at least 10 times
        eligible_products = [product for product, count in product_count.items() if count >= 1]
        log.debug(f"Eligible products: {eligible_products}")

        # Extract recommendations from eligible products
        recommendations = [p for p in product_ratings if p['nomeProduto'] in eligible_products]
        recommendations = sorted(recommendations, key=lambda x: -x['nota'])[:n]
        log.info(f"Recommendations generated: {recommendations}")

        return [r['nomeProduto'] for r in recommendations]


