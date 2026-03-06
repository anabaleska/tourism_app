package com.example.tourism_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "places")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id // internal surrogate key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_uuid", nullable = false, unique = true)
    private UUID placeUuid;

    @Column(nullable = false)
    private String name;

    private String category;
    private String type;
    private String place;
    private String municipality;
    private Double lat;
    private Double lng;
    private Double rating;
    private Integer price;

    @Column(name = "time_to_visit")
    private Integer timeToVisit;

    private String tags;
    private String description;

    @Column(name = "name_normalized")
    private String nameNormalized;

    @Column(name = "model_text")
    private String modelText;

    @Column(name = "image_url")
    private String imageUrl;
}