package com.example.pawsome.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Meal {

    private String id;
    private UserProfile owner;
    private String petId;
    private LocalDateTime dateTime;
    private int amount;
    private String unit;
    private String foodType;
    private String note;

    public Meal() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Meal setId(String id) {
        this.id = id;
        return this;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public Meal setOwner(UserProfile owner) {
        this.owner = owner;
        return this;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Meal setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Meal setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public Meal setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getFoodType() {
        return foodType;
    }

    public Meal setFoodType(String foodType) {
        this.foodType = foodType;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Meal setNote(String note) {
        this.note = note;
        return this;
    }

    public String getPetId() {
        return petId;
    }

    public Meal setPetId(String petId) {
        this.petId = petId;
        return this;
    }
}
