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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsome.R;
import com.example.pawsome.adapters.MealTypeAdapter;
import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.dal.FirebaseDB;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.model.MealType;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PetProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private MaterialButton pet_BTN_upload;
    private MaterialButton pet_BTN_save;
    private MaterialButton pet_BTN_date_of_birth;
    private MaterialButton pet_BTN_add_meal;
    private MaterialButton pet_BTN_add_walk;
    private MaterialButton pet_meal_BTN_save;
    private MaterialButton pet_meal_BTN_cancel;
    private MaterialButton pet_meal_BTN_time;
    private ImageView pet_IMG_profile;
    private TextInputLayout pet_EDT_name;
    private TextInputLayout pet_EDT_date_of_birth;
    private TextInputLayout pet_meal_EDT_name;
    private TextInputLayout pet_meal_EDT_amount;
    private TextInputLayout pet_meal_EDT_unit;
    private TextInputLayout pet_meal_EDT_type;
    private TextInputLayout pet_meal_EDT_time;
    private AppCompatSpinner pet_SP_gender;
    private RecyclerView mealsLogLSTMeals;
    private MaterialCardView pet_CV_add_meal_type;
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
        setContentView(R.layout.activity_pet_profile);

        storageReference = FirebaseDB.getInstance().getStorageReference(Constants.DB_PETS_PROFILE_IMAGES);

        initPetProfile();

        initPetView();
        initAddMealTypeView();

        initImagePickerLauncher();
        initRecyclerView();

        setImageUploaded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null || CurrentUser.getInstance().getUserProfile() == null)
            goToLoginActivity();
    }

    private void initPetView() {
        initPetFindViewById();
        setPetButtonsListener();
        setPetTextChangedListener();
    }
    private void initAddMealTypeView() {
        initAddMealTypeFindViewById();
        setAddMealTypeButtonsListener();
        setAddMealTypeTextChangedListener();
        cancelMealType();
    }

    private void initPetFindViewById() {
        pet_IMG_profile = findViewById(R.id.pet_IMG_profile);
        pet_BTN_upload = findViewById(R.id.pet_BTN_upload);
        pet_BTN_save = findViewById(R.id.pet_BTN_save);
        pet_BTN_date_of_birth = findViewById(R.id.pet_BTN_date_of_birth);
        pet_BTN_add_meal = findViewById(R.id.pet_BTN_add_meal);
        pet_BTN_add_walk = findViewById(R.id.pet_BTN_add_walk);
        pet_EDT_name = findViewById(R.id.pet_EDT_name);
        pet_EDT_date_of_birth = findViewById(R.id.pet_EDT_date_of_birth);
        pet_SP_gender = findViewById(R.id.pet_SP_gender);
        mealsLogLSTMeals = findViewById(R.id.meals_log_LST_meals);
        pet_CV_add_meal_type = findViewById(R.id.pet_CV_add_meal_type);

    }

    private void initAddMealTypeFindViewById() {
        pet_meal_BTN_save = findViewById(R.id.pet_meal_BTN_save);
        pet_meal_BTN_cancel = findViewById(R.id.pet_meal_BTN_cancel);
        pet_meal_BTN_time = findViewById(R.id.pet_meal_BTN_time);
        pet_meal_EDT_name = findViewById(R.id.pet_meal_EDT_name);
        pet_meal_EDT_amount = findViewById(R.id.pet_meal_EDT_amount);
        pet_meal_EDT_unit = findViewById(R.id.pet_meal_EDT_unit);
        pet_meal_EDT_type = findViewById(R.id.pet_meal_EDT_type);
        pet_meal_EDT_time = findViewById(R.id.pet_meal_EDT_time);
    }

    private void setPetButtonsListener() {
        pet_IMG_profile.setOnClickListener(v -> checkPermissionAndUploadImage());
        pet_BTN_upload.setOnClickListener(v -> uploadImage());
        pet_BTN_save.setOnClickListener(v -> updatePetProfile(imageUrl));
        pet_BTN_date_of_birth.setOnClickListener(v -> setDate());
        pet_BTN_add_meal.setOnClickListener(v -> addMealType());
    }

    private void setAddMealTypeButtonsListener () {
        pet_meal_BTN_save.setOnClickListener(v -> saveMealType());
        pet_meal_BTN_cancel.setOnClickListener(v -> cancelMealType());
        pet_meal_BTN_time.setOnClickListener(v -> setTime());
    }

    private void setAddMealTypeTextChangedListener() {
        pet_meal_EDT_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                if(!pet_meal_EDT_name.getEditText().getText().toString().isEmpty())
                    pet_meal_EDT_name.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        pet_meal_EDT_amount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence amount, int start, int before, int count) {
                if(!pet_meal_EDT_amount.getEditText().getText().toString().isEmpty())
                    pet_meal_EDT_amount.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        pet_meal_EDT_unit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence unit, int start, int before, int count) {
                if(!pet_meal_EDT_unit.getEditText().getText().toString().isEmpty())
                    pet_meal_EDT_unit.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        pet_meal_EDT_type.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence type, int start, int before, int count) {
                if(!pet_meal_EDT_type.getEditText().getText().toString().isEmpty())
                    pet_meal_EDT_type.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        pet_meal_EDT_time.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence time, int start, int before, int count) {
                if(!pet_meal_EDT_time.getEditText().getText().toString().isEmpty())
                    pet_meal_EDT_time.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });


    }

    private void setPetTextChangedListener() {
        pet_EDT_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                if(!pet_EDT_name.getEditText().getText().toString().isEmpty())
                    pet_EDT_name.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        pet_EDT_date_of_birth.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence amount, int start, int before, int count) {
                if(!pet_EDT_date_of_birth.getEditText().getText().toString().isEmpty())
                    pet_EDT_date_of_birth.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void initRecyclerView() {
        mealTypeAdapter = new MealTypeAdapter(this, petProfile.getMealTypes());
        mealsLogLSTMeals.setLayoutManager(new LinearLayoutManager(this));
        mealsLogLSTMeals.setAdapter(mealTypeAdapter);
    }

    private void initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        setImageNotUploaded();
                        imageUri = result.getData().getData();
                        pet_IMG_profile.setImageURI(imageUri);
                    }
                }
        );
    }

    private void clearAddMealTypeView() {
        pet_meal_EDT_name.getEditText().setText("");
        pet_meal_EDT_amount.getEditText().setText("");
        pet_meal_EDT_unit.getEditText().setText("");
        pet_meal_EDT_type.getEditText().setText("");
        pet_meal_EDT_time.getEditText().setText("");

        pet_meal_EDT_name.setError(null);
        pet_meal_EDT_amount.setError(null);
        pet_meal_EDT_unit.setError(null);
        pet_meal_EDT_type.setError(null);
        pet_meal_EDT_time.setError(null);

    }

    private void initPetProfile() {
        petProfile = new PetProfile().addOwner(CurrentUser.getInstance().getUid());
    }

    private void cancelMealType() {
        pet_CV_add_meal_type.setVisibility(View.GONE);
        clearAddMealTypeView();
    }

    private void saveMealType() {
        if(!validateMealTypeFields())
            return;

        MealType mealType = new MealType();
        mealType.setName(pet_meal_EDT_name.getEditText().getText().toString());
        mealType.setAmount(Integer.parseInt(pet_meal_EDT_amount.getEditText().getText().toString()));
        mealType.setUnit(pet_meal_EDT_unit.getEditText().getText().toString());
        mealType.setFoodType(pet_meal_EDT_type.getEditText().getText().toString());
        mealType.setTimeFromString(pet_meal_EDT_time.getEditText().getText().toString());

        petProfile.getMealTypes().add(mealType);
        mealTypeAdapter.notifyDataSetChanged();
        cancelMealType();
    }

    private boolean validateMealTypeFields() {
        if(pet_meal_EDT_name.getEditText().getText().toString().isEmpty()) {
            pet_meal_EDT_name.setError("Name is required!");
            return false;
        }
        if(pet_meal_EDT_time.getEditText().getText().toString().isEmpty()) {
            pet_meal_EDT_time.setError("Time is required!");
            return false;
        }
        if(pet_meal_EDT_amount.getEditText().getText().toString().isEmpty()) {
            pet_meal_EDT_amount.setError("Amount is required!");
            return false;
        }
        if(pet_meal_EDT_unit.getEditText().getText().toString().isEmpty()) {
            pet_meal_EDT_unit.setError("Unit is required!");
            return false;
        }
        if(pet_meal_EDT_type.getEditText().getText().toString().isEmpty()) {
            pet_meal_EDT_type.setError("Type is required!");
            return false;
        }

        return true;
    }

    private boolean validatePetFields() {
        if(pet_EDT_name.getEditText().getText().toString().isEmpty()) {
            pet_EDT_name.setError("Name is required!");
            return false;
        }
        if (pet_EDT_date_of_birth.getEditText().getText().toString().isEmpty()) {
            pet_EDT_date_of_birth.setError("Date of birth is required!");
            return false;
        }
        if(!isImageUploaded) {
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
            pet_meal_EDT_time.getEditText().setText(selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        });
    }
    private void addMealType() {
        if(pet_CV_add_meal_type.getVisibility() == View.VISIBLE)
            return;

        clearAddMealTypeView();
        pet_CV_add_meal_type.setVisibility(View.VISIBLE);
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
        if (!validatePetFields())
            return;

            String name = pet_EDT_name.getEditText().getText().toString();
            String gender = pet_SP_gender.getSelectedItem().toString();
            String dateOfBirth = pet_EDT_date_of_birth.getEditText().getText().toString();

            petProfile
                    .setName(name)
                    .setGender(gender)
                    .setDateOfBirth(dateOfBirth)
                    .setProfileImage(imageUrl);

            CurrentUser.getInstance().getUserProfile().getPetsIds().add(petProfile.getId());
            CurrentPet.getInstance().setPetProfile(petProfile);
            DBCrud.getInstance().setPetInDB(petProfile);
            DBCrud.getInstance().setUserInDB(CurrentUser.getInstance().getUserProfile());
            goToMainActivity();
            finish();
    }
    private void uploadImage() {
        // TODO: start show progress bar (loading)
        this.fileName = petProfile.getId();
        StorageReference reference = storageReference.child(this.fileName);
        reference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                        imageUrl = task.getResult().toString();
                        setImageUploaded();
                    });
                    pet_IMG_profile.setImageURI(null);
                    // TODO: stop show progress bar
                    Toast.makeText(PetProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // TODO: stop show progress bar
                    Toast.makeText(PetProfileActivity.this, "Failed Uploaded Image", Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE);

//    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData() != null) {
//            // Get the URI of the selected image
//            setImageNotUploaded();
//            imageUri = data.getData();
//            pet_IMG_profile.setImageURI(imageUri);
//        }

//    }

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

}