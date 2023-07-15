package com.example.pawsome.model;

import com.example.pawsome.utils.Constants;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PetProfile {

    private String id;
    private String name;
    private String profileImage;
    private String gender;
    private long dateOfBirth;
    private List<String> ownersIds = new ArrayList<>();
    private List<MealType> mealTypes = new ArrayList<>();
    private List<Meal> meals = new ArrayList<>();

    // TODO: change to walks like meals
    // TODO: change to walkType

    public PetProfile() {
        this.id = UUID.randomUUID().toString();
        this.profileImage = Constants.DEFAULT_PET_IMAGE_URL;
    }

    public String getId() {
        return id;
    }

    public PetProfile setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PetProfile setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public PetProfile setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PetProfile setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public List<String> getOwnersIds() {
        return ownersIds;
    }

    public PetProfile setOwnersIds(List<String> ownersIds) {
        this.ownersIds = ownersIds;
        return this;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public PetProfile setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public List<MealType> getMealTypes() {
        return mealTypes;
    }

    public PetProfile setMealTypes(List<MealType> mealTypes) {
        this.mealTypes = mealTypes;
        return this;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public PetProfile setMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

    public PetProfile addOwner(String ownerId) {
        this.ownersIds.add(ownerId);
        return this;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
    }

    public boolean addMealType(MealType mealType) {
        if(this.mealTypes.contains(mealType))
            return false;

        return this.mealTypes.add(mealType);
    }

    public boolean isOnlyOneOwner() {
        return this.ownersIds.size() == 1;
    }

    public boolean removeMealType(MealType mealType) {
        if(!this.mealTypes.contains(mealType))
            return false;

        return this.mealTypes.remove(mealType);
    }

}