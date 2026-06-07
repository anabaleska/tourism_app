package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InteractionRepositoryJpa extends JpaRepository<Interaction, Long> {
    Interaction getInteractionById(Long id);
    List<Interaction> findAllByUser_Id(String userId);
    List<Interaction> findAllByPlace_Id(String placeId);
    Optional<Interaction> findByUser_IdAndPlace_Id(String userId, String placeId);
}
