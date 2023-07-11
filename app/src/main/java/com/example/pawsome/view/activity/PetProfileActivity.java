package com.example.pawsome.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;

import com.example.pawsome.R;
import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.current.CurrentPet;
import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PetProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;

    private MaterialButton pet_BTN_upload;
    private MaterialButton pet_BTN_save;
    private MaterialButton pet_BTN_date_of_birth;
    private MaterialButton pet_BTN_add_meal;
    private MaterialButton pet_BTN_add_walk;
    private ImageView pet_IMG_profile;
    private TextInputLayout pet_EDT_name;
    private TextInputLayout pet_EDT_date_of_birth;
    private AppCompatSpinner pet_SP_gender;


    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    String imageUrl;
    private String fileName;

    private boolean isImageUploaded = true;
    private PetProfile petProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        storageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_PETS_PROFILE_IMAGES);

        initViews();
        setButtonsListener();
        setImageUploaded();
        initPetProfile();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null || CurrentUser.getInstance().getUserProfile() == null)
            goToLoginActivity();
    }

    private void initViews() {
        pet_IMG_profile = findViewById(R.id.pet_IMG_profile);
        pet_BTN_upload = findViewById(R.id.pet_BTN_upload);
        pet_BTN_save = findViewById(R.id.pet_BTN_save);
        pet_BTN_date_of_birth = findViewById(R.id.pet_BTN_date_of_birth);
        pet_BTN_add_meal = findViewById(R.id.pet_BTN_add_meal);
        pet_BTN_add_walk = findViewById(R.id.pet_BTN_add_walk);
        pet_EDT_name = findViewById(R.id.pet_EDT_name);
        pet_EDT_date_of_birth = findViewById(R.id.pet_EDT_date_of_birth);
        pet_SP_gender = findViewById(R.id.pet_SP_gender);
    }

    private void setButtonsListener () {
        pet_IMG_profile.setOnClickListener(v -> checkPermissionAndUploadImage());
        pet_BTN_upload.setOnClickListener(v -> uploadImage());
        pet_BTN_save.setOnClickListener(v -> updatePetProfile(imageUrl));
        pet_BTN_date_of_birth.setOnClickListener(v -> setDate());
    }

    private void setDate(){
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.show(getSupportFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            pet_EDT_date_of_birth.getEditText().setText(simpleDateFormat.format(new Date((long)selection)));
        });
    }

    private void updatePetProfile(String imageUrl) {
        if (isImageUploaded) {
            String name = pet_EDT_name.getEditText().getText().toString();
            String gender = pet_SP_gender.getSelectedItem().toString();
            String dateOfBirth = pet_EDT_date_of_birth.getEditText().getText().toString();

            petProfile
                    .setName(name)
                    .setGender(gender)
                    .setDateOfBirth(dateOfBirth)
                    .setProfileImage(imageUrl);

            CurrentUser.getInstance().getUserProfile().getPetsIds().add(petProfile.getId());
            CurrentPet.getInstance().setPetProfile(petProfile.getId());
            DBCrud.getInstance().setPetInDB(petProfile.getId(), petProfile);
            DBCrud.getInstance().setUserInDB(CurrentUser.getInstance().getUid(), CurrentUser.getInstance().getUserProfile());
            goToMainActivity();
            finish();
        }
        else
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
    }

    private void createPetProfile(String imageUrl) {
        if (isImageUploaded) {
            String name = pet_EDT_name.getEditText().getText().toString();
            String gender = pet_SP_gender.getSelectedItem().toString();
            String dateOfBirth = pet_EDT_date_of_birth.getEditText().getText().toString();

            PetProfile petProfile = new PetProfile(name, gender, CurrentUser.getInstance().getUid(), dateOfBirth);

            CurrentUser.getInstance().getUserProfile().getPetsIds().add(petProfile.getId());
            CurrentPet.getInstance().setPetProfile(petProfile.getId());
            DBCrud.getInstance().setPetInDB(petProfile.getId(), petProfile);
            DBCrud.getInstance().setUserInDB(CurrentUser.getInstance().getUid(), CurrentUser.getInstance().getUserProfile());
            finish();
        }
        else
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image and saving data");

        this.fileName = petProfile.getId();
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
                        pet_IMG_profile.setImageURI(null);
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(PetProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(PetProfileActivity.this, "Failed Uploaded Image", Toast.LENGTH_SHORT).show();
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
            pet_IMG_profile.setImageURI(imageUri);
        }
    }

    private void setImageUploaded() {
        isImageUploaded = true;
        pet_BTN_upload.setEnabled(false);
        pet_BTN_upload.setText("Image Ready");
        pet_IMG_profile.setImageURI(imageUri);
    }

    private void setImageNotUploaded() {
        isImageUploaded = false;
        pet_BTN_upload.setEnabled(true);
        pet_BTN_upload.setText("Upload Image");
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

    private void initPetProfile() {
        petProfile = new PetProfile().addOwner(CurrentUser.getInstance().getUid());
    }

}