package com.example.tourism_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "interactions")
@IdClass(Interaction.InteractionId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {

    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID userId;

    @Id
    @Column(name = "place_id", columnDefinition = "uuid")
    private UUID placeId;

    private Integer interaction;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InteractionId implements Serializable {
        private UUID userId;
        private UUID placeId;
    }
}