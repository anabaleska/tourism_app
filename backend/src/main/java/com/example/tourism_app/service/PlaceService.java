package com.example.tourism_app.service;

import com.example.tourism_app.model.Place;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PlaceService {
    Optional<Place> getPlaceById(String id);
    Place createPlace(String placeUuid, String name, String category, String type, String place,
                      String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl);
    Place updatePlace(Long id, UUID placeUuid, String name, String category, String type, String place,
                      String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl);
    void deletePlace(String id);
    List<Place> listPlaces();
    List<Place> listPlacesByNameContaining(String search);
    List<Place> listFilteredPlaces(String type, String category, String municipality, String tag);
    Map<String, List<String>> getAllFilterOptions();
}
