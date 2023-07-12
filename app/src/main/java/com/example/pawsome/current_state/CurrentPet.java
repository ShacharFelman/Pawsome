package com.example.pawsome.current_state;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentPet {

    private static CurrentPet currentPet = null;
    private PetProfile petProfile = null;
    private ValueEventListener petProfileListener;
    private final List<PetProfileObserver> observers = new ArrayList<>();

    private CurrentPet() {
    }

    public static CurrentPet getInstance(){
        if (currentPet == null)
            currentPet = new CurrentPet();
        return currentPet;
    }

    public PetProfile getPetProfile() {
        return petProfile;
    }

    public CurrentPet setPetProfile(PetProfile petProfile) {
        this.petProfile = petProfile;
        notifyObservers();
        return this;
    }

    public CurrentPet setPetProfileById(String petId) {
        loadPetProfile(petId);
        return this;
    }

    private void loadPetProfile(String petId) {
        DBCrud.getInstance().getPetReference(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    petProfile = (snapshot.getValue(PetProfile.class));
                    notifyObservers();
                }
                else {
                    Log.d("pet_null", "loadPetProfile: PetId = " + petId + " does not exist");
                    petProfile = null;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String getPetId() {
        return petProfile.getId();
    }

    public void addObserver(PetProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PetProfileObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (PetProfileObserver observer : observers) {
            observer.onPetProfileChanged();
        }
    }
}
