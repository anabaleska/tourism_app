from app.collaborative_recommender import CollaborativeRecommender
from app.content_recommender import Recommender

recommender = Recommender("../tourism_app.db")
collab_recommender = CollaborativeRecommender("../tourism_app.db")