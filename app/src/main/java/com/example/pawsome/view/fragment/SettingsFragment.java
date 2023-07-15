package com.example.pawsome.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pawsome.adapters.PetsAdapter;
import com.example.pawsome.callbacks.PetCallback;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.dal.DataCrud;
import com.example.pawsome.databinding.FragmentSettingsBinding;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.view.activity.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private PetsAdapter petsAdapter;
    private List<PetProfile> pets = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initButtonsListeners();
        setUserImageView();
        setUserNameView();
        getPetsData();

        return root;
    }

    private void initButtonsListeners() {
        binding.settingsBTNProfile.setOnClickListener(v -> goToProfileActivity());
        binding.settingsBTNAddPet.setOnClickListener(v -> goToPetProfileActivity());
    }

    private void setUserImageView() {
        Glide
                .with(this)
                .load(CurrentUser.getInstance().getUserProfile().getProfileImage())
                .into(binding.profileIMGProfile);
    }

    private void setUserNameView() {
        binding.settingsLBLName.setText(CurrentUser.getInstance().getUserProfile().getName());
    }

    private void setPetsListCallbacks() {
        petsAdapter.setPetCallback(new PetCallback() {
            @Override
            public void deleteClicked(PetProfile pet, int position) {
                deletePetPressed(pet);
            }

            @Override
            public void itemClicked(PetProfile pet, int position) {
                goToPetProfileActivity(pet.getId());
            }
        });
    }

    private void getPetsData() {
        if (CurrentUser.getInstance().getUserProfile().hasPets()) {
            binding.settingsCPIPetsLoading.setVisibility(View.VISIBLE);
            for (String petId : CurrentUser.getInstance().getUserProfile().getPetsIds()) {
                DataCrud.getInstance().getPetReference(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            pets.add(snapshot.getValue(PetProfile.class));
                            if (pets.size() == CurrentUser.getInstance().getUserProfile().getPetsIds().size()) {
                                setPetsListView();
                                binding.settingsCPIPetsLoading.setVisibility(View.INVISIBLE);
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



    private void deletePetPressed(PetProfile pet) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        alertDialogBuilder.setTitle("Delete Pet");
        alertDialogBuilder.setMessage("Delete " + pet.getName() + " from your pets list?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_delete);
        alertDialogBuilder.setPositiveButton("Delete", (dialog, which) -> deletePetFromUser(pet));
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }


    private void setPetsListView() {
        petsAdapter = new PetsAdapter(this, pets);
        binding.settingsLSTPets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.settingsLSTPets.setAdapter(petsAdapter);
        setPetsListCallbacks();
    }

    private void deletePetFromUser(PetProfile pet) {
        if (pet.isOnlyOneOwner())
            DataCrud.getInstance().deletePetFromDB(pet.getId());

        CurrentUser.getInstance().getUserProfile().deletePet(pet.getId());
        CurrentUser.getInstance().saveCurrentUserToDB();
    }

    private void goToProfileActivity() {
        ((MainActivity) getActivity()).goToProfileActivity();
    }

    private void goToPetProfileActivity() {
        ((MainActivity) getActivity()).goToPetProfileActivity();
    }

    private void goToPetProfileActivity(String petId) {
        ((MainActivity) getActivity()).goToPetProfileActivity(petId);
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