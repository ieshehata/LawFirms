package com.app.lawfirms.activities.lawyer.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.lawfirms.R;
import com.app.lawfirms.adapter.ConversationAdapter;
import com.app.lawfirms.callback.ConversationCallback;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.controllers.ConversationController;
import com.app.lawfirms.controllers.UserController;
import com.app.lawfirms.models.ConversationModel;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;


public class LawyerChatFragment extends Fragment {
    private ArrayList<ConversationModel> currentList = new ArrayList<>();
    private LoadingHelper loadingHelper;
    private ConversationAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;
    private FloatingActionButton add;
    private static final String TAG = "MainActivity";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        loadingHelper = new LoadingHelper(getActivity());
        noList = view.findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(getActivity());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        SharedData.currentUser.setFcmToken(token);
                        new UserController().save(SharedData.currentUser, new UserCallback() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> users) {
                            }

                            @Override
                            public void onFail(String error) {
                            }
                        });
                        Log.d(TAG, msg);
                        //Toast.makeText(UserHomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        getData();
        return view;
    }

    private void getData() {
        loadingHelper.showLoading("");
        new ConversationController().getConversationsByUser(SharedData.currentUser.getKey(), new ConversationCallback() {
            @Override
            public void onSuccess(ArrayList<ConversationModel> conversations) {
                loadingHelper.dismissLoading();
                currentList = conversations;
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new ConversationAdapter(currentList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    } else {
                        adapter.updateData(currentList);
                    }
                } else {
                    noList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }







}
