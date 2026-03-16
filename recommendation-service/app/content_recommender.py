import sqlite3
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


class Recommender:

    def __init__(self, db_path):
        with sqlite3.connect(db_path) as conn:
            self.df = pd.read_sql_query(
                "SELECT place_id, name, model_text FROM places",
                conn
            )

        self.df["model_text"] = self.df["model_text"].fillna("")

        tfidf = TfidfVectorizer()

        self.tag_matrix = tfidf.fit_transform(self.df["model_text"])

        self.cos_sim = cosine_similarity(self.tag_matrix, self.tag_matrix)

        self.id_to_index = pd.Series(self.df.index, index=self.df["place_id"]).to_dict()

    def recommend(self, place_id, top_n=5):
        if place_id not in self.id_to_index:
            return []

        idx = self.id_to_index[place_id]

        sim_scores = list(enumerate(self.cos_sim[idx]))

        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)

        sim_scores = [s for s in sim_scores
                      if self.df.iloc[s[0]]["place_id"] != place_id
                      ][:top_n]

        recs = self.df.iloc[[i[0] for i in sim_scores]]

        return recs[["place_id", "name"]].to_dict(orient="records")
