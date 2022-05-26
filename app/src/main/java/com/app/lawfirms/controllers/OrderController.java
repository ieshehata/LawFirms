package com.app.lawfirms.controllers;

import androidx.annotation.NonNull;

import com.app.lawfirms.callback.OrderCallback;
import com.app.lawfirms.models.OrderModel;
import com.app.lawfirms.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class OrderController {
    private final String node = "Orders";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<OrderModel> orders = new ArrayList<>();

    public void save(final OrderModel order, final OrderCallback callback) {
        if(order.getKey() == null || order.getKey().equals("")){
            order.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + order.getKey());
        myRef.setValue(order)
                .addOnSuccessListener(aVoid -> {
                    orders.add(order);
                    callback.onSuccess(orders);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getOrders(final OrderCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel order = snapshot.getValue(OrderModel.class);
                    orders.add(order);
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getOrdersAlways(final OrderCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    orders.add(model);
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getOrderByKey(final String key, final OrderCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    if(model.getKey().equals(key)) {
                        orders.add(model);
                    }
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getOrderByTime(final ArrayList<Date> day, final OrderCallback callback){
        Query query = myRef.orderByChild("time/key").equalTo(String.valueOf(day));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    if(model.getDays().equals(day)) {
                        orders.add(model);
                    }
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getOrderByLawyer(final String userKey, final OrderCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    if(model.getLawyer() != null && model.getLawyer().getKey() != null && model.getLawyer().getKey().equals(userKey) && model.getUser() != null) {
                        orders.add(model);
                    }
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getOrderByUser(final String userKey, final OrderCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    if(model.getUser() != null && model.getUser().getKey() != null && model.getUser().getKey().equals(userKey) && model.getLawyer() != null) {
                        orders.add(model);
                    }
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(OrderModel order,final OrderCallback callback) {
        myRef = database.getReference(node+"/"+order.getKey());
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
                        orders = new ArrayList<>();
                        callback.onSuccess(orders);
                    }
                });
    }

    public void newOrder(UserModel userModel, int value, String description, final OrderCallback callback) {
        OrderModel order = new OrderModel();
        order.setLawyer(userModel);
        order.setUser(userModel);
        order.setTotalPrice(value);
        order.setDescription(description);

        save(order, new OrderCallback() {
            @Override
            public void onSuccess(ArrayList<OrderModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void responseOnRequest(OrderModel order, Boolean isAccepted, final OrderCallback callback) {
        order.setState(isAccepted ? 1 : -1);
        save(order, new OrderCallback() {
            @Override
            public void onSuccess(ArrayList<OrderModel> orders) {
                callback.onSuccess(orders);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
