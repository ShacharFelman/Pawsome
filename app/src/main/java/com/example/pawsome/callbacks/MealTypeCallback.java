package com.example.pawsome.callbacks;

import com.example.pawsome.model.MealType;

public interface MealTypeCallback {
    void removeClicked(MealType mealType, int position);
}