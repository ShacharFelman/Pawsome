package com.example.pawsome.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.pawsome.adapters.WalksAdapter;
import com.example.pawsome.callbacks.WalkCallback;
import com.example.pawsome.current_state.observers.PetMealsObserver;
import com.example.pawsome.current_state.observers.PetWalksObserver;
import com.example.pawsome.current_state.singletons.CurrentPet;
import com.example.pawsome.current_state.singletons.CurrentUser;
import com.example.pawsome.dal.DataCrud;
import com.example.pawsome.databinding.FragmentHomeBinding;
import com.example.pawsome.databinding.FragmentWalkLogBinding;
import com.example.pawsome.model.Meal;
import com.example.pawsome.model.Walk;
import com.example.pawsome.utils.DateTimeConverter;

import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements PetWalksObserver, PetMealsObserver {
    private FragmentHomeBinding binding;
    private Walk lastWalk;
    private Meal lastMeal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CurrentPet.getInstance().addWalksObserver(this);
        CurrentPet.getInstance().addMealsObserver(this);

        updateFragmentData();

        return root;
    }

    private void updateFragmentData() {
        if (isAdded()) { // checks if the fragment is currently added to its activity
            getLastWalk();
            getLastMeal();
            setLastWalkView();
            setLastMealView();
        }
    }


    private void getLastWalk() {
        if (CurrentPet.getInstance().getPetProfile() != null) {
            List<Walk> walks = CurrentPet.getInstance().getPetProfile().getWalks();
            walks.sort(Comparator.comparingLong(Walk::getDateTime).reversed());
            if (!walks.isEmpty())
                lastWalk = walks.get(0);
        }
    }

    private void getLastMeal() {
        if (CurrentPet.getInstance().getPetProfile() != null) {
            List<Meal> meals = CurrentPet.getInstance().getPetProfile().getMeals();
            meals.sort(Comparator.comparingLong(Meal::getDateTime).reversed());
            if (!meals.isEmpty())
                lastMeal = meals.get(0);
        }
    }

    private void setLastWalkView() {
        if(lastWalk == null) {
            setNoWalksView();
            return;
        }

        binding.homeTVWalk.setText("Last Walk");
        binding.homeITMWalk.getRoot().setVisibility(View.VISIBLE);

        binding.homeITMWalk.walkBTNDelete.setVisibility(View.GONE);
        binding.homeITMWalk.walkTVUser.setText(lastWalk.getOwner().getName());
        binding.homeITMWalk.walkTVTime.setText(DateTimeConverter.longToStringTime(lastWalk.getDateTime()));
        binding.homeITMWalk.walkTVDate.setText(DateTimeConverter.longToStringDate(lastWalk.getDateTime()));
        binding.homeITMWalk.walkTVNote.setText(lastWalk.getNote());
        binding.homeITMWalk.walkTVTypeDuration.setText(lastWalk.getName() + ", " + lastWalk.getDurationInMinutes() + " min");
        binding.homeITMWalk.walkIMGPoop.setVisibility(!lastWalk.isPoop() ? View.VISIBLE : View.GONE);
        Glide.
                with(getContext()).
                load(lastWalk.getOwner().getProfileImage()).
                into(binding.homeITMWalk.walkIMGUser);

        if(lastWalk.getOwner().getUid().equals(CurrentUser.getInstance().getUid()))
            binding.homeITMWalk.walkCVItem.setStrokeWidth(5);
        else
            binding.homeITMWalk.walkCVItem.setStrokeWidth(0);

    }

    private void setLastMealView() {
        if(lastMeal == null) {
            setNoMealsView();
            return;
        }

        binding.homeTVMeal.setText("Last Meal");
        binding.homeITMMeal.getRoot().setVisibility(View.VISIBLE);

        binding.homeITMMeal.mealBTNDelete.setVisibility(View.GONE);
        binding.homeITMMeal.mealTVUser.setText(lastMeal.getOwner().getName());
        binding.homeITMMeal.mealTVTime.setText(DateTimeConverter.longToStringTime(lastMeal.getDateTime()));
        binding.homeITMMeal.mealTVDate.setText(DateTimeConverter.longToStringDate(lastMeal.getDateTime()));
        binding.homeITMMeal.mealTVNote.setText(lastMeal.getNote());
        binding.homeITMMeal.mealTVType.setText(lastMeal.getName());

        Glide.
                with(getContext()).
                load(lastMeal.getOwner().getProfileImage()).
                into(binding.homeITMMeal.mealIMGUser);

        if(lastMeal.getOwner().getUid().equals(CurrentUser.getInstance().getUid()))
            binding.homeITMMeal.mealCVItem.setStrokeWidth(5);
        else
            binding.homeITMMeal.mealCVItem.setStrokeWidth(0);
    }

    private void setNoWalksView() {
        binding.homeTVWalk.setText("No walks yet");
        binding.homeITMWalk.getRoot().setVisibility(View.INVISIBLE);
    }


    private void setNoMealsView() {
        binding.homeTVMeal.setText("No meals yet");
        binding.homeITMMeal.getRoot().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CurrentPet.getInstance().removeWalksObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onWalksListChanged() {
        updateFragmentData();
    }

    @Override
    public void onMealsListChanged() {
        updateFragmentData();
    }
}