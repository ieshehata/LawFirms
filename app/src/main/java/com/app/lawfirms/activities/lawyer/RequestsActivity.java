package com.app.lawfirms.activities.lawyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lawfirms.R;
import com.app.lawfirms.adapter.RequestsAdapter;
import com.app.lawfirms.callback.OrderCallback;
import com.app.lawfirms.controllers.OrderController;
import com.app.lawfirms.models.OrderModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity implements RequestsAdapter.RequestsListener{
    private LoadingHelper loadingHelper;
    private RequestsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noList;
    private ArrayList<OrderModel> currentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(this);
        getData();
    }

    private void getData() {
        loadingHelper.showLoading("");
        new OrderController().getOrderByTime(SharedData.currentTime.getDays(), new OrderCallback() {
            @Override
            public void onSuccess(ArrayList<OrderModel> orders) {
                loadingHelper.dismissLoading();
                currentList = orders;
                noList.setText("No Orders Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new RequestsAdapter(currentList, RequestsActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
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
                Toast.makeText(RequestsActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void response(int position, boolean isAccepted) {
        loadingHelper.showLoading("");
        new OrderController().responseOnRequest(adapter.getData().get(position), isAccepted, new OrderCallback() {
            @Override
            public void onSuccess(ArrayList<OrderModel> reservations) {
                loadingHelper.dismissLoading();
                Toast.makeText(RequestsActivity.this, "Done!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(RequestsActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}