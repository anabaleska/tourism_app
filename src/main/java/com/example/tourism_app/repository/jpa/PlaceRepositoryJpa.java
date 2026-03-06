package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepositoryJpa extends JpaRepository<Place, Long> {
}
