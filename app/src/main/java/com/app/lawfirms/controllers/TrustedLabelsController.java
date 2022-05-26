package com.app.lawfirms.controllers;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawfirms.android.callbacks.TrustedLabelsCallback;

import java.util.ArrayList;

public class TrustedLabelsController {
    private final String node = "TrustedLabels";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<String> trustedLabels = new ArrayList<>();

    public void save(final ArrayList<String> labels, final TrustedLabelsCallback callback) {
        myRef = database.getReference(node);
        myRef.setValue(labels)
                .addOnSuccessListener(aVoid -> {
                    trustedLabels = new ArrayList<>();
                    trustedLabels.addAll(labels);
                    callback.onSuccess(trustedLabels);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getLabels(final TrustedLabelsCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trustedLabels = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String labels = snapshot.getValue(String.class);
                    trustedLabels.add(labels);
                }
                callback.onSuccess(trustedLabels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getLabelsAlways(final TrustedLabelsCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trustedLabels = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String model = snapshot.getValue(String.class);
                    trustedLabels.add(model);
                }
                callback.onSuccess(trustedLabels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }
}
