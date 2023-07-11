package com.example.pawsome.current;

import androidx.annotation.NonNull;

import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CurrentPet {

    private static CurrentPet currentPet = null;
    private PetProfile petProfile = null;
    private ValueEventListener petProfileListener;

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

    public CurrentPet setPetProfile(String petId) {
        loadPetProfile(petId);
        return this;
    }

    private void loadPetProfile(String petId) {
        DatabaseReference dbRef = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_PETS);
        dbRef.child(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    petProfile = (snapshot.getValue(PetProfile.class));
                else
                    petProfile = null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String getPetId() {
        return petProfile.getId();
    }
}
