from fastapi import APIRouter
from app.recommender_instance import recommender

router = APIRouter()

@router.get("/recommend")
def recommend(place_id: str):

    results = recommender.recommend(place_id)

    return {"recommendations": results}
