package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlaceRepositoryJpa extends JpaRepository<Place, String> {
    Place getPlaceById(String placeUuid);
    List<Place> getPlacesByNameContaining(String name);
    @Query("""
    SELECT p
    FROM Place p
    WHERE (:type IS NULL OR :type = ''
           OR LOWER(p.type) LIKE LOWER(CONCAT('%', :type, '%')))
      AND (:category IS NULL OR :category = ''
           OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%')))
      AND (:municipality IS NULL OR :municipality = ''
           OR LOWER(p.municipality) LIKE LOWER(CONCAT('%', :municipality, '%')))
      AND (:tag IS NULL OR :tag = ''
           OR LOWER(p.tags) LIKE LOWER(CONCAT('%', :tag, '%')))
    """)
    List<Place> findFilteredPlaces(
            @Param("type") String type,
            @Param("category") String category,
            @Param("municipality") String municipality,
            @Param("tag") String tag
    );

}
