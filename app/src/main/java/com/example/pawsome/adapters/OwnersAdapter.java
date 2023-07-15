package com.example.pawsome.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawsome.R;
import com.example.pawsome.model.UserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class OwnersAdapter extends RecyclerView.Adapter<OwnersAdapter.OwnerViewHolder> {

    private final AppCompatActivity activity;
    private List<UserProfile> owners;

//    private MealCallback mealCallback;


    public OwnersAdapter(AppCompatActivity activity, List<UserProfile> owners) {
        this.owners = owners;
        this.activity = activity;
    }

//    public PetsAdapter setMealCallback(MealCallback mealCallback) {
//        this.mealCallback = mealCallback;
//        return this;
//    }

    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lst_owner, parent, false);
        OwnerViewHolder ownerViewHolder = new OwnerViewHolder(view);
        return ownerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder holder, int position) {
        UserProfile owner = getItem(position);
        holder.owner_TV_name.setText(owner.getName());
        Glide.
                with(activity).
                load(owner.getProfileImage()).
                into(holder.owner_IMG_user);
    }


    public int calcAge(LocalDate dob) {
        LocalDate now = LocalDate.now();
        return Period.between(dob, now).getYears();
    }

    private UserProfile getItem(int position) {
        return owners.get(position);
    }

    @Override
    public int getItemCount() {
        return owners == null ? 0 : owners.size();
    }

    public void updateOwnersList(List<UserProfile> owners) {
        this.owners = owners;
        notifyDataSetChanged();
    }

//    public void deleteMeal(String mealId) {
//        if (mealsList.containsKey(mealId))
//            mealsList.remove(mealId);
//        notifyDataSetChanged();
//    }

    public List<UserProfile> getOwners() {
        return owners;
    }

    public class OwnerViewHolder extends RecyclerView.ViewHolder {
        private ImageView owner_IMG_user;
        private MaterialTextView owner_TV_name;
        private MaterialButton owner_BTN_delete;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
//            itemView.setOnClickListener(view -> mealCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()));
        }

        private void initViews() {
            owner_IMG_user = itemView.findViewById(R.id.owner_IMG_user);
            owner_TV_name = itemView.findViewById(R.id.owner_TV_name);
            owner_BTN_delete = itemView.findViewById(R.id.owner_BTN_delete);
        }
    }
}