from app.collaborative_recommender import CollaborativeRecommender
from app.content_recommender import Recommender
from app.database import get_database_engine

database_engine = get_database_engine()

recommender = Recommender(database_engine)
collab_recommender = CollaborativeRecommender(database_engine)
