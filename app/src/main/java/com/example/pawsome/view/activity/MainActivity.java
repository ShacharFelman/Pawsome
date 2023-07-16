package com.example.pawsome.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.example.pawsome.R;
import com.example.pawsome.current_state.observers.UserPetsListObserver;
import com.example.pawsome.current_state.singletons.CurrentPet;
import com.example.pawsome.current_state.singletons.CurrentUser;
import com.example.pawsome.dal.DataCrud;
import com.example.pawsome.databinding.ActivityMainBinding;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.view.fragment.AddMealFragment;
import com.example.pawsome.view.fragment.AddWalkFragment;
import com.example.pawsome.view.fragment.MealLogFragment;
import com.example.pawsome.view.fragment.HomeFragment;
import com.example.pawsome.view.fragment.SettingsFragment;
import com.example.pawsome.view.fragment.TopFragment;
import com.example.pawsome.view.fragment.WalkLogFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements UserPetsListObserver {

    private static final int mainFragmentLocation = R.id.main_FRAME_fragments;
    private static final int topFragmentLocation = R.id.main_FRAME_top;
    private static final int menu_home = R.id.menu_FRG_home;
    private static final int menu_meals = R.id.menu_FRG_meals;
    private static final int menu_add = R.id.menu_FRG_add;
    private static final int menu_walks = R.id.menu_FRG_walks;
    private static final int menu_settings = R.id.menu_FRG_settings;

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBottomNaviMenuListener();
        binding.mainBNVMenu.setSelectedItemId(R.id.menu_FRG_home);

        if(!CurrentUser.getInstance().getUserProfile().hasPets()) {
            setNoPets();
        }
        else {
            loadPetProfile(CurrentUser.getInstance().getUserProfile().getPetsIds().get(0));
        }

    }

    private void setBottomNaviMenuListener() {
        binding.mainBNVMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == menu_home) {
                replaceFragment(HomeFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == menu_meals) {
                replaceFragment(MealLogFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == menu_add) {
                setAddDialog();
            } else if (item.getItemId() == menu_walks) {
                replaceFragment(WalkLogFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == menu_settings) {
                replaceFragment(SettingsFragment.class, mainFragmentLocation);
            } else {
                replaceFragment(HomeFragment.class, mainFragmentLocation);
            }
            return true;
        });
    }

    private void setTopFragment() {
        replaceFragment(TopFragment.class, topFragmentLocation);
    }

    public void replaceFragment(Class fragmentClass, int fragmentLocation) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentLocation, fragment)
                .commit();
    }

    public void selectHomeFragmentOnMenu() {
        binding.mainBNVMenu.setSelectedItemId(menu_home);
    }
    public void selectMealLogFragmentOnMenu() {
        binding.mainBNVMenu.setSelectedItemId(menu_meals);
    }
    public void selectWalkLogFragmentOnMenu() {
        binding.mainBNVMenu.setSelectedItemId(menu_walks);
    }
    public void selectSettingsFragmentOnMenu() {
        binding.mainBNVMenu.setSelectedItemId(menu_settings);
    }

    private void setAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setNeutralButton("Meal", (dialog, which) -> replaceFragment(AddMealFragment.class, mainFragmentLocation));
        builder.setNegativeButton("Walk", (dialog, which) -> replaceFragment(AddWalkFragment.class, mainFragmentLocation));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setNoPets() {
        binding.mainBNVMenu.setEnabled(false);
    }

    public void goToProfileActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KEY_NEW_USER, false);
        bundle.putBoolean(Constants.KEY_FROM_MAIN, true);
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToPetProfileActivity(String petId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_PET_ID, petId);
        bundle.putBoolean(Constants.KEY_NEW_PET, petId == null || petId.isEmpty());
        bundle.putBoolean(Constants.KEY_FROM_MAIN, true);

        Intent intent = new Intent(this, PetProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToPetProfileActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KEY_NEW_PET, true);
        bundle.putBoolean(Constants.KEY_FROM_MAIN, true);

        Intent intent = new Intent(this, PetProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadPetProfile(String petId) {
        binding.mainBNVMenu.setEnabled(false);
        DataCrud.getInstance().getPetReference(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CurrentPet.getInstance().setPetProfile(snapshot.getValue(PetProfile.class));
                    binding.mainBNVMenu.setEnabled(true);
                    setTopFragment();
                }
                else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onPetsListChanged() {
        if(!CurrentUser.getInstance().getUserProfile().hasPets()) {
            setNoPets();
        }
        else {
            loadPetProfile(CurrentUser.getInstance().getUserProfile().getPetsIds().get(0));
        }
    }
}