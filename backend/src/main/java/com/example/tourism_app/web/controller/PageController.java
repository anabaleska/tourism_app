package com.example.tourism_app.web.controller;

import com.example.tourism_app.model.Place;
import com.example.tourism_app.service.InteractionService;
import com.example.tourism_app.service.PlaceService;
import com.example.tourism_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PageController {

    private final UserService userService;
    private final PlaceService placeService;
    private final InteractionService interactionService;
    private static final int PLACES_PER_PAGE = 20;

    public PageController(UserService userService, PlaceService placeService, InteractionService interactionService) {
        this.userService = userService;
        this.placeService = placeService;
        this.interactionService = interactionService;
    }

    @RequestMapping({ "/home", "/" })
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        //      list places
        //Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        //user.ifPresent(userBap -> model.addAttribute("user", userBap));
        java.util.List<Place> allPlaces = placeService.listPlaces();

        // Calculate pagination
        int totalPlaces = allPlaces.size();
        int totalPages = (int) Math.ceil((double) totalPlaces / PLACES_PER_PAGE);
        int startIndex = (page - 1) * PLACES_PER_PAGE;
        int endIndex = Math.min(startIndex + PLACES_PER_PAGE, totalPlaces);

        java.util.List<Place> paginatedPlaces = allPlaces.subList(startIndex, endIndex);

        model.addAttribute("places", paginatedPlaces);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPlaces", totalPlaces);

        return "home"; // will load resources/templates/home.html
    }

    //  Get place page
    @GetMapping("/places/{placeId}")
    public String PlacePage(@PathVariable Long placeId, Model model) {
//        Long userId = userService.getCurrentUserId();
//        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
//        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        Place place = placeService.getPlaceById(placeId);

        model.addAttribute("place", place);
        model.addAttribute("placeId", placeId);
        model.addAttribute("similarPlaces", placeService.listPlaces().stream()
                .filter(p -> p.getCategory().equals(place.getCategory()) && !p.getId().equals(placeId))
                .collect(java.util.stream.Collectors.toList()));

        return "placePage";
    }

    //  Get favourites page
    @GetMapping("/favourites")
    public String reportPostPage(@ModelAttribute Model model) {
//        Long userId = userService.getCurrentUserId();
//        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
//        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        return "favourites";
    }





}
