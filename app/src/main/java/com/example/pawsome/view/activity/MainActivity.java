package com.example.pawsome.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import com.example.pawsome.R;
import com.example.pawsome.view.fragment.AddMealFragment;
import com.example.pawsome.view.fragment.AddWalkFragment;
import com.example.pawsome.view.fragment.MealLogFragment;
import com.example.pawsome.view.fragment.HomeFragment;
import com.example.pawsome.view.fragment.SettingsFragment;
import com.example.pawsome.view.fragment.TopFragment;
import com.example.pawsome.view.fragment.WalkLogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static BottomNavigationView main_BNV_menu;
    private static int mainFragmentLocation = R.id.main_FRAME_fragments;
    private static int topFragmentLocation = R.id.main_FRAME_top;

    private static int menu_home = R.id.menu_FRG_home;
    private static int menu_meals = R.id.menu_FRG_meals;
    private static int menu_add = R.id.menu_FRG_add;
    private static int menu_walks = R.id.menu_FRG_walks;
    private static int menu_settings = R.id.menu_FRG_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setTopFragment();
        setBottomNaviMenuListener();
        main_BNV_menu.setSelectedItemId(R.id.menu_FRG_home);
    }

    private void initViews() {
        main_BNV_menu = findViewById(R.id.main_BNV_menu);
    }

    private void setBottomNaviMenuListener() {
        main_BNV_menu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == menu_home) {
                replaceFragment(HomeFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == menu_meals) {
                replaceFragment(MealLogFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == menu_add) {
                setAddDialog();
            } else if (item.getItemId() == R.id.menu_FRG_walks) {
                replaceFragment(WalkLogFragment.class, mainFragmentLocation);
            } else if (item.getItemId() == R.id.menu_FRG_settings) {
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

    public void replaceFragment(Class fragmentClass, int fragmentLocation/*, String key, String value*/) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Bundle bundle = new Bundle();
//        bundle.putString(key, value);
//        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentLocation, fragment)
                .commit();
    }

    private void setAddDialog() {
        // instance of alert dialog to build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // set the neutral button to do some actions
        builder.setNeutralButton("Meal", (dialog, which) -> replaceFragment(AddMealFragment.class, mainFragmentLocation));
        builder.setNegativeButton("Walk", (dialog, which) -> replaceFragment(AddWalkFragment.class, mainFragmentLocation));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}