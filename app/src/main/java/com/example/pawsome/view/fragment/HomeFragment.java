package com.example.pawsome.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.pawsome.databinding.FragmentHomeBinding;
import com.example.pawsome.model.Meal;
import com.example.pawsome.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
//    private OffersAdapter offerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        loadOffers();
        return root;
    }

    private void initFragmentData() {
        initViews();
        setCallbacks();
        initListeners();
    }

    private void initListeners() {

    }

    private void initViews() {
//        if (meals == null || meals.isEmpty()) {
//            binding.homeLSTOffers.setVisibility(View.GONE);
//            binding.homeTVNoGroups.setVisibility(View.VISIBLE);
//        } else {
//            Log.d("firebase", "else: " + meals.toString());
//            binding.homeLSTOffers.setVisibility(View.VISIBLE);
//            binding.homeTVNoGroups.setVisibility(View.GONE);
//        }
//        offerAdapter = new OffersAdapter(this, meals);
//        binding.homeLSTOffers.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.homeLSTOffers.setAdapter(offerAdapter);
    }

    private void setCallbacks() {
//        offerAdapter.setOfferCallback(new OfferCallback() {
//            @Override
//            public void joinClicked(Offer offer, int position) {
//                if (offer.getCapacity() <= offer.getNumOfUsers()) {
//                    SignalSingleton.getInstance().toast("The group is full");
//                } else {
//                    CurrentUser.getInstance().getUserProfile().getOffers().put(offer.getId(), offer);
//                    offer.addUser(CurrentUser.getInstance().getUserProfile().getUid());
////                    databaseRef = FirebaseDatabase.getInstance().getReference(Constants.DB_USERS);
////                    databaseRef.child(CurrentUser.getInstance().getUserProfile().getUid()).setValue(CurrentUser.getInstance().getUserProfile());
//                    offerAdapter.removeOffer(offer.getId());
//                    offerAdapter.notifyItemRemoved(position);
//                }
//            }
//
//            @Override
//            public void itemClicked(Offer offer, int position) {
//                //move to the offer page with the offer details
//            }
//
////            @Override
////            public void leaveClicked(Offer item, int position) {}
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
//        binding.homeETSearch.setText(null);
    }
}