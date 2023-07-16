package com.example.pawsome.current_state.singletons;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pawsome.current_state.observers.UserPetsListObserver;
import com.example.pawsome.current_state.observers.UserProfileObserver;
import com.example.pawsome.dal.DataCrud;
import com.example.pawsome.model.PetProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserPetsList implements UserProfileObserver {
    private static CurrentUserPetsList currentUserPetsList = null;
    private List<PetProfile> pets = new ArrayList<>();
    private final List<UserPetsListObserver> observers = new ArrayList<>();

    private boolean isPetsListLoaded = false;

    private CurrentUserPetsList() {
        CurrentUser.getInstance().registerListener(this);
        getPetsData();
    }

    public static CurrentUserPetsList getInstance(){
        if (currentUserPetsList == null)
            currentUserPetsList = new CurrentUserPetsList();
        return currentUserPetsList;
    }

    public List<PetProfile> getPets() {
        return pets;
    }

    private void getPetsData() {
        if (CurrentUser.getInstance().getUserProfile().hasPets()) {
            isPetsListLoaded = false;
            pets.clear();
            for (String petId : CurrentUser.getInstance().getUserProfile().getPetsIds()) {
                DataCrud.getInstance().getPetReference(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            pets.add(snapshot.getValue(PetProfile.class));
                            if (pets.size() == CurrentUser.getInstance().getUserProfile().getPetsIds().size()) {
                                isPetsListLoaded = true;
                                notifyObservers();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }

    public boolean isPetsListLoaded() {
        return isPetsListLoaded;
    }

    public void registerListener(UserPetsListObserver observer) {
        observers.add(observer);
    }

    public void unregisterListener(UserPetsListObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (UserPetsListObserver observer : observers) {
            observer.onPetsListChanged();
        }
    }

    @Override
    public void onPetsListChanged() {
        if (isPetsListLoaded)
            getPetsData();
    }
}
