package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.dal.DataCrud;
import com.example.pawsome.databinding.FragmentAddMealBinding;
import com.example.pawsome.model.Meal;
import com.example.pawsome.model.MealType;
import com.example.pawsome.model.UserProfile;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.view.activity.MainActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddMealFragment extends Fragment {

    private FragmentAddMealBinding binding;
    private List<MealType> mealTypes;
    private MealType selectedMealType;
    private List<UserProfile> owners;
    private UserProfile selectedOwner;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddMealBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (CurrentPet.getInstance().getPetProfile() != null)
            updateFragmentData();

        return root;
    }

    private void updateFragmentData() {
        getMealsTypesList();
        initMealTypesSpinner();
        setMealTypeDataInView();

        getOwnersList();

        initListeners();
    }

    private void initListeners() {
        binding.addMealACTVMealType.setOnItemClickListener((parent, view, position, id) -> {
            this.selectedMealType = (MealType) parent.getItemAtPosition(position);
        });

        binding.addMealACTVOwner.setOnItemClickListener((parent, view, position, id) -> {
            this.selectedOwner = (UserProfile) parent.getItemAtPosition(position);
        });

        binding.addMealBTNSave.setOnClickListener(v -> {
            saveMeal();
        });

        binding.addMealBTNTime.setOnClickListener(v -> setTime());
        binding.addMealBTNDate.setOnClickListener(v -> setDate());
    }

    private void initMealTypesSpinner() {
        if (this.mealTypes == null || this.mealTypes.isEmpty())
            binding.addMealTILMealType.setVisibility(View.GONE);
        else {
            ArrayAdapter<MealType> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, mealTypes);
            binding.addMealACTVMealType.setAdapter(adapter);
        }
    }

    private void initOwnersSpinner() {
        if (this.owners == null || this.owners.isEmpty())
            binding.addMealTILMealType.setVisibility(View.INVISIBLE);
        else {
            ArrayAdapter<UserProfile> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, owners);
            binding.addMealACTVOwner.setAdapter(adapter);
            binding.addMealTILOwner.setVisibility(View.VISIBLE);
        }
    }

    private void setTime() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(LocalTime.now().getHour())
                .setMinute(LocalTime.now().getMinute())
                .setTitleText("Select meal time")
                .build();

        timePicker.show(getParentFragmentManager(), "tag");
        timePicker.addOnPositiveButtonClickListener(selection -> {
            LocalTime selectedTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
            binding.addMealEDTTime.getEditText().setText(selectedTime.format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME)));
        });
    }

    private void setDate(){
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.show(getParentFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.FORMAT_DATE);
            binding.addMealEDTDate.getEditText().setText(simpleDateFormat.format(new Date((long)selection)));
        });
    }

//    private MealType getMealTypeSelected() {
//        String mealTypeName = binding.addMealACTVMealType.getText().toString();
//        for (MealType mealType : mealTypes) {
//            if (mealType.getName().equals(mealTypeName))
//                return mealType;
//        }
//        return null;
//    }

    private void setMealTypeDataInView() {
        if (this.selectedMealType != null) {
            if (selectedMealType.getName() != null && !selectedMealType.getName().isEmpty())
                binding.addMealEDTName.getEditText().setText(selectedMealType.getName());

            if (selectedMealType.getAmount() != 0)
                binding.addMealEDTAmount.getEditText().setText(String.valueOf(selectedMealType.getAmount()));

            if (selectedMealType.getUnit() != null && !selectedMealType.getUnit().isEmpty())
                binding.addMealEDTUnit.getEditText().setText(selectedMealType.getUnit());

            if (selectedMealType.getFoodType() != null && !selectedMealType.getFoodType().isEmpty())
                binding.addMealEDTFoodType.getEditText().setText(selectedMealType.getFoodType());
        }

        binding.addMealEDTTime.getEditText().setText(LocalTime.now().format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME)));
        binding.addMealEDTDate.getEditText().setText(LocalDate.now().format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE)));
    }

    private void getMealsTypesList() {
        if (CurrentPet.getInstance().getPetProfile() != null)
            this.mealTypes = CurrentPet.getInstance().getPetProfile().getMealTypes();
    }

    private void getOwnersList() {
        binding.addMealTILOwner.setVisibility(View.INVISIBLE);

        if (CurrentPet.getInstance().getPetProfile() != null) {
            if (this.owners == null)
                this.owners = new ArrayList<>();

            for (String userId : CurrentPet.getInstance().getPetProfile().getOwnersIds()) {
                DataCrud.getInstance().getUserReference(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserProfile userProfile = snapshot.getValue(UserProfile.class);
                            owners.add(userProfile);
                            if (owners.size() == CurrentPet.getInstance().getPetProfile().getOwnersIds().size()) {
                                initOwnersSpinner();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }

    private void saveMeal() {
        if (CurrentPet.getInstance().getPetProfile() != null) {
            Meal meal = new Meal();

            String date = binding.addMealEDTDate.getEditText().getText().toString();
            String time = binding.addMealEDTTime.getEditText().getText().toString();
            LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            meal
//                    .setPetId(CurrentPet.getInstance().getPetProfile().getId())
                    .setName(binding.addMealEDTName.getEditText().getText().toString())
                    .setAmount(Integer.parseInt(binding.addMealEDTAmount.getEditText().getText().toString()))
                    .setUnit(binding.addMealEDTUnit.getEditText().getText().toString())
//                    .setNote(binding.addMealEDTNote.getEditText().getText().toString())
                    .setFoodType(binding.addMealEDTFoodType.getEditText().getText().toString())
                    .setDateTimeAsLocalDateTime(dateTime);


            CurrentPet.getInstance().getPetProfile().addMeal(meal);
            DataCrud.getInstance().setPetInDB(CurrentPet.getInstance().getPetProfile());

            ((MainActivity) getActivity()).replaceToHomeFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
