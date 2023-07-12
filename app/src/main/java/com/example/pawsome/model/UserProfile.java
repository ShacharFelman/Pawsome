package com.example.pawsome.model;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private String name;
    private String uid;
    private String profileImage;
    private String email;
    private String phoneNumber;
    private List<String> petsIds;
    private boolean registered;

    public UserProfile() {
        this.petsIds = new ArrayList<>();
    }

    public UserProfile(String name, String uid, String email) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.petsIds = new ArrayList<>();
        this.registered = false;
        this.profileImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
    }

    public String getName() {
        return name;
    }

    public UserProfile setName(String name) {
        this.name = name;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public UserProfile setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public UserProfile setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserProfile setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public List<String> getPetsIds() {
        return petsIds;
    }

    public UserProfile setPetsIds(List<String> petsIds) {
        this.petsIds = petsIds;
        return this;
    }

    public boolean getRegistered() {
        return registered;
    }

    public UserProfile setRegistered(boolean registered) {
        this.registered = registered;
        return this;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", petsIds=" + petsIds +
                ", isRegistered=" + registered +
                '}';
    }
}
