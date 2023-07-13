package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.pawsome.R;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.dal.DBCrud;
import com.example.pawsome.databinding.FragmentAddMealBinding;
import com.example.pawsome.databinding.FragmentTopBinding;
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

public class TopFragment extends Fragment {


    private FragmentTopBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initButtonsListeners();
        setUserImage();

        return root;
    }

    private void initButtonsListeners() {
        binding.topBTNLogout.setOnClickListener(v -> ((MainActivity) getActivity()).signOut());
    }

    private void setUserImage() {
        Glide
                .with(this)
                .load(CurrentPet.getInstance().getPetProfile().getProfileImage())
                .into(binding.topIMGProfile);
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