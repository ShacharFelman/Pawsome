package com.example.pawsome.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawsome.Callbacks.MealCallback;
import com.example.pawsome.R;
import com.example.pawsome.model.Meal;
import com.example.pawsome.model.adam_delete.Group;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private Fragment fragment;
    private HashMap<String, Meal> allMealsList;


    private MealCallback mealCallback;


    public MealsAdapter(Fragment fragment) {
        this.allMealsList = new HashMap<>();
        this.fragment = fragment;
    }

    public MealsAdapter setMealCallback(MealCallback mealCallback) {
        this.mealCallback = mealCallback;
        return this;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        MealViewHolder mealViewHolder = new MealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = getItem(position);
        holder.meal_TV_time.setText(meal.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.meal_TV_date.setText(meal.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        holder.meal_TV_food_details.setText(meal.getAmount() + " " + meal.getUnit() + "(" + meal.getFoodType() + ")");
        holder.meal_TV_note.setText(meal.getNote());
        Glide.
                with(fragment.getContext()).
                load(meal.getOwner().getProfileImage()).
                into(holder.meal_IMG_user);
    }

    private Meal getItem(int position) {
        ArrayList<Meal> mealsValues = new ArrayList<>(allMealsList.values());
        return mealsValues.get(position);
    }

    @Override
    public int getItemCount() {
        return allMealsList == null ? 0 : allMealsList.size();
    }

    public void updateMeals(HashMap<String, Meal> meals) {
        this.allMealsList = meals;
        notifyDataSetChanged();
    }


    public void deleteMeal(String mealId) {
        if (allMealsList.containsKey(mealId))
            allMealsList.remove(mealId);
        notifyDataSetChanged();
    }


    public HashMap<String, Meal> getAllMealsList() {
        return allMealsList;
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        private ImageView meal_IMG_user;
        private MaterialTextView meal_TV_time;
        private MaterialTextView meal_TV_date;
        private MaterialTextView meal_TV_food_details;
        private MaterialTextView meal_TV_note;
        private MaterialButton meal_BTN_delete;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(view -> mealCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()));
        }

        private void initViews() {
            meal_TV_time = itemView.findViewById(R.id.group_TV_groupName);
            meal_TV_date = itemView.findViewById(R.id.group_TV_game);
            meal_TV_food_details = itemView.findViewById(R.id.group_TV_groupDescription);
            meal_TV_note = itemView.findViewById(R.id.group_TV_capacity);
            meal_BTN_delete = itemView.findViewById(R.id.group_BTN_join);
            meal_IMG_user = itemView.findViewById(R.id.group_IMG_game);
        }
    }
}
