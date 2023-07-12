package com.example.pawsome.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Meal {

    private String id;
    private UserProfile owner;
    private String petId;
//    private LocalDateTime dateTime;
    private long dateTime;

    private int amount;
    private String unit;
    private String foodType;
    private String note;
    private String mealType;

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

    public long getDateTime() {
        return dateTime;
    }

    public Meal setDateTime(long dateTime) {
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

    public String getMealType() {
        return mealType;
    }

    public Meal setMealType(String mealType) {
        this.mealType = mealType;
        return this;
    }

    public LocalDateTime getDateTimeAsLocalDateTime() {
        Instant instant = Instant.ofEpochMilli(this.dateTime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        String date = localDateTime.format(dateFormatter);
//        String time = localDateTime.format(timeFormatter);
        return localDateTime;
    }

    public Meal setDateTimeAsLocalDateTime(LocalDateTime dateTime) {
        ZonedDateTime zdt = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        this.dateTime = zdt.toInstant().toEpochMilli();
        return this;
    }
}
