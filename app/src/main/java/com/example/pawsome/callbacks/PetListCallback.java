package com.example.pawsome.callbacks;

import com.example.pawsome.model.PetProfile;

public interface PetListCallback {
    void deleteClicked(PetProfile pet, int position);
    void itemClicked(PetProfile pet, int position);
}
