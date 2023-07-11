package com.example.pawsome.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pawsome.dal.MealCrud;
import com.example.pawsome.model.Meal;
import com.example.pawsome.view.activity.adam.ChatActivity;
import com.example.pawsome.Callbacks.MealCallback;
import com.example.pawsome.adapters.MealsAdapter;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.model.adam_delete.Group;

import java.util.HashMap;

public class MyGroupsFragment extends Fragment {

    private FragmentMealsBinding binding;

    private MealsAdapter mealsAdapter;

    private MealCrud mealsCrud;

    private Observer<HashMap<String, Meal>> observer = new Observer<HashMap<String, Meal>>() {
        @Override
        public void onChanged(HashMap<String, Meal> meals) {
            mealsAdapter.updateMeals(meals);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMealsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews();
        setCallbacks();
        initListeners();
        return root;
    }



    private void initViews() {
        mealsCrud = new MealCrud();
        mealsCrud.getMealsFromDB().observe(getViewLifecycleOwner(), observer);

        mealsAdapter = new MealsAdapter(this);
        binding.myGroupsGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.myGroupsGroups.setAdapter(mealsAdapter);

    }

    private void setCallbacks() {
        mealsAdapter.setMealCallback(new MealCallback() {
            @Override
            public void itemClicked(Meal meal, int position) {
                /*loadChatActivity(group)*/;
            }

            @Override
            public void deleteClicked(Meal meal, int position) {
                mealsAdapter.deleteMeal(meal.getId());
                mealsAdapter.notifyItemRemoved(position);
                mealsAdapter.notifyItemChanged(position);
                if(meal.getUsersID().isEmpty()){

                    mealsCrud.removeGroupFromDB(group.getId());
                }
                else{
                    mealsCrud.updateGroupDB(group);
                }
                mealsCrud.updateUserDB();
            }
        });
    }

    private void initListeners() {
        binding.myGroupsETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mealsAdapter.filter(false,editable.toString());
            }
        });
    }

    private void loadChatActivity(Group group) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_CHAT, group.getChatMessages());
        intent.putExtra(Constants.KEY_GROUP_NAME, group.getName());
        intent.putExtra(Constants.KEY_GROUP_ID,group.getId());
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.myGroupsETSearch.setText(null);
    }
}