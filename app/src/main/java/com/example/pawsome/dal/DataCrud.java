package com.example.pawsome.dal;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pawsome.model.PetProfile;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class DataCrud {

    private static DataCrud instance;
    private final DatabaseReference usersDatabaseReference;
    private final DatabaseReference petsDatabaseReference;

    private DataCrud() {
        usersDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_USERS);
        petsDatabaseReference = FirebaseDB.getInstance().getDatabaseReference(Constants.DB_PETS);
    }

    public static DataCrud getInstance() {
        if (instance == null) {
            instance = new DataCrud();
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

    public void deletePetFromDB(String petId) {
        petsDatabaseReference.child(petId).removeValue();
    }

    public void deletePetFromUser(String petId, String userId) {
        usersDatabaseReference.child(userId).child("petsIds").child(petId).removeValue();

        DataCrud.getInstance().getUserReference(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null)
                        _deletePetFromUser(petId, task.getResult().getValue(UserProfile.class));
                }
            }
        });
    }

    private void _deletePetFromUser(String petId, UserProfile user) {
        user.removePet(petId);
        DataCrud.getInstance().setUserInDB(user);
    }


}
