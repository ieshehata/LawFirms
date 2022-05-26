package com.app.lawfirms.controllers;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lawfirms.android.callbacks.TipCallback;
import com.lawfirms.android.models.TipModel;

import java.util.ArrayList;
import java.util.Calendar;

public class TipController {
    private String node = "Tips";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<TipModel> tips = new ArrayList<>();

    public void save(final TipModel tip, final TipCallback callback) {
        if(tip.getKey() == null || tip.getKey().equals("")){
            tip.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + tip.getKey());
        myRef.setValue(tip)
                .addOnSuccessListener(aVoid -> {
                    tips.add(tip);
                    callback.onSuccess(tips);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getTips(final TipCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipModel tip = snapshot.getValue(TipModel.class);
                    tips.add(tip);
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getTipsAlways(final TipCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipModel tip = snapshot.getValue(TipModel.class);
                    tips.add(tip);
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getTipByKey(final String key, final TipCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipModel model = snapshot.getValue(TipModel.class);
                    if(model.getKey().equals(key)) {
                        tips.add(model);
                    }
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(TipModel tip,final TipCallback callback) {
        myRef = database.getReference(node+"/"+tip.getKey());
        myRef.removeValue()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tips = new ArrayList<>();
                        callback.onSuccess(tips);
                    }
                });
    }

    public void newTip(String image, String title, String text, final TipCallback callback) {
        TipModel tip = new TipModel();
        tip.setImage(image);
        tip.setTitle(title);
        tip.setText(text);
        tip.setRepeat(true);
        tip.setDate(Calendar.getInstance().getTime());

        save(tip, new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
