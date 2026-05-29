package com.example.tourism_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "places")
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Place {

    @Id
    @Column(name = "place_id", nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String type;
    private String place;
    private String municipality;
    private Double lat;
    private Double lng;
    private Double rating;
    private Integer price;

    @Column(name = "time_to_visit")
    private Integer timeToVisit;

    private String tags;
    private String description;

    @Column(name = "name_normalized")
    private String nameNormalized;

    @Column(name = "model_text")
    private String modelText;

    @Column(name = "image_url")
    private String imageUrl;

    public Place() {
    }

    public Place(String placeUuid, String name, String category, String type, String place, String municipality, Double lat, Double lng, Double rating, Integer price, Integer timeToVisit, String tags, String description, String nameNormalized, String modelText, String imageUrl) {
        this.id = placeUuid;
        this.name = name;
        this.category = category;
        this.type = type;
        this.place = place;
        this.municipality = municipality;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.price = price;
        this.timeToVisit = timeToVisit;
        this.tags = tags;
        this.description = description;
        this.nameNormalized = nameNormalized;
        this.modelText = modelText;
        this.imageUrl = imageUrl;

    }

    public String getId() {
        return id;
    }

    public void setPlaceUuid(String placeUuid) {
        this.id = placeUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTimeToVisit() {
        return timeToVisit;
    }

    public void setTimeToVisit(Integer timeToVisit) {
        this.timeToVisit = timeToVisit;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameNormalized() {
        return nameNormalized;
    }

    public void setNameNormalized(String nameNormalized) {
        this.nameNormalized = nameNormalized;
    }

    public String getModelText() {
        return modelText;
    }

    public void setModelText(String modelText) {
        this.modelText = modelText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}