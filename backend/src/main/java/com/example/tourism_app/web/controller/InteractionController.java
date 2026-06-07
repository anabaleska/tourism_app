package com.example.tourism_app.web.controller;

import com.example.tourism_app.service.InteractionService;
import com.example.tourism_app.service.PlaceService;
import com.example.tourism_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class InteractionController {

    private final UserService userService;
    private final PlaceService placeService;
    private final InteractionService interactionService;


    public InteractionController(UserService userService, PlaceService placeService, InteractionService interactionService) {
        this.userService = userService;
        this.placeService = placeService;
        this.interactionService = interactionService;
    }

    private String requireCurrentUserId() {
        try {
            return userService.getCurrentUserId();
        } catch (Exception ex) {
            throw new IllegalStateException("You must be logged in to submit an interaction.");
        }
    }


    //
    @PostMapping("/addInteraction/{interactionId}")
    public String addInteraction(Model model,
                                 @RequestParam String placeId,
                                 @RequestParam Integer interaction,
                                 @PathVariable Long interactionId) {
        if (interactionId == null || interactionId < 0) {
            throw new IllegalArgumentException("interactionId is required");
        }
        if (placeId == null || placeId.isBlank()) {
            throw new IllegalArgumentException("placeId is required");
        }
        if (interaction == null) {
            throw new IllegalArgumentException("interaction is required");
        }
        interactionService.createInteraction(requireCurrentUserId(), placeId, interaction);
        return "redirect:/home";
    }

    //
    @PostMapping("/updateInteraction/{interactionId}")
    public String updateInteraction(Model model,
                                    @RequestParam String placeId,
                                    @RequestParam Integer interaction,
                                    @PathVariable Long interactionId) {

        if (interactionId == null) {
            throw new IllegalArgumentException("interactionId is required");
        }
        if (placeId == null || placeId.isBlank()) {
            throw new IllegalArgumentException("placeId is required");
        }
        if (interaction == null) {
            throw new IllegalArgumentException("interaction is required");
        }

        interactionService.updateInteraction(interactionId, requireCurrentUserId(), placeId, interaction);
        return "redirect:/home";
    }



}
