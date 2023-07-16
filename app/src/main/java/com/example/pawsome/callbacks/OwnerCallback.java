package com.example.pawsome.callbacks;

import com.example.pawsome.model.UserProfile;

public interface OwnerCallback {
    void deleteClicked(UserProfile user, int position);
}
