package com.example.tourism_app.service;

import com.example.tourism_app.model.Place;
import jakarta.persistence.Column;

import java.util.List;
import java.util.UUID;

public interface PlaceService {
    Place getPlaceById(Long id);
    Place createPlace(UUID placeUuid, String name, String category, String type, String place,
                      String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl);
    Place updatePlace(Long id, UUID placeUuid, String name, String category, String type, String place,
                      String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl);
    void deletePlace(Long id);
    List<Place> listPlaces();
}
