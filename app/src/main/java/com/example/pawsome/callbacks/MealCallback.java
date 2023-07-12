package com.example.pawsome.callbacks;

import com.example.pawsome.model.Meal;

public interface MealCallback {
    void itemClicked(Meal meal, int position);
    void deleteClicked(Meal meal, int position);
}
