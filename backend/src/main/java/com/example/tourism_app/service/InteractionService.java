package com.example.tourism_app.service;

import com.example.tourism_app.model.Interaction;
import com.example.tourism_app.model.Place;

import java.util.List;
import java.util.UUID;

public interface InteractionService {
    Interaction getInteractionById(Long id);
    Interaction createInteraction(UUID userUuid, UUID placeUuid, Integer interaction);
    Interaction updateInteraction(Long id, UUID userUuid, UUID placeUuid, Integer interaction);
    void deleteInteraction(Long id);
    List<Interaction> listInteractions();
    List<Interaction> findAllUserInteractions(Long userId);
}
