package com.example.pawsome.dal;

import com.example.pawsome.model.PetProfile;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DatabaseReference;

public class DBCrud {

    private static DBCrud instance;
    private DatabaseReference usersDatabaseReference;
    private DatabaseReference petsDatabaseReference;

    private DBCrud() {
        usersDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_USERS);
        petsDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_PETS);
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
