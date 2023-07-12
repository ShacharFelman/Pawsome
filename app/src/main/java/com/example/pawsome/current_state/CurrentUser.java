package com.example.pawsome.current_state;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CurrentUser {

    private static CurrentUser currentUser = null;
    private UserProfile userProfile = null;

    private final FirebaseUser user;

    private CurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            loadUserProfile();
        }
    }

    public static CurrentUser getInstance(){
        if (currentUser == null)
            currentUser = new CurrentUser();
        return currentUser;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public CurrentUser setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    private void loadUserProfile() {
        DBCrud.getInstance().getUserReference(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userProfile = (snapshot.getValue(UserProfile.class));
                    setCurrentPet();
                }
                else
                    userProfile = null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String getUid() {
        if(getUserProfile() != null)
            return getUserProfile().getUid();

        return null;
    }

    public void setCurrentPet() {
        Log.d("pet_null", "setCurrentPet: " + userProfile.getPetsIds());
        if(userProfile.getPetsIds() == null || userProfile.getPetsIds().isEmpty())
            return;

        CurrentPet.getInstance().setPetProfileById(userProfile.getPetsIds().get(0));
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "userProfile=" + userProfile +
                ", user=" + user +
                '}';
    }
}
