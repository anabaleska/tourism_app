package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepositoryJpa extends JpaRepository<Interaction, Long> {
    Interaction getInteractionById(Long id);

}
