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

    public Interaction(UUID userUuid, UUID placeUuid, User user, Place place, Integer interaction) {
        this.userUuid = userUuid;
        this.placeUuid = placeUuid;
        this.interaction = interaction;
        this.user = user;
        this.place = place;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getPlaceUuid() {
        return placeUuid;
    }

    public void setPlaceUuid(UUID placeUuid) {
        this.placeUuid = placeUuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Integer getInteraction() {
        return interaction;
    }

    public void setInteraction(Integer interaction) {
        this.interaction = interaction;
    }
}