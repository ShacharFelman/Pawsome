package com.example.pawsome.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawsome.adapters.MealsAdapter;
import com.example.pawsome.current_state.CurrentPet;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.databinding.FragmentMealLogBinding;
import com.example.pawsome.model.Meal;
import com.example.pawsome.view.activity.MainActivity;

import java.util.List;

public class MealLogFragment extends Fragment {
    private FragmentMealLogBinding binding;
    private MealsAdapter mealsAdapter;
    private List<Meal> meals;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initMealsList();
        initViews();
//        setCallbacks();
        initListeners();
        return root;
    }

//    private void initFragmentData() {
//        initViews();
//        setCallbacks();
//        initListeners();
//    }

    private void initListeners() {
    }

    private void initMealsList() {
        if(CurrentPet.getInstance().getPetProfile() != null)
            this.meals = CurrentPet.getInstance().getPetProfile().getMeals();
    }

    private void initViews() {
        if (meals == null || meals.isEmpty()) {
            binding.mealsLogLSTMeals.setVisibility(View.GONE);
            binding.mealsLogTVEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.mealsLogLSTMeals.setVisibility(View.VISIBLE);
            binding.mealsLogTVEmpty.setVisibility(View.GONE);
        }

        mealsAdapter = new MealsAdapter(this, meals);
        binding.mealsLogLSTMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mealsLogLSTMeals.setAdapter(mealsAdapter);
    }

//    private void setCallbacks() {
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
//
//    }

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