package com.example.tourism_app.service.impl;

import com.example.tourism_app.model.Interaction;
import com.example.tourism_app.model.Place;
import com.example.tourism_app.model.User;
import com.example.tourism_app.repository.jpa.InteractionRepositoryJpa;
import com.example.tourism_app.repository.jpa.PlaceRepositoryJpa;
import com.example.tourism_app.repository.jpa.UserRepositoryJpa;
import com.example.tourism_app.service.InteractionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepositoryJpa interactionRepositoryJpa;
    private final PlaceRepositoryJpa placeRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    public InteractionServiceImpl(InteractionRepositoryJpa interactionRepositoryJpa, PlaceRepositoryJpa placeRepositoryJpa, UserRepositoryJpa userRepositoryJpa) {
        this.interactionRepositoryJpa = interactionRepositoryJpa;
        this.placeRepositoryJpa = placeRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public Interaction getInteractionById(Long id) {
        return interactionRepositoryJpa.getInteractionById(id);
    }

    @Override
    public Interaction createInteraction(UUID userUuid, UUID placeUuid, Integer interaction) {
        Place place = placeRepositoryJpa.getPlaceByPlaceUuid(placeUuid);
        User user = userRepositoryJpa.getUserByUserUuid(userUuid);
        Interaction newInteraction = new Interaction(userUuid, placeUuid, user, place, interaction);
        interactionRepositoryJpa.save(newInteraction);

        return newInteraction;
    }

    @Override
    public Interaction updateInteraction(Long id, UUID userUuid, UUID placeUuid, Integer interaction) {

        Interaction OldInteraction = interactionRepositoryJpa.getInteractionById(id);
        OldInteraction.setInteraction(interaction);

        if(OldInteraction.getPlace().getPlaceUuid() != placeUuid){
            Place place = placeRepositoryJpa.getPlaceByPlaceUuid(placeUuid);
            OldInteraction.setPlace(place);
            OldInteraction.setPlaceUuid(placeUuid);
        }
        if(OldInteraction.getUser().getUserUuid() != userUuid){
            User user = userRepositoryJpa.getUserByUserUuid(userUuid);
            OldInteraction.setUser(user);
            OldInteraction.setUserUuid(userUuid);
        }

        interactionRepositoryJpa.save(OldInteraction);

        return OldInteraction;
    }

    @Override
    public void deleteInteraction(Long id) {
        interactionRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Interaction> listInteractions() {
        return interactionRepositoryJpa.findAll();
    }
}
