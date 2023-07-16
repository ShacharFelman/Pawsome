package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawsome.adapters.PetsCurrentAdapter;
import com.example.pawsome.callbacks.PetCurrentCallback;
import com.example.pawsome.callbacks.PetListCallback;
import com.example.pawsome.current_state.observers.UserPetsListObserver;
import com.example.pawsome.current_state.singletons.CurrentPet;
import com.example.pawsome.current_state.singletons.CurrentUser;
import com.example.pawsome.current_state.singletons.CurrentUserPetsList;
import com.example.pawsome.databinding.FragmentTopBinding;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment implements UserPetsListObserver {


    private FragmentTopBinding binding;
    private PetsCurrentAdapter petsCurrentAdapter;
    private List<PetProfile> pets = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initButtonsListeners();
        CurrentUserPetsList.getInstance().registerListener(this);
        setUserNameView();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(CurrentUser.getInstance().getUserProfile().hasPets()) {
            if (CurrentUserPetsList.getInstance().isPetsListLoaded()) {
                pets = CurrentUserPetsList.getInstance().getPets();
                binding.topTVEmpty.setVisibility(View.GONE);
                initPetListView();
            }
        }
        else
            binding.topTVEmpty.setVisibility(View.VISIBLE);
    }

    private void initButtonsListeners() {
        binding.topBTNLogout.setOnClickListener(v -> ((MainActivity) getActivity()).signOut());
    }

    private void initPetListView() {
        petsCurrentAdapter = new PetsCurrentAdapter(this, pets);
        binding.topLSTPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.topLSTPets.setAdapter(petsCurrentAdapter);
        Log.d("aaa", "onResume: pets list size: " + pets.size());
        setCurrentPetsListCallbacks();
    }

    private void setCurrentPetsListCallbacks() {
        petsCurrentAdapter.setPetCurrentCallback((pet, position) -> {
            petsCurrentAdapter.notifyDataSetChanged();
            CurrentPet.getInstance().setPetProfile(pet);
            ((MainActivity) getActivity()).selectHomeFragmentOnMenu();
        });
    }

    private void setUserNameView() {
        binding.topTVTitle.setText("Hello " + CurrentUser.getInstance().getUserProfile().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        CurrentUserPetsList.getInstance().unregisterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPetsListChanged() {
        pets = CurrentUserPetsList.getInstance().getPets();
        initPetListView();
    }

}