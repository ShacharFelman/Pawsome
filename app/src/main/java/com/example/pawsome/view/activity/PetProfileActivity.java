package com.example.pawsome.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pawsome.adapters.MealTypeAdapter;
import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.databinding.ActivityPetProfileBinding;
import com.example.pawsome.model.MealType;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PetProfileActivity extends AppCompatActivity {

    private ActivityPetProfileBinding binding;
    private StorageReference storageReference;
    private MealTypeAdapter mealTypeAdapter;
    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;
    private Uri imageUri;
    String imageUrl;
    private String fileName;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private boolean isImageUploaded = true;
    private PetProfile petProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_PETS_PROFILE_IMAGES);

        initPetProfile();

        initPetView();
        initAddMealTypeView();

        initImagePickerLauncher();
        initRecyclerView();

        setCallbacks();

        setImageUploaded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null || CurrentUser.getInstance().getUserProfile() == null)
            goToLoginActivity();
    }

    private void initPetView() {
        setPetButtonsListener();
        setPetTextChangedListener();
    }

    private void initAddMealTypeView() {
        setAddMealTypeButtonsListener();
        setAddMealTypeTextChangedListener();
        cancelMealType();
    }

    private void setCallbacks() {
        mealTypeAdapter.setGroupCallback((mealType, position) -> petProfile.removeMealType(mealType));
    }

    private void setPetButtonsListener() {
        binding.petIMGProfile.setOnClickListener(v -> checkPermissionAndUploadImage());
        binding.petBTNSave.setOnClickListener(v -> updatePetProfile(imageUrl));
        binding.petBTNDateOfBirth.setOnClickListener(v -> setDate());
        binding.petBTNAddMeal.setOnClickListener(v -> addMealType());
    }

    private void setAddMealTypeButtonsListener() {
        binding.petCVAddMealType.petMealBTNSave.setOnClickListener(v -> saveMealType());
        binding.petCVAddMealType.petMealBTNCancel.setOnClickListener(v -> cancelMealType());
        binding.petCVAddMealType.petMealBTNTime.setOnClickListener(v -> setTime());
    }

    private void setAddMealTypeTextChangedListener() {
        binding.petCVAddMealType.petMealEDTName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                if (!binding.petCVAddMealType.petMealEDTName.getEditText().getText().toString().isEmpty())
                    binding.petCVAddMealType.petMealEDTName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.petCVAddMealType.petMealEDTAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence amount, int start, int before, int count) {
                if (!binding.petCVAddMealType.petMealEDTAmount.getEditText().getText().toString().isEmpty())
                    binding.petCVAddMealType.petMealEDTAmount.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.petCVAddMealType.petMealEDTUnit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence unit, int start, int before, int count) {
                if (!binding.petCVAddMealType.petMealEDTUnit.getEditText().getText().toString().isEmpty())
                    binding.petCVAddMealType.petMealEDTUnit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.petCVAddMealType.petMealEDTType.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence type, int start, int before, int count) {
                if (!binding.petCVAddMealType.petMealEDTType.getEditText().getText().toString().isEmpty())
                    binding.petCVAddMealType.petMealEDTType.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.petCVAddMealType.petMealEDTTime.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence time, int start, int before, int count) {
                if (!binding.petCVAddMealType.petMealEDTTime.getEditText().getText().toString().isEmpty())
                    binding.petCVAddMealType.petMealEDTTime.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setPetTextChangedListener() {
        binding.petEDTName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                if (!binding.petEDTName.getEditText().getText().toString().isEmpty())
                    binding.petEDTName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.petEDTDateOfBirth.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence amount, int start, int before, int count) {
                if (!binding.petEDTDateOfBirth.getEditText().getText().toString().isEmpty())
                    binding.petEDTDateOfBirth.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initRecyclerView() {
        mealTypeAdapter = new MealTypeAdapter(this, petProfile.getMealTypes());
        binding.petLSTMeals.setLayoutManager(new LinearLayoutManager(this));
        binding.petLSTMeals.setAdapter(mealTypeAdapter);
    }

    private void initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        imageUri = result.getData().getData();
                        binding.petIMGProfile.setImageURI(imageUri);
                        uploadImage();
                    }
                }
        );
    }

    private void clearAddMealTypeView() {
        binding.petCVAddMealType.petMealEDTName.getEditText().setText("");
        binding.petCVAddMealType.petMealEDTAmount.getEditText().setText("");
        binding.petCVAddMealType.petMealEDTUnit.getEditText().setText("");
        binding.petCVAddMealType.petMealEDTType.getEditText().setText("");
        binding.petCVAddMealType.petMealEDTTime.getEditText().setText("");

        binding.petCVAddMealType.petMealEDTName.setError(null);
        binding.petCVAddMealType.petMealEDTAmount.setError(null);
        binding.petCVAddMealType.petMealEDTUnit.setError(null);
        binding.petCVAddMealType.petMealEDTType.setError(null);
        binding.petCVAddMealType.petMealEDTTime.setError(null);
    }

    private void initPetProfile() {
        petProfile = new PetProfile().addOwner(CurrentUser.getInstance().getUid());
    }

    private void cancelMealType() {
        binding.petCVAddMealType.getRoot().setVisibility(View.GONE);
        enablePetProfileFields();
        clearAddMealTypeView();
    }

    private void saveMealType() {
        if (!validateMealTypeFields())
            return;

        MealType mealType = new MealType();

        mealType.setName(binding.petCVAddMealType.petMealEDTName.getEditText().getText().toString());
        mealType.setAmount(Integer.parseInt(binding.petCVAddMealType.petMealEDTAmount.getEditText().getText().toString()));
        mealType.setUnit(binding.petCVAddMealType.petMealEDTUnit.getEditText().getText().toString());
        mealType.setFoodType(binding.petCVAddMealType.petMealEDTType.getEditText().getText().toString());
        mealType.setTimeFromString(binding.petCVAddMealType.petMealEDTTime.getEditText().getText().toString());

        if (petProfile.addMealType(mealType)) {
            Log.d("save", "saveMealType: Meal type added successfully! " + petProfile.getMealTypes());
            mealTypeAdapter.notifyDataSetChanged();
            cancelMealType();
        } else {
            Toast.makeText(this, "This meal type already exists!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateMealTypeFields() {
        if (binding.petCVAddMealType.petMealEDTName.getEditText().getText().toString().isEmpty()) {
            binding.petCVAddMealType.petMealEDTName.setError("Name is required!");
            return false;
        }
        if (binding.petCVAddMealType.petMealEDTTime.getEditText().getText().toString().isEmpty()) {
            binding.petCVAddMealType.petMealEDTTime.setError("Time is required!");
            return false;
        }
        if (binding.petCVAddMealType.petMealEDTUnit.getEditText().getText().toString().isEmpty()) {
            binding.petCVAddMealType.petMealEDTUnit.setError("Amount is required!");
            return false;
        }
        if (binding.petCVAddMealType.petMealEDTAmount.getEditText().getText().toString().isEmpty()) {
            binding.petCVAddMealType.petMealEDTAmount.setError("Unit is required!");
            return false;
        }
        if (binding.petCVAddMealType.petMealEDTType.getEditText().getText().toString().isEmpty()) {
            binding.petCVAddMealType.petMealEDTType.setError("Type is required!");
            return false;
        }
        return true;
    }

    private boolean validatePetFields() {
        if (binding.petEDTName.getEditText().getText().toString().isEmpty()) {
            binding.petEDTName.setError("Name is required!");
            return false;
        }
        if (binding.petEDTDateOfBirth.getEditText().getText().toString().isEmpty()) {
            binding.petEDTDateOfBirth.setError("Date of birth is required!");
            return false;
        }
        if (!isImageUploaded) {
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setTime() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(LocalTime.now().getHour())
                .setMinute(LocalTime.now().getMinute())
                .setTitleText("Select meal time")
                .build();

        timePicker.show(getSupportFragmentManager(), "tag");
        timePicker.addOnPositiveButtonClickListener(selection -> {
            LocalTime selectedTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
            binding.petCVAddMealType.petMealEDTTime.getEditText().setText(selectedTime.format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME)));
        });
    }

    private void addMealType() {
        if (binding.petCVAddMealType.getRoot().getVisibility() == View.VISIBLE)
            return;

        clearAddMealTypeView();
        disablePetProfileFields();
        binding.petCVAddMealType.getRoot().setVisibility(View.VISIBLE);
    }

    private void setDate() {
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.show(getSupportFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.FORMAT_DATE);
            binding.petEDTDateOfBirth.getEditText().setText(simpleDateFormat.format(new Date((long) selection)));
        });
    }

    private void updatePetProfile(String imageUrl) {
        if (!validatePetFields())
            return;

        String name = binding.petEDTName.getEditText().getText().toString();
        String gender = binding.petSPGender.getSelectedItem().toString();
        String dateOfBirth = binding.petEDTDateOfBirth.getEditText().getText().toString();

        petProfile
                .setName(name)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setProfileImage(imageUrl);

        CurrentUser.getInstance().getUserProfile().getPetsIds().add(petProfile.getId());
        CurrentPet.getInstance().setPetProfile(petProfile);
        DBCrud.getInstance().setPetInDB(petProfile);
        DBCrud.getInstance().setUserInDB(CurrentUser.getInstance().getUserProfile());
//        goToMainActivity();
        finish();
    }

    private void uploadImage() {
        setImageUploadingView(true);

        this.fileName = petProfile.getId();
        StorageReference reference = storageReference.child(this.fileName);
        reference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                        imageUrl = task.getResult().toString();
                        setImageUploaded();
                    });
                    binding.petIMGProfile.setImageURI(null);
                    setImageUploadingView(false);
                    Toast.makeText(PetProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    setImageUploadingView(false);
                    Toast.makeText(PetProfileActivity.this, "Failed Uploaded Image", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkPermissionAndUploadImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES},
                    IMAGE_UPLOAD_REQUEST_CODE);
        } else
            openImagePicker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openImagePicker();
            else
                Log.e("Permission Denied", "Storage permission denied");
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    private void setImageUploaded() {
        isImageUploaded = true;
        binding.petIMGProfile.setImageURI(imageUri);
    }

    private void setImageNotUploaded() {
        isImageUploaded = false;
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

    private void disablePetProfileFields() {
        binding.petIMGProfile.setEnabled(false);
        binding.petEDTName.setEnabled(false);
        binding.petSPGender.setEnabled(false);
        binding.petBTNAddMeal.setEnabled(false);
        binding.petBTNDateOfBirth.setEnabled(false);
        binding.petBTNAddWalk.setEnabled(false);
        binding.petBTNSave.setEnabled(false);
        binding.petLSTMeals.setEnabled(false);
        binding.petLSTMeals.setEnabled(false);
    }

    private void enablePetProfileFields() {
        binding.petIMGProfile.setEnabled(true);
        binding.petEDTName.setEnabled(true);
        binding.petSPGender.setEnabled(true);
        binding.petBTNAddMeal.setEnabled(true);
        binding.petBTNDateOfBirth.setEnabled(true);
        binding.petBTNAddWalk.setEnabled(true);
        binding.petBTNSave.setEnabled(true);
        binding.petLSTMeals.setEnabled(true);
    }

    private void setImageUploadingView(boolean isUploading) {
        if (isUploading) {
            binding.petCPIUpload.setVisibility(View.VISIBLE);
            binding.petIMGProfile.setEnabled(false);
            binding.petBTNSave.setEnabled(false);
        } else {
            binding.petCPIUpload.setVisibility(View.INVISIBLE);
            binding.petIMGProfile.setEnabled(true);
            binding.petBTNSave.setEnabled(true);
        }
    }
}