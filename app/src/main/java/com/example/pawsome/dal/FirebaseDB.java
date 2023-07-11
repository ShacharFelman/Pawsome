package com.example.pawsome.dal;

import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseDB {

    private static FirebaseDB instance;

    private FirebaseStorage storageDB;
    private FirebaseDatabase realtimeDB;

    private FirebaseDB() {
        storageDB = FirebaseStorage.getInstance();
        realtimeDB = FirebaseDatabase.getInstance();
    }

    public static FirebaseDB getInstance() {
        if (instance == null) {
            instance = new FirebaseDB();
        }
        return instance;
    }

    public FirebaseStorage getStorageDB() {
        return storageDB;
    }

    public FirebaseDatabase getRealtimeDB() {
        return realtimeDB;
    }

    public DatabaseReference getDatabaseReference(String path) {
        return realtimeDB.getReference(path);
    }

    public StorageReference getStorageReference(String path) {
        return storageDB.getReference(path);
    }

    public DatabaseReference getUsersReference() {
        return getDatabaseReference(Constants.DB_USERS);
    }

}
