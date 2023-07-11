package com.example.pawsome.view.fragment.adam;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pawsome.Callbacks.MealCallback;
import com.example.pawsome.adapters.MealsAdapter;
import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.dal.adam.HomeViewModel;
import com.example.pawsome.databinding.FragmentHomeBinding;
import com.example.pawsome.model.adam_delete.Group;
import com.example.pawsome.utils.Signal;
import com.example.pawsome.view.activity.MainActivity;

import java.util.HashMap;

public class HomeFragment_adam extends Fragment {

    private FragmentHomeBinding binding;
    private MealsAdapter mealsAdapter;
    private HomeViewModel homeViewModel;
    private Observer<HashMap<String, Group>> observer = new Observer<HashMap<String, Group>>() {

        @Override
        public void onChanged(HashMap<String, Group> groups) {
            mealsAdapter.updateMeals(groups);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();
        setCallbacks();
        initListeners();
        return root;
    }

    private void initListeners() {
        binding.homeETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mealsAdapter.filter(binding.homeCBFavGames.isChecked(),binding.homeETSearch.getText().toString());

            }
        });

        binding.homeLogout.setOnClickListener(view -> ((MainActivity)getContext()).signOut());

        binding.homeCBFavGames.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mealsAdapter.filter(compoundButton.isChecked(),binding.homeETSearch.getText().toString());
            }
        });
    }




    private void initViews() {
        binding.homeTVHello.setText("Hello, " + CurrentUser.getInstance().getUserProfile().getName());
        homeViewModel = new HomeViewModel();
        homeViewModel.getMGroups().observe(getViewLifecycleOwner(), observer);

        mealsAdapter = new MealsAdapter(this);
        binding.homeLSTGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.homeLSTGroups.setAdapter(mealsAdapter);
    }

    private void setCallbacks() {
        mealsAdapter.setMealCallback(new MealCallback() {
            @Override
            public void joinClicked(Group group, int position) {
                if(group.getCapacity() <= group.getNumOfUsers()){
                    Signal.getInstance().toast("The group is full");
                }
                else{
//                    CurrentUser.getInstance().getGroups().put(group.getId(), group);
                    group.addUser(CurrentUser.getInstance().getUid());
                    mealsAdapter.deleteMeal(group.getId());
                    mealsAdapter.notifyItemRemoved(position);
                    homeViewModel.updateJoinedGroupDB(group);
                }
            }

            @Override
            public void itemClicked(Group group, int position) {
            }

            @Override
            public void deleteClicked(Group item, int position) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.homeETSearch.setText(null);
        binding.homeCBFavGames.setChecked(false);
    }
}