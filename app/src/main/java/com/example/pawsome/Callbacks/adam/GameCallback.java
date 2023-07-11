package com.example.pawsome.Callbacks.adam;

import com.example.pawsome.model.adam_delete.Game;

public interface GameCallback {
    void favoriteClicked(Game game, int position);
    void itemClicked(Game game, int position);
}
