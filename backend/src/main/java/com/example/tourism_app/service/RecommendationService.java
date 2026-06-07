package com.example.tourism_app.service;

import com.example.tourism_app.model.Place;

import java.util.List;

public interface RecommendationService {
    List<Place> getContentBasedRecommendations(String placeId);
    List<Place> getCollaborativeRecommendations(String userId);
}
