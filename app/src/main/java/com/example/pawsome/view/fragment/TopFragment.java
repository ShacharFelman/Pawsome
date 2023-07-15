package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.databinding.FragmentTopBinding;
import com.example.pawsome.view.activity.MainActivity;

public class TopFragment extends Fragment {


    private FragmentTopBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initButtonsListeners();
        setUserNameView();
        if(CurrentPet.getInstance().getPetProfile() != null)
            setUserImageView();

        return root;
    }

    private void initButtonsListeners() {
        binding.topBTNLogout.setOnClickListener(v -> ((MainActivity) getActivity()).signOut());
    }

    private void setUserImageView() {
        Glide
                .with(this)
                .load(CurrentPet.getInstance().getPetProfile().getProfileImage())
                .into(binding.topIMGProfile);
    }

    private void setUserNameView() {
        binding.topTVTitle.setText("Hello " + CurrentUser.getInstance().getUserProfile().getName());
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