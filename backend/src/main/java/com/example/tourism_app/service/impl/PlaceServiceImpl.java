package com.example.tourism_app.service.impl;

import com.example.tourism_app.model.Place;
import com.example.tourism_app.repository.jpa.PlaceRepositoryJpa;
import com.example.tourism_app.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepositoryJpa placeRepositoryJpa;

    public PlaceServiceImpl(PlaceRepositoryJpa placeRepositoryJpa) {
        this.placeRepositoryJpa = placeRepositoryJpa;
    }

    @Override
    public Optional<Place> getPlaceById(String id) {
        return placeRepositoryJpa.findById(id);
    }

    @Override
    public Place createPlace(String placeUuid, String name, String category, String type, String place, String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl) {
        Place newPlace = new Place(placeUuid, name, category, type, place, municipality, lat, lng, rating, price, timeToVisit, tags, description, nameNormalized, modelText, imageUrl);
        placeRepositoryJpa.save(newPlace);

        return newPlace;
    }

    @Override
    public Place updatePlace(Long id, UUID placeUuid, String name, String category, String type, String place, String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl) {
        return null;
    }


//    @Override
//    public Place updatePlace(UUID id, UUID placeUuid, String name, String category, String type, String place, String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl) {
//        Place oldPlace = placeRepositoryJpa.findById(id);
//
//        oldPlace.setPlaceUuid(placeUuid);oldPlace.setName(name);oldPlace.setCategory(category);oldPlace.setType(type);
//        oldPlace.setPlace(place);oldPlace.setMunicipality(municipality);oldPlace.setLat(lat);oldPlace.setLng(lng);
//        oldPlace.setRating(rating);oldPlace.setPrice(price);oldPlace.setTimeToVisit(timeToVisit);oldPlace.setTags(tags);
//        oldPlace.setDescription(description);oldPlace.setNameNormalized(nameNormalized);oldPlace.setModelText(modelText);
//        oldPlace.setImageUrl(imageUrl);
//
//        placeRepositoryJpa.save(oldPlace);
//
//        return oldPlace;
//    }

    @Override
    public void deletePlace(String id) {
        placeRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Place> listPlaces() {
        return placeRepositoryJpa.findAll();
    }

    @Override
    public List<Place> listPlacesByNameContaining(String search) {
        return placeRepositoryJpa.getPlacesByNameContaining(search);
    }

    @Override
    public List<Place> listFilteredPlaces(String type, String category, String municipality, String tag) {
        return placeRepositoryJpa.findFilteredPlaces(type, category, municipality, tag);
    }

    @Override
    public Map<String, List<String>> getAllFilterOptions() {
        List<Place> allPlaces = placeRepositoryJpa.findAll();
        Map<String, List<String>> filterOptions = new HashMap<>();

        filterOptions.put("categories", allPlaces.stream().map(Place::getCategory).distinct().toList());
        filterOptions.put("municipalities", allPlaces.stream().map(Place::getMunicipality).distinct().toList());
        filterOptions.put("types", allPlaces.stream().map(Place::getType).distinct().toList());
        filterOptions.put("tags", allPlaces.stream().map(Place::getTags).distinct().toList());

        return filterOptions;
    }
}
