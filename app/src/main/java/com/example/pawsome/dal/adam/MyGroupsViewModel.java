package com.example.pawsome.dal.adam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.model.adam_delete.Group;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyGroupsViewModel extends ViewModel {

    private final MutableLiveData<HashMap<String, Group>> mGroups;

    public MyGroupsViewModel() {
        mGroups = new MutableLiveData<>();
        HashMap<String, Group> groups = new HashMap<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference().child(Constants.DB_GROUPS);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Group newGroup = snapshot.getValue(Group.class);
                if (newGroup != null) {
                    if (/*CurrentUser.getInstance().getGroups().get(newGroup.getId()) != null*/false) {
                        groups.put(newGroup.getId(), newGroup);
                        mGroups.setValue(groups);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Group newGroup = snapshot.getValue(Group.class);
                if (newGroup != null) {
                    if (/*CurrentUser.getInstance().getGroups().get(newGroup.getId()) != null*/false) {
                        groups.put(newGroup.getId(), newGroup);
                        mGroups.setValue(groups);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public MutableLiveData<HashMap<String, Group>> getGroups() {
        return mGroups;
    }

    public void updateUserDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_USERS);
        databaseReference.child(CurrentUser.getInstance().getUid()).setValue(CurrentUser.getInstance());
    }

    public void removeGroupFromDB(String groupID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_GROUPS).child(groupID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateGroupDB(Group group) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_GROUPS);
        databaseReference.child(group.getId()).setValue(group);
    }
}