from fastapi import FastAPI
from app.routes.recommendations import router

app = FastAPI()

app.include_router(router)


@app.get("/")
def root():
    return {"message": "Recommendation service running"}
