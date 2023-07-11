package com.example.pawsome.dal;

import androidx.annotation.NonNull;

import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DBCrud {

    private static DBCrud instance;
    private DatabaseReference usersDatabaseReference;
    private DatabaseReference petsDatabaseReference;
    private DatabaseReference mealsDatabaseReference;
    private DatabaseReference walksDatabaseReference;


    private DBCrud() {
        usersDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_USERS);
        petsDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_PETS);
        mealsDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_MEALS);
        walksDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_WALKS);
    }

    public static DBCrud getInstance() {
        if (instance == null) {
            instance = new DBCrud();
        }
        return instance;
    }

    public void setUserInDB(String uid, UserProfile userProfile) {
        usersDatabaseReference.child(uid).setValue(userProfile);
    }

    public void setPetInDB(String petId, PetProfile petProfile) {
        petsDatabaseReference.child(petId).setValue(petProfile);
    }


}
