package com.example.tourism_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "place_uuid", nullable = false)
    private UUID placeUuid;

    @ManyToOne
    @JoinColumn(name = "user_internal_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_internal_id", referencedColumnName = "id")
    private Place place;

    private Integer interaction;
}