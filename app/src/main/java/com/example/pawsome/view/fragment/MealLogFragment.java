package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawsome.adapters.MealsAdapter;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.PetProfileObserver;
import com.example.pawsome.databinding.FragmentMealLogBinding;
import com.example.pawsome.model.Meal;

import java.util.Comparator;
import java.util.List;

public class MealLogFragment extends Fragment implements PetProfileObserver {
    private FragmentMealLogBinding binding;
    private MealsAdapter mealsAdapter;
    private List<Meal> meals;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CurrentPet.getInstance().addObserver(this);

        if(CurrentPet.getInstance().getPetProfile() != null)
            updateFragmentData();

        return root;
    }

    private void updateFragmentData() {
        getMealsList();
        setMealsListView();
        initListeners();
    }

    private void initListeners() {
    }

    private void getMealsList() {
        if (CurrentPet.getInstance().getPetProfile() != null) {
            this.meals = CurrentPet.getInstance().getPetProfile().getMeals();
            this.meals.sort(Comparator.comparingLong(Meal::getDateTime).reversed());
        }
    }

    private void setMealsListView() {
        if (meals == null || meals.isEmpty()) {
            binding.mealsLogLSTMeals.setVisibility(View.GONE);
            binding.mealsLogTVEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.mealsLogLSTMeals.setVisibility(View.VISIBLE);
            binding.mealsLogTVEmpty.setVisibility(View.GONE);
        }

        mealsAdapter = new MealsAdapter(this, meals);
        binding.mealsLogLSTMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mealsLogLSTMeals.setAdapter(mealsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        CurrentPet.getInstance().removeObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPetProfileChanged() {
        updateFragmentData();
    }
}