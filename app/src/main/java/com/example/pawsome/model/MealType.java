package com.example.pawsome.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MealType {

    private LocalTime time;
    private int amount;
    private String unit;
    private String foodType;
    private String name;


    public MealType() {
    }

    public MealType(LocalTime time, int amount, String unit, String foodType, String name) {
        this.time = time;
        this.amount = amount;
        this.unit = unit;
        this.foodType = foodType;
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public MealType setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public MealType setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MealType setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getFoodType() {
        return foodType;
    }

    public MealType setFoodType(String foodType) {
        this.foodType = foodType;
        return this;
    }

    public String getName() {
        return name;
    }

    public MealType setName(String name) {
        this.name = name;
        return this;
    }

    public String getTimeString(){
        if (time == null)
            return "NA";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
