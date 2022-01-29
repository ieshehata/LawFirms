package com.app.lawfirms.activities.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lawfirms.R;
import com.app.lawfirms.adapter.RateAdapter;
import com.app.lawfirms.callback.RateCallback;
import com.app.lawfirms.controllers.RateController;
import com.app.lawfirms.dialogs.RateDialog;
import com.app.lawfirms.models.RateModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class RateActivity extends AppCompatActivity implements RateAdapter.RateListener, RateDialog.RateDialogListener {
    RecyclerView list;
    TextView noList;
    private ArrayList<RateModel> currentList;
    RatingBar ratingBar;
    private LoadingHelper loadingHelper;
    private RateAdapter adapter;
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        list = findViewById(R.id.list);
        add = findViewById(R.id.fab);

        ratingBar = findViewById(R.id.simpleRatingBar);
        noList = findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(this);

        // perform click event on button
        add.setOnClickListener(v -> {
            RateDialog dialog = new RateDialog(false);
            dialog.show(getSupportFragmentManager(), "dialog");
        });

        if(SharedData.userType == 3){
            add.setVisibility(View.VISIBLE);
        }else {
            add.setVisibility(View.GONE);
        }

        loadingHelper.showLoading("");
        new RateController().getRates( new RateCallback() {
            @Override
            public void onSuccess(ArrayList<RateModel> rates) {
                loadingHelper.dismissLoading();
                currentList = rates;
                noList.setText("No Rates Found!");
                if (currentList.size() > 0) {
                    list.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new RateAdapter(currentList, RateActivity.this);
                        list.setAdapter(adapter);
                        list.setLayoutManager(new LinearLayoutManager(RateActivity.this));
                    } else {
                        adapter.updateData(currentList);
                    }
                } else {
                    noList.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFail(String msg) {
                loadingHelper.dismissLoading();
                Toast.makeText(RateActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getData(float rateValue, String commentText) {
        RateModel rate = new RateModel();
        rate.setComment(commentText);
        rate.setCreatedAt(Calendar.getInstance().getTime());
        rate.setRate(Math.round(rateValue));
        rate.setFromOwener(SharedData.user);
        rate.setToSupplier(SharedData.lawyer);
        new RateController().save(rate, new RateCallback() {
            @Override
            public void onSuccess(ArrayList<RateModel> rates) {
                Toast.makeText(RateActivity.this, "Send", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail(String msg) {
                Toast.makeText(RateActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}