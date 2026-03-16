from fastapi import APIRouter
from app.recommender_instance import recommender, collab_recommender

router = APIRouter()

@router.get("/recommend")
def recommend(place_id: str):

    results = recommender.recommend(place_id)

    return {"recommendations": results}


@router.get("/recommend_collab")
def recommend_collab(user_id: str):
    results = collab_recommender.recommend(user_id)
    return {"recommendations": results}