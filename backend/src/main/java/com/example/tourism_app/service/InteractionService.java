package com.example.tourism_app.service;

import com.example.tourism_app.model.Interaction;
import com.example.tourism_app.model.Place;

import java.util.List;
import java.util.UUID;

public interface InteractionService {
    Interaction getInteractionById(Long id);
    Interaction createInteraction(String userUuid, String placeUuid, Integer interaction);
    Interaction updateInteraction(Long id, String userUuid, String placeUuid, Integer interaction);
    void deleteInteraction(Long id);
    List<Interaction> listInteractions();
    List<Interaction> findAllUserInteractions(String userId);
    Double averageInteraction(String placeId);
}
