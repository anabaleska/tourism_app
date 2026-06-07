package com.example.tourism_app.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.tourism_app.model.Place;
import com.example.tourism_app.service.PlaceService;
import com.example.tourism_app.service.RecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final PlaceService placeService;
    private final RestClient restClient;

    public RecommendationServiceImpl(
            PlaceService placeService,
            @Value("${recommendation-service.base-url:http://localhost:8000}") String recommendationServiceBaseUrl) {
        this.placeService = placeService;
        this.restClient = RestClient.builder().baseUrl(recommendationServiceBaseUrl).build();
    }

    @Override
    public List<Place> getContentBasedRecommendations(String placeId) {
        RecommendationResponse response = fetchRecommendations("/recommend", "place_id", placeId);

        return toPlaces(response);
    }

    @Override
    public List<Place> getCollaborativeRecommendations(String userId) {
        RecommendationResponse response = fetchRecommendations("/recommend_collab", "user_id", userId);

        return toPlaces(response);
    }

    private RecommendationResponse fetchRecommendations(String path, String queryParam, String value) {
        RecommendationResponse response;

        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam(queryParam, value)
                            .build())
                    .retrieve()
                    .body(RecommendationResponse.class);
        } catch (RestClientException ex) {
            return null;
        }

        return response;
    }

    private List<Place> toPlaces(RecommendationResponse response) {
        if (response == null || response.getRecommendations() == null) {
            return Collections.emptyList();
        }

        return response.getRecommendations().stream()
                .map(RecommendationItem::getPlaceId)
                .filter(Objects::nonNull)
                .map(placeService::getPlaceById)
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    public static class RecommendationResponse {
        private List<RecommendationItem> recommendations;

        public List<RecommendationItem> getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(List<RecommendationItem> recommendations) {
            this.recommendations = recommendations;
        }
    }

    public static class RecommendationItem {
        @JsonProperty("place_id")
        private String placeId;
        private String name;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
