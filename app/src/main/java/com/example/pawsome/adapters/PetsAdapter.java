package com.example.pawsome.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawsome.R;
import com.example.pawsome.callbacks.PetCallback;
import com.example.pawsome.model.PetProfile;
import com.example.pawsome.utils.DateTimeConverter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private final Fragment fragment;
    private List<PetProfile> petsList;

    private PetCallback petCallback;

    public PetsAdapter(Fragment fragment, List<PetProfile> petsList) {
        this.petsList = petsList;
        this.fragment = fragment;
    }

    public PetsAdapter setPetCallback(PetCallback petCallback) {
        this.petCallback = petCallback;
        return this;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lst_pet, parent, false);
        PetViewHolder petViewHolder = new PetViewHolder(view);
        return petViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetProfile pet = getItem(position);
        holder.pet_item_TV_name.setText(pet.getName());
        holder.pet_item_TV_age.setText(calcAge(DateTimeConverter.longToLocalDate(pet.getDateOfBirth())) + " years old");
        Glide.
                with(fragment.getContext()).
                load(pet.getProfileImage()).
                into(holder.pet_item_IMG_pet);
    }


    public int calcAge(LocalDate dob) {
        LocalDate now = LocalDate.now();
        return Period.between(dob, now).getYears();
    }

    private PetProfile getItem(int position) {
        return petsList.get(position);
    }

    @Override
    public int getItemCount() {
        return petsList == null ? 0 : petsList.size();
    }

    public void updatePetsList(List<PetProfile> petsList) {
        this.petsList = petsList;
        notifyDataSetChanged();
    }

//    public void deleteMeal(String mealId) {
//        if (mealsList.containsKey(mealId))
//            mealsList.remove(mealId);
//        notifyDataSetChanged();
//    }

    public List<PetProfile> getPetsList() {
        return petsList;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {
        private ImageView pet_item_IMG_pet;
        private MaterialTextView pet_item_TV_name;
        private MaterialTextView pet_item_TV_age;
        private MaterialButton pet_item_BTN_delete;
        private MaterialCardView pet_item_CV_item;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(view -> petCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()));
            pet_item_BTN_delete.setOnClickListener(view -> petCallback.deleteClicked(getItem(getAdapterPosition()), getAdapterPosition()));
        }

        private void initViews() {
            pet_item_IMG_pet = itemView.findViewById(R.id.pet_item_IMG_pet);
            pet_item_TV_name = itemView.findViewById(R.id.pet_item_TV_name);
            pet_item_TV_age = itemView.findViewById(R.id.pet_item_TV_age);
            pet_item_BTN_delete = itemView.findViewById(R.id.pet_item_BTN_delete);
            pet_item_CV_item = itemView.findViewById(R.id.pet_item_CV_item);
        }
    }
}
