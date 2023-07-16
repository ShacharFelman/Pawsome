package com.example.pawsome.callbacks;

import com.example.pawsome.model.Meal;
import com.example.pawsome.model.Walk;

public interface WalkCallback {
    void itemClicked(Walk walk, int position);
    void deleteClicked(Walk walk, int position);
}
