package com.example.pawsome.dal;

import com.example.pawsome.model.PetProfile;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DatabaseReference;

public class DBCrud {

    private static DBCrud instance;
    private final DatabaseReference usersDatabaseReference;
    private final DatabaseReference petsDatabaseReference;

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

    public void setUserInDB(UserProfile userProfile) {
        usersDatabaseReference.child(userProfile.getUid()).setValue(userProfile);
    }

    public void setPetInDB(PetProfile petProfile) {
        petsDatabaseReference.child(petProfile.getId()).setValue(petProfile);
    }

    public DatabaseReference getUserReference(String uid) {
        return usersDatabaseReference.child(uid);
    }

    public DatabaseReference getPetReference(String id) {
        return petsDatabaseReference.child(id);
    }


}
