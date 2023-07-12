package com.example.pawsome.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Meal {

    private UserProfile owner;
    private String petId;
    private long dateTime;
    private String note;
    private MealType mealType;

    private int amount;
    private String unit;
    private String foodType;

    public Meal() {
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

    public MealType getMealType() {
        return mealType;
    }

    public Meal setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public LocalDateTime getDateTimeAsLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.dateTime), ZoneId.systemDefault());
    }

    public Meal setDateTimeAsLocalDateTime(LocalDateTime dateTime) {
        ZonedDateTime zdt = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        this.dateTime = zdt.toInstant().toEpochMilli();
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

    @Override
    public String toString() {
        return "Meal{" +
                "owner=" + owner +
                ", petId='" + petId + '\'' +
                ", dateTime=" + dateTime +
                ", note='" + note + '\'' +
                ", mealType=" + mealType +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", foodType='" + foodType + '\'' +
                '}';
    }

    public Meal setMealDefaultDataFromMealType() {
        if(this.mealType != null) {
            this.amount = this.mealType.getAmount();
            this.unit = this.mealType.getUnit();
            this.foodType = this.mealType.getFoodType();
        }

        return this;
    }

    public Meal setMealDefaultDataFromMealType(MealType mealType) {
        this.amount = mealType.getAmount();
        this.unit = mealType.getUnit();
        this.foodType = mealType.getFoodType();
        return this;
    }
}
