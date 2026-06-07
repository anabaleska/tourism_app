package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceRepositoryJpa extends JpaRepository<Place, String> {
    Place getPlaceById(String placeUuid);
}
