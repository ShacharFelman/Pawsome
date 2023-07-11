package com.example.pawsome.Callbacks.adam;

import com.example.pawsome.model.adam_delete.Group;

public interface GroupCallback_adam {
    void joinClicked(Group group, int position);
    void itemClicked(Group group, int position);
    void leaveClicked(Group group, int position);
}
