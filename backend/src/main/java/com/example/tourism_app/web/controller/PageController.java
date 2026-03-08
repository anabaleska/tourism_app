package com.example.tourism_app.web.controller;

import com.example.tourism_app.service.InteractionService;
import com.example.tourism_app.service.PlaceService;
import com.example.tourism_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class PageController {

    private final UserService userService;
    private final PlaceService placeService;
    private final InteractionService interactionService;

    public PageController(UserService userService, PlaceService placeService, InteractionService interactionService) {
        this.userService = userService;
        this.placeService = placeService;
        this.interactionService = interactionService;
    }

    @RequestMapping({ "/home", "/" })
    public String home(Model model) {
        //      list places
        //Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        //user.ifPresent(userBap -> model.addAttribute("user", userBap));
        model.addAttribute("places", placeService.listPlaces());
        return "home"; // will load resources/templates/home.html
    }



}
