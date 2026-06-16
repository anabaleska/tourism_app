package com.example.tourism_app.web.controller;

import com.example.tourism_app.model.Interaction;
import com.example.tourism_app.model.Place;
import com.example.tourism_app.model.User;
import com.example.tourism_app.service.InteractionService;
import com.example.tourism_app.service.PlaceService;
import com.example.tourism_app.service.RecommendationService;
import com.example.tourism_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private final UserService userService;
    private final PlaceService placeService;
    private final InteractionService interactionService;
    private final RecommendationService recommendationService;
    private static final int PLACES_PER_PAGE = 20;

    public PageController(UserService userService, PlaceService placeService, InteractionService interactionService, RecommendationService recommendationService) {
        this.userService = userService;
        this.placeService = placeService;
        this.interactionService = interactionService;
        this.recommendationService = recommendationService;
    }

    @RequestMapping({ "/home", "/" })
    public String home(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) Map<String, String> allParams,
                       Model model) {
//        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
//        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        List<Place>  allPlaces;
        Map<String, List<String>> filterOptions = placeService.getAllFilterOptions();

        if(search != null){
            allPlaces = placeService.listPlacesByNameContaining(search);
        } else{
            if(allParams != null){
                allPlaces = placeService.listFilteredPlaces(allParams.get("types"), allParams.get("categories"),
                        allParams.get("municipalities"), allParams.get("tags"));
            } else {
                allPlaces = placeService.listPlaces();
            }
        }

        Collections.shuffle(allPlaces);

        // Calculate pagination
        int totalPlaces = allPlaces.size();
        int totalPages = (int) Math.ceil((double) totalPlaces / PLACES_PER_PAGE);
        int startIndex = (page - 1) * PLACES_PER_PAGE;
        int endIndex = Math.min(startIndex + PLACES_PER_PAGE, totalPlaces);

        List<Place> paginatedPlaces = allPlaces.subList(startIndex, endIndex);

        List<Interaction> userInteractedPlaces = interactionService.findAllUserInteractions(userService.getCurrentUserId());

        Map<String, Integer> personalPlaceRatings = userInteractedPlaces.stream()
                .collect(Collectors.toMap(
                        i -> i.getPlace().getId(),
                        Interaction::getInteraction,
                        (existing, replacement) -> replacement // Resolves duplicate keys if a place appears more than once
                ));

        model.addAttribute("places", paginatedPlaces);
        model.addAttribute("filterOptions", filterOptions);
        model.addAttribute("userInteractions", personalPlaceRatings);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPlaces", totalPlaces);

        return "home"; // will load resources/templates/home.html
    }

    //  Get place page
    @GetMapping("/places/{placeId}")
    public String PlacePage(@PathVariable String placeId, Model model) {
//        Long userId = userService.getCurrentUserId();
//        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
//        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        System.out.println("Incoming placeId = " + placeId);
        Place place = placeService.getPlaceById(placeId).orElseThrow(() -> new RuntimeException("Place not found"));

        model.addAttribute("place", place);
        model.addAttribute("placeId", placeId);
        model.addAttribute("recommendationsRequested", false);
        addPlaceInteractionModel(place, model);

        return "placePage";
    }

    @GetMapping("/places/{placeId}/recommendations")
    public String contentBasedRecommendations(@PathVariable String placeId, Model model) {
        Place place = placeService.getPlaceById(placeId).orElseThrow(() -> new RuntimeException("Place not found"));

        model.addAttribute("place", place);
        model.addAttribute("placeId", placeId);
        model.addAttribute("similarPlaces", recommendationService.getContentBasedRecommendations(placeId));
        model.addAttribute("recommendationsRequested", true);
        addPlaceInteractionModel(place, model);

        return "placePage";
    }

    //  Get favorites page
    @GetMapping("/favorites")
    public String favorites(Model model) {
        String userId = userService.getCurrentUserId();
        addFavoritesModel(userId, model);
        model.addAttribute("collaborativeRecommendationsRequested", false);

        return "favorites";
    }

    @GetMapping("/favorites/recommendations")
    public String collaborativeRecommendations(Model model) {
        String userId = userService.getCurrentUserId();
        addFavoritesModel(userId, model);
        List<Place> collaborativeRecommendations = recommendationService.getCollaborativeRecommendations(userId);
        Map<String, Double> averageInteractions = (Map<String, Double>) model.getAttribute("averageInteractions");

        if (averageInteractions != null) {
            for (Place place : collaborativeRecommendations) {
                if (place != null && place.getId() != null) {
                    averageInteractions.put(place.getId(), interactionService.averageInteraction(place.getId()));
                }
            }
        }

        model.addAttribute("collaborativeRecommendations", collaborativeRecommendations);
        model.addAttribute("collaborativeRecommendationsRequested", true);

        return "favorites";
    }

    private void addFavoritesModel(String userId, Model model) {
        Optional<User> userOpt = userService.getUserById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        List<Interaction> favorites = interactionService.findAllUserInteractions(userId).stream()
                .filter(Objects::nonNull)
                .filter(interaction -> interaction.getPlace() != null)
                .toList();

        Map<String, Double> averageInteractions = new HashMap<>();
        Map<String, Integer> userInteractions = new HashMap<>();
        for (Interaction favorite : favorites) {
            if (favorite.getPlace().getId() != null) {
                String placeId = favorite.getPlace().getId();
                averageInteractions.put(placeId, interactionService.averageInteraction(placeId));
                userInteractions.put(placeId, favorite.getInteraction());
            }
        }

        model.addAttribute("user", userOpt.get());
        model.addAttribute("favorites", favorites);
        model.addAttribute("averageInteractions", averageInteractions);
        model.addAttribute("userInteractions", userInteractions);
    }

    private void addPlaceInteractionModel(Place place, Model model) {
        boolean authenticated = false;
        Interaction currentInteraction = null;

        if (place != null && place.getId() != null) {
            model.addAttribute("averageInteraction", interactionService.averageInteraction(place.getId()));

            try {
                String currentUserId = userService.getCurrentUserId();
                authenticated = true;
                currentInteraction = interactionService
                        .findUserPlaceInteraction(currentUserId, place.getId())
                        .orElse(null);
            } catch (Exception ignored) {
                // Anonymous users can view place details, but cannot submit interactions.
            }
        } else {
            model.addAttribute("averageInteraction", 0.0);
        }

        model.addAttribute("authenticated", authenticated);
        model.addAttribute("currentInteraction", currentInteraction);
    }

    @GetMapping("/profile")
    public String profile(Model model) { String userId = userService.getCurrentUserId();
        Optional<User> userOpt = userService.getUserById(userId);

        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
//      list favorites from user
        List<Interaction> favorites =
                interactionService.findAllUserInteractions(userService.getCurrentUserId());
        model.addAttribute("favorites", favorites);

        return "profile"; // will load resources/templates/profile.html
    }

}
