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
import java.util.Objects;
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
    public Interaction createInteraction(String userUuid, String placeUuid, Integer interaction) {
        Place place = placeRepositoryJpa.getPlaceById(placeUuid);
        User user = userRepositoryJpa.findById(userUuid).orElseThrow(() -> new RuntimeException("User not found"));;
        Interaction newInteraction = new Interaction(user, place, interaction);
        interactionRepositoryJpa.save(newInteraction);
        return newInteraction;
    }

    @Override
    public Interaction updateInteraction(Long id, String userUuid, String placeUuid, Integer interaction) {
        Interaction OldInteraction = interactionRepositoryJpa.findById(id).orElseThrow(() -> new RuntimeException("Interaction not found"));
        if(!userUuid.equals(OldInteraction.getUser().getId())) throw new RuntimeException("User interaction not owned by you");
        if(!placeUuid.equals(OldInteraction.getPlace().getId())) throw new RuntimeException("Place interaction not found");
        OldInteraction.setInteraction(interaction);
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

    @Override
    public List<Interaction> findAllUserInteractions(String userId) {
        return interactionRepositoryJpa.findAllByUser_Id(userId);
    }

    @Override
    public Double averageInteraction(String placeId) {
        List<Interaction> interactions = interactionRepositoryJpa.findAllByPlace_Id(placeId);
        if (interactions == null || interactions.isEmpty()) {
            return 0.0;
        }

        return interactions.stream()
                .filter(Objects::nonNull)
                .map(Interaction::getInteraction)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
