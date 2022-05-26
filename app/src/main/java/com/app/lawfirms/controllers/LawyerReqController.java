package com.app.lawfirms.controllers;

import androidx.annotation.NonNull;

import com.app.lawfirms.callback.LawyerReqCallback;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.models.LawyersReqModel;
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

public class LawyerReqController {
    private final String node = "RegistrationRequests";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<LawyersReqModel> requests = new ArrayList<>();

    public void save(final LawyersReqModel request, final LawyerReqCallback callback) {
        if(request.getKey() == null){
            request.setKey(myRef.push().getKey());
        }else if(request.getKey().equals("")){
            request.setKey(myRef.push().getKey());
        }
        myRef = database.getReference(node + "/" + request.getKey());
        myRef.setValue(request)
                .addOnSuccessListener(aVoid -> {
                    requests.add(request);
                    callback.onSuccess(requests);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getRequests(final LawyerReqCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LawyersReqModel request = snapshot.getValue(LawyersReqModel.class);
                    requests.add(request);
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void geRequestsAlways(final LawyerReqCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LawyersReqModel request = snapshot.getValue(LawyersReqModel.class);
                    if(request.getState() == 0) {
                        requests.add(request);
                    }
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getRequestByKey(final String key, final LawyerReqCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LawyersReqModel a = snapshot.getValue(LawyersReqModel.class);
                    if(a.getKey().equals(key)) {
                        requests.add(a);
                    }
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(LawyersReqModel request, final LawyerReqCallback callback) {
        myRef = database.getReference(node+"/"+request.getKey());
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
                        requests = new ArrayList<>();
                        callback.onSuccess(requests);
                    }
                });
    }

    public void newRequest(UserModel lawyer, final LawyerReqCallback callback) {
        final LawyersReqModel request = new LawyersReqModel();
        request.setLawyer(lawyer);
        request.setState(0);

        save(request, new LawyerReqCallback() {
            @Override
            public void onSuccess(ArrayList<LawyersReqModel> caregiverReqs) {
                callback.onSuccess(requests);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void responseOnRequest(LawyersReqModel request, Boolean isAccepted, final LawyerReqCallback callback) {
        request.setState(isAccepted ? 1 : -1);
        save(request, new LawyerReqCallback() {
            @Override
            public void onSuccess(ArrayList<LawyersReqModel> requests) {
                new UserController().getUserByKey(request.getLawyer().getKey(), new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> suppliers) {
                        if(suppliers.size() > 0) {
                            UserModel caregiver = suppliers.get(0);
                            caregiver.setState(isAccepted ? 1 : -1);
                            new UserController().save(caregiver, new UserCallback() {
                                @Override
                                public void onSuccess(ArrayList<UserModel> suppliers) {
                                    callback.onSuccess(requests);

                                }

                                @Override
                                public void onFail(String error) {
                                    callback.onFail(error);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        callback.onFail(error);
                    }

                });
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
