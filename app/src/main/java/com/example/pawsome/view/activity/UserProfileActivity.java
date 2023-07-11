package com.example.pawsome.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.pawsome.R;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;

    private MaterialButton profile_BTN_upload;
    private MaterialButton profile_BTN_add_pet;
    private MaterialButton profile_BTN_save;
    private MaterialButton profile_BTN_home;
    private ImageView profile_IMG_profile;
    private TextInputLayout profile_EDT_name;
    private TextInputLayout profile_EDT_phone;
    private LinearLayoutCompat profile_LAY_next_page_options;

    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    String imageUrl;
    private String fileName;

    private boolean isImageUploaded = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        storageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_USERS_PROFILE_IMAGES);

        initViews();
        setButtonsListener();
        initUserProfileData();
        setImageUploaded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null || CurrentUser.getInstance().getUserProfile() == null)
            goToLoginActivity();
    }

    private void initViews() {
        profile_IMG_profile = findViewById(R.id.profile_IMG_profile);
        profile_BTN_upload = findViewById(R.id.profile_BTN_upload);
        profile_BTN_add_pet = findViewById(R.id.profile_BTN_add_pet);
        profile_BTN_home = findViewById(R.id.profile_BTN_home);
        profile_BTN_save = findViewById(R.id.profile_BTN_save);
        profile_EDT_name = findViewById(R.id.profile_EDT_name);
        profile_EDT_phone = findViewById(R.id.profile_EDT_phone);
        profile_LAY_next_page_options = findViewById(R.id.profile_LAY_next_page_options);
    }

    private void setButtonsListener () {
        profile_IMG_profile.setOnClickListener(v -> checkPermissionAndUploadImage());
        profile_BTN_upload.setOnClickListener(v -> uploadImage());
        profile_BTN_add_pet.setOnClickListener(v -> goToPetProfileActivity());
        profile_BTN_save.setOnClickListener(v -> updateUserProfile(imageUrl));
        profile_BTN_home.setOnClickListener(v -> goToMainActivity());
    }

    private void initUserProfileData() {
        UserProfile userProfile = CurrentUser.getInstance().getUserProfile();

        if(userProfile.getName() != null && !userProfile.getName().isEmpty())
            profile_EDT_name.getEditText().setText(userProfile.getName());
        if(userProfile.getPhoneNumber() != null && !userProfile.getPhoneNumber().isEmpty())
            profile_EDT_phone.getEditText().setText(userProfile.getPhoneNumber());
        if(userProfile.getProfileImage() != null && !userProfile.getProfileImage().isEmpty())
            Glide.with(UserProfileActivity.this)
                    .load(userProfile.getProfileImage())
                    .into(profile_IMG_profile);
    }

    private void updateUserProfile(String imageUrl) {
        if (isImageUploaded) {
            UserProfile userProfile = CurrentUser.getInstance().getUserProfile();
            String phone = profile_EDT_phone.getEditText().getText().toString();
            String name = profile_EDT_name.getEditText().getText().toString();
            userProfile
                    .setPhoneNumber(phone)
                    .setName(name)
                    .setProfileImage(imageUrl)
                    .setRegistered(true);

            FirebaseDB.getInstance().getUsersReference().child(userProfile.getUid()).setValue(userProfile);
            profileSaved();
        }
        else
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image and saving data");

        this.fileName = CurrentUser.getInstance().getUid();
        StorageReference reference = storageReference.child(this.fileName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imageUrl = task.getResult().toString();
                                setImageUploaded();
                            }
                        });
                        profile_IMG_profile.setImageURI(null);
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Failed Uploaded Image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkPermissionAndUploadImage() {
        // Check if permission to read external storage is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES},
                    IMAGE_UPLOAD_REQUEST_CODE);
        } else {
            // Permission already granted, start image selection process
            openImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start image selection process
                openImagePicker();
            } else {
                // Permission denied
                Log.e("Permission Denied", "Storage permission denied");
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData() != null) {
            // Get the URI of the selected image
            setImageNotUploaded();
            imageUri = data.getData();
            profile_IMG_profile.setImageURI(imageUri);
        }
    }

    private void setImageUploaded() {
        isImageUploaded = true;
        profile_BTN_upload.setEnabled(false);
        profile_BTN_upload.setText("Image Ready");
        profile_IMG_profile.setImageURI(imageUri);
    }

    private void setImageNotUploaded() {
        isImageUploaded = false;
        profile_BTN_upload.setEnabled(true);
        profile_BTN_upload.setText("Upload Image");
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToPetProfileActivity() {
        Intent intent = new Intent(this, PetProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void profileSaved() {
        profile_BTN_save.setEnabled(false);
        profile_BTN_save.setText("Profile Saved");
        profile_LAY_next_page_options.setVisibility(View.VISIBLE);
    }

}