import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity

class CollaborativeRecommender:

    def __init__(self, db_engine, interactions_table="interactions"):
        with db_engine.connect() as conn:
            self.places_df = pd.read_sql_query(
                "SELECT place_id, name, category, municipality FROM places",
                conn
            ).set_index("place_id")

            interactions_df = pd.read_sql_query(
                f"SELECT user_id, place_id, interaction FROM {interactions_table}",
                conn
            )

        if interactions_df.empty:
            self.user_item_matrix = pd.DataFrame()
            self.item_similarity_df = pd.DataFrame()
            return

        user_item_matrix = interactions_df.pivot_table(
            index="user_id",
            columns="place_id",
            values="interaction",
            fill_value=0
        )
        self.user_item_matrix = user_item_matrix

        item_user_matrix = user_item_matrix.T
        if item_user_matrix.empty:
            self.item_similarity_df = pd.DataFrame()
            return

        item_similarity = cosine_similarity(item_user_matrix)
        self.item_similarity_df = pd.DataFrame(
            item_similarity,
            index=item_user_matrix.index,
            columns=item_user_matrix.index
        )

    def recommend(self, user_id, top_n=5, similarity_threshold=0.2):
        if self.user_item_matrix.empty or user_id not in self.user_item_matrix.index:
            return []

        user_likes = self.user_item_matrix.loc[user_id]
        liked_places = user_likes[user_likes > 0].index.tolist()

        scores = {}
        for place in liked_places:
            similar_places = self.item_similarity_df[place]
            for other_place, score in similar_places.items():
                if other_place in liked_places or score < similarity_threshold:
                    continue
                scores[other_place] = scores.get(other_place, 0) + score

        recommendations = pd.Series(scores).sort_values(ascending=False).head(top_n)

        results = []
        for place_id, score in recommendations.items():
            if place_id not in self.places_df.index:
                continue
            place = self.places_df.loc[place_id]
            results.append({
                "place_id": place_id,
                "name": place["name"],
                "similarity_score": round(score, 3),
                "category": place["category"],
                "municipality": place["municipality"]
            })

        return results
