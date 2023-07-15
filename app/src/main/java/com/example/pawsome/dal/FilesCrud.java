package com.example.pawsome.dal;

import com.example.pawsome.utils.Constants;
import com.google.firebase.storage.StorageReference;

public class FilesCrud {

    private static FilesCrud instance;
    private final StorageReference usersImagesStorageReference;
    private final StorageReference petsImagesStorageReference;

    private FilesCrud() {
        usersImagesStorageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_USERS_PROFILE_IMAGES);
        petsImagesStorageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_PETS_PROFILE_IMAGES);
    }

    public static FilesCrud getInstance() {
        if (instance == null) {
            instance = new FilesCrud();
        }
        return instance;
    }

    public StorageReference getUserFileReference(String uid) {
        return usersImagesStorageReference.child(uid);
    }

    public StorageReference getPetFileReference(String id) {
        return petsImagesStorageReference.child(id);
    }


}
