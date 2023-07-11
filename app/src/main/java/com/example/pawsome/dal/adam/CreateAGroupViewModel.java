package com.example.pawsome.dal.adam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.model.adam_delete.Game;
import com.example.pawsome.model.adam_delete.Group;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateAGroupViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Game>> mGames;

    public CreateAGroupViewModel() {
        mGames = new MutableLiveData<>();
        ArrayList<Game> games = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference().child(Constants.DB_GAMES);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Game newGame = snapshot.getValue(Game.class);
                if (newGame != null) {
                    games.add(newGame);
                    mGames.setValue(games);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * updating firebase: groups, admins' group
     **/
    public void updateGroupDB(Group newGroup){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = db.getReference(Constants.DB_GROUPS);
        databaseReference.child(newGroup.getId()).setValue(newGroup);

        DatabaseReference userDatabaseReference = db.getReference(Constants.DB_USERS);
        userDatabaseReference.child(CurrentUser.getInstance().getUid()).setValue(CurrentUser.getInstance());
    }

    public MutableLiveData<ArrayList<Game>> getMGames() {
        return mGames;
    }
}