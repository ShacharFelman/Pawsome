package com.example.pawsome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsome.R;
import com.example.pawsome.model.MealType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MealTypeAdapter extends RecyclerView.Adapter<MealTypeAdapter.MealTypeViewHolder> {

    private List<MealType> mealTypes;
    private Context context;

    public MealTypeAdapter(Context context, List<MealType> mealTypes) {
        this.context = context;
        this.mealTypes = mealTypes;
    }

    @NonNull
    @Override
    public MealTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_type, parent, false);
        return new MealTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypeViewHolder holder, int position) {
        MealType mealType = mealTypes.get(position);
        holder.meal_type_TV_Name.setText(mealType.getName());
        holder.meal_type_BTN_delete.setOnClickListener(v -> {
            // TODO: Add your delete logic here
            mealTypes.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return mealTypes.size();
    }

    public static class MealTypeViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView meal_type_TV_Name;
        MaterialButton meal_type_BTN_delete;

        public MealTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            meal_type_TV_Name = itemView.findViewById(R.id.meal_type_TV_name);
            meal_type_BTN_delete = itemView.findViewById(R.id.meal_type_BTN_delete);
        }
    }
}
