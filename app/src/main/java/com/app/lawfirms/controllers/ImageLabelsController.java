package com.app.lawfirms.controllers;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawfirms.android.callbacks.ImageLabelsCallback;
import com.lawfirms.android.models.ImageLabelsModel;

import java.util.ArrayList;

public class ImageLabelsController {
    private final String node = "ImagesLabels";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<ImageLabelsModel> images = new ArrayList<>();

    public void save(final ImageLabelsModel image, final ImageLabelsCallback callback) {
        if(image.getKey() == null || image.getKey().equals("")){
            image.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + image.getKey());
        myRef.setValue(image)
                .addOnSuccessListener(aVoid -> {
                    images.add(image);
                    callback.onSuccess(images);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getImages(final ImageLabelsCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageLabelsModel image = snapshot.getValue(ImageLabelsModel.class);
                    images.add(image);
                }
                callback.onSuccess(images);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getImagesAlways(final ImageLabelsCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageLabelsModel model = snapshot.getValue(ImageLabelsModel.class);
                    images.add(model);
                }
                callback.onSuccess(images);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }
    public void delete(ImageLabelsModel image,final ImageLabelsCallback callback) {
        myRef = database.getReference(node+"/"+image.getKey());
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
                        images = new ArrayList<>();
                        callback.onSuccess(images);
                    }
                });
    }
}
