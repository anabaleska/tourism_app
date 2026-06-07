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
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        //      list places
        //Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        //user.ifPresent(userBap -> model.addAttribute("user", userBap));
        java.util.List<Place> allPlaces = placeService.listPlaces();
        boolean authenticated = false;
        String currentUserId = null;

        try {
            currentUserId = userService.getCurrentUserId();
            authenticated = true;
        } catch (Exception ignored) {
            // anonymous users can still browse the home page
        }

        // Calculate pagination
        int totalPlaces = allPlaces.size();
        int totalPages = (int) Math.ceil((double) totalPlaces / PLACES_PER_PAGE);
        int startIndex = (page - 1) * PLACES_PER_PAGE;
        int endIndex = Math.min(startIndex + PLACES_PER_PAGE, totalPlaces);

        java.util.List<Place> paginatedPlaces = allPlaces.subList(startIndex, endIndex);

        Map<String, Double> averageInteractions = new HashMap<>();
        for (Place place : paginatedPlaces) {
            if (place != null && place.getId() != null) {
                averageInteractions.put(place.getId(), interactionService.averageInteraction(place.getId()));
            }
        }

        Map<String, Long> userInteractionIds = new HashMap<>();
        Map<String, Integer> userInteractionValues = new HashMap<>();
        if (authenticated && currentUserId != null) {
            for (Interaction interaction : interactionService.findAllUserInteractions(currentUserId)) {
                if (interaction != null && interaction.getPlace() != null && interaction.getPlace().getId() != null) {
                    String placeId = interaction.getPlace().getId();
                    userInteractionIds.put(placeId, interaction.getId());
                    userInteractionValues.put(placeId, interaction.getInteraction());
                }
            }
        }

        model.addAttribute("places", paginatedPlaces);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPlaces", totalPlaces);
        model.addAttribute("averageInteractions", averageInteractions);
        model.addAttribute("userInteractionIds", userInteractionIds);
        model.addAttribute("userInteractionValues", userInteractionValues);
        model.addAttribute("authenticated", authenticated);

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

        return "placePage";
    }

    @GetMapping("/places/{placeId}/recommendations")
    public String contentBasedRecommendations(@PathVariable String placeId, Model model) {
        Place place = placeService.getPlaceById(placeId).orElseThrow(() -> new RuntimeException("Place not found"));

        model.addAttribute("place", place);
        model.addAttribute("placeId", placeId);
        model.addAttribute("similarPlaces", recommendationService.getContentBasedRecommendations(placeId));
        model.addAttribute("recommendationsRequested", true);

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
