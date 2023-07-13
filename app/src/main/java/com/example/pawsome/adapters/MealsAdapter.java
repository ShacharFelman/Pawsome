package com.example.pawsome.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawsome.callbacks.MealCallback;
import com.example.pawsome.R;
import com.example.pawsome.current_state.CurrentUser;
import com.example.pawsome.model.Meal;
import com.example.pawsome.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private final Fragment fragment;
    private List<Meal> mealsList;


    private MealCallback mealCallback;


    public MealsAdapter(Fragment fragment, List<Meal> mealsList) {
        this.mealsList = mealsList;
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
        holder.meal_TV_user.setText(meal.getOwner().getName());
        holder.meal_TV_time.setText(meal.getDateTimeAsLocalDateTime().format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME)));
        holder.meal_TV_date.setText(meal.getDateTimeAsLocalDateTime().format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE)));
        holder.meal_TV_note.setText(meal.getNote());
        holder.meal_TV_type.setText(meal.getMealType().getName());
        Glide.
                with(fragment.getContext()).
                load(meal.getOwner().getProfileImage()).
                into(holder.meal_IMG_user);

        if(meal.getOwner().getUid().equals(CurrentUser.getInstance().getUid()))
            holder.meal_CV_item.setStrokeWidth(0);
        else
            holder.meal_CV_item.setStrokeWidth(2);
    }

    private Meal getItem(int position) {
        return mealsList.get(position);
    }

    @Override
    public int getItemCount() {
        return mealsList == null ? 0 : mealsList.size();
    }

    public void updateMeals(List<Meal> mealsList) {
        this.mealsList = mealsList;
        notifyDataSetChanged();
    }

//    public void deleteMeal(String mealId) {
//        if (mealsList.containsKey(mealId))
//            mealsList.remove(mealId);
//        notifyDataSetChanged();
//    }

    public List<Meal> getMealsList() {
        mealsList.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return mealsList;
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        private ImageView meal_IMG_user;
        private MaterialTextView meal_TV_user;
        private MaterialTextView meal_TV_time;
        private MaterialTextView meal_TV_date;
        private MaterialTextView meal_TV_type;
        private MaterialTextView meal_TV_note;
        private MaterialButton meal_BTN_delete;
        private MaterialCardView meal_CV_item;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(view -> mealCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()));
        }

        private void initViews() {
            meal_IMG_user = itemView.findViewById(R.id.meal_IMG_user);
            meal_TV_user = itemView.findViewById(R.id.meal_TV_user);
            meal_TV_time = itemView.findViewById(R.id.meal_TV_time);
            meal_TV_date = itemView.findViewById(R.id.meal_TV_date);
            meal_TV_type = itemView.findViewById(R.id.meal_TV_type);
            meal_TV_note = itemView.findViewById(R.id.meal_TV_note);
            meal_BTN_delete = itemView.findViewById(R.id.meal_BTN_delete);
            meal_CV_item = itemView.findViewById(R.id.meal_CV_item);
        }
    }
}
