package com.example.pawsome.callbacks;

import com.example.pawsome.model.Meal;
import com.example.pawsome.model.PetProfile;

public interface PetCallback {
    void deleteClicked(PetProfile pet, int position);
    void itemClicked(PetProfile pet, int position);
}
