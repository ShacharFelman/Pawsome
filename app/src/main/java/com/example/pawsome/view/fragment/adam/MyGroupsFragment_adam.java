package com.example.pawsome.view.fragment.adam;

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

import com.example.pawsome.Callbacks.MealCallback;
import com.example.pawsome.adapters.MealsAdapter;
import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.dal.adam.MyGroupsViewModel;
import com.example.pawsome.model.adam_delete.Group;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.view.activity.adam.ChatActivity;

import java.util.HashMap;

public class MyGroupsFragment_adam extends Fragment {

    private FragmentMealsBinding binding;

    private MealsAdapter mealsAdapter;

    private MyGroupsViewModel myGroupsViewModel;

    private Observer<HashMap<String, Group>> observer = new Observer<HashMap<String, Group>>() {
        @Override
        public void onChanged(HashMap<String, Group> groups) {
            mealsAdapter.updateMeals(groups);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        MyGroupsViewModel myGroupsViewModel =
//                new ViewModelProvider(this).get(MyGroupsViewModel.class);

        binding = FragmentMealsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews();
        setCallbacks();
        initListeners();
        return root;
    }



    private void initViews() {
//        myGroupsViewModel = new MyGroupsViewModel();
        myGroupsViewModel.getGroups().observe(getViewLifecycleOwner(), observer);

        mealsAdapter = new MealsAdapter(this);
        binding.myGroupsGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.myGroupsGroups.setAdapter(mealsAdapter);

    }

    private void setCallbacks() {
        mealsAdapter.setMealCallback(new MealCallback() {
            @Override
            public void joinClicked(Group group, int position) {
            }

            @Override
            public void itemClicked(Group group, int position) {
                loadChatActivity(group);
            }

            @Override
            public void deleteClicked(Group group, int position) {
                group.removeUser(CurrentUser.getInstance().getUid());
//                CurrentUser.getInstance().getGroups().remove(group.getId());
                mealsAdapter.deleteMeal(group.getId());
                mealsAdapter.notifyItemRemoved(position);
                mealsAdapter.notifyItemChanged(position);
                if(group.getUsersID().isEmpty()){

                    myGroupsViewModel.removeGroupFromDB(group.getId());
                }
                else{
                    myGroupsViewModel.updateGroupDB(group);
                }
                myGroupsViewModel.updateUserDB();
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