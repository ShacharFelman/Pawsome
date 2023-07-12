package com.example.pawsome.model;

import com.example.pawsome.utils.Constants;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private List<String> ownersIds;
    private String dateOfBirth;
    // TODO: change to walkType
    private List<LocalTime> WalksTimes;
    private List<MealType> mealTypes;
    // TODO: change to walks
    private Map<LocalDateTime, String> walksIds;
    private List<Meal> meals;
    private String defaultFoodType;
    private String defaultFoodAmount;
    private String defaultWalkDuration;


    public PetProfile() {
        this.id = UUID.randomUUID().toString();
        this.ownersIds = new ArrayList<>();
        this.WalksTimes = new ArrayList<>();
        this.mealTypes = new ArrayList<>();
        this.walksIds = new HashMap<>();
        this.meals = new ArrayList<>();
        this.profileImage = Constants.DEFAULT_USER_IMAGE_URL;
    }

    public PetProfile(String name, String gender, String ownerId, String dateOfBirth, List<LocalTime> walksTimes, List<MealType> mealTypes) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.profileImage = Constants.DEFAULT_PET_IMAGE_URL;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.WalksTimes = walksTimes == null ? new ArrayList<>() : walksTimes;
        this.mealTypes = mealTypes == null ? new ArrayList<>() : mealTypes;
        this.ownersIds = new ArrayList<>();
        this.ownersIds.add(ownerId);
        this.walksIds = new HashMap<>();
        this.meals = new ArrayList<>();
    }

    public PetProfile(String name, String gender, String ownerId, String dateOfBirth) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.profileImage = Constants.DEFAULT_PET_IMAGE_URL;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.WalksTimes = new ArrayList<>();
        this.mealTypes = new ArrayList<>();
        this.ownersIds = new ArrayList<>();
        this.ownersIds.add(ownerId);
        this.walksIds = new HashMap<>();
        this.meals = new ArrayList<>();
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public PetProfile setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public List<LocalTime> getWalksTimes() {
        return WalksTimes;
    }

    public PetProfile setWalksTimes(List<LocalTime> walksTimes) {
        WalksTimes = walksTimes;
        return this;
    }

    public List<MealType> getMealPrefs() {
        return mealTypes;
    }

    public PetProfile setMealPrefs(List<MealType> mealTypes) {
        this.mealTypes = mealTypes;
        return this;
    }

    public Map<LocalDateTime, String> getWalksIds() {
        return walksIds;
    }

    public PetProfile setWalksIds(Map<LocalDateTime, String> walksIds) {
        this.walksIds = walksIds;
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

    public String getDefaultFoodType() {
        return defaultFoodType;
    }

    public PetProfile setDefaultFoodType(String defaultFoodType) {
        this.defaultFoodType = defaultFoodType;
        return this;
    }

    public String getDefaultFoodAmount() {
        return defaultFoodAmount;
    }

    public PetProfile setDefaultFoodAmount(String defaultFoodAmount) {
        this.defaultFoodAmount = defaultFoodAmount;
        return this;
    }

    public String getDefaultWalkDuration() {
        return defaultWalkDuration;
    }

    public PetProfile setDefaultWalkDuration(String defaultWalkDuration) {
        this.defaultWalkDuration = defaultWalkDuration;
        return this;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
    }
}