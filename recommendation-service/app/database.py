import os

from sqlalchemy import create_engine


DEFAULT_DATABASE_URL = (
    "postgresql+psycopg2://tourismAppDb:tourismAppDb@localhost:5433/tourism_app_db"
)


def get_database_engine():
    database_url = os.getenv("DATABASE_URL", DEFAULT_DATABASE_URL)
    return create_engine(database_url)
