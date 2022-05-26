package com.app.lawfirms.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lawfirms.android.R;
import com.lawfirms.android.activities.general.LawyerDetailsActivity;
import com.lawfirms.android.adapters.LawyersAdapter;
import com.lawfirms.android.callbacks.UserCallback;
import com.lawfirms.android.controllers.UserController;
import com.lawfirms.android.models.UserModel;
import com.lawfirms.android.utils.LoadingHelper;
import com.lawfirms.android.utils.SharedData;

import java.util.ArrayList;

public class LawyersListActivity extends AppCompatActivity implements LawyersAdapter.LawyerListener {
    private LoadingHelper loadingHelper;
    private RecyclerView recyclerView;
    private LawyersAdapter adapter;
    private TextView noList;

    private ArrayList<UserModel> allLawyers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyers_list);

        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_items);
        setTitle(SharedData.servicesNames.get(SharedData.currentCategory) + " Lawyers");
        loadingHelper = new LoadingHelper(this);

        load();
    }


    private void load() {
        loadingHelper.showLoading("");
        new UserController().getLawyersAlways(SharedData.currentCategory, new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
                if (users.size() > 0) {
                    allLawyers = users;
                    if(adapter == null || adapter.getData().size() == 0) {
                        adapter = new LawyersAdapter(allLawyers, LawyersListActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(LawyersListActivity.this));
                    }else {
                        adapter.updateData(allLawyers);
                    }
                } else {
                    noList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(LawyersListActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void response(int position, boolean isBlocking) {

    }

    @Override
    public void view(int position) {
        SharedData.currentLawyer = adapter.getData().get(position);
        Intent intent = new Intent(LawyersListActivity.this, LawyerDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int position) {

    }
}