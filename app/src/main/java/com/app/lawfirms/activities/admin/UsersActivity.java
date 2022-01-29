package com.app.lawfirms.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.lawfirms.R;
import com.app.lawfirms.adapter.UserAdapter;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.controllers.UserController;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.LoadingHelper;

import java.util.ArrayList;

public class UsersActivity  extends AppCompatActivity implements UserAdapter.UserListener, View.OnClickListener{
private LoadingHelper loadingHelper;
private RecyclerView recyclerView;
private Button allButton, activeButton, inactiveButton;
private UserAdapter adapter;
private ArrayList<UserModel> allOweners = new ArrayList<>();
private ArrayList<UserModel> activeOweners = new ArrayList<>();
private ArrayList<UserModel> inactiveOweners = new ArrayList<>();
private int listFilter = 0; //0 -> All, 1 -> active, 2 -> inactive
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView = findViewById(R.id.recycler_view);
        allButton = findViewById(R.id.all_button);
        activeButton = findViewById(R.id.active_button);
        inactiveButton = findViewById(R.id.inactive_button);

        allButton.setOnClickListener(UsersActivity.this);
        activeButton.setOnClickListener(UsersActivity.this);
        inactiveButton.setOnClickListener(UsersActivity.this);
        loadingHelper = new LoadingHelper(this);

        load();
    }

    private void load() {
        loadingHelper.showLoading("");
        new UserController().getUsersAlways(new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
                allOweners = new ArrayList<>();
                activeOweners = new ArrayList<>();
                inactiveOweners = new ArrayList<>();
                for(UserModel user : users) {
                    if(user.getState() == 1 && user.getUserType() == 3) {
                        allOweners.add(user);
                        activeOweners.add(user);
                    }else if(user.getState() == -1 && user.getUserType() == 3) {
                        allOweners.add(user);
                        inactiveOweners.add(user);
                    }
                }
                if(adapter == null || adapter.getData().size() == 0) {
                    if(listFilter == 0) {
                        adapter = new UserAdapter(allOweners, UsersActivity.this);
                    }else if(listFilter == 1) {
                        adapter = new UserAdapter(activeOweners, UsersActivity.this);
                    }else if(listFilter == 2) {
                        adapter = new UserAdapter(inactiveOweners, UsersActivity.this);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
                }else {
                    if(listFilter == 0) {
                        adapter.updateData(allOweners);
                    }else if(listFilter == 1) {
                        adapter.updateData(activeOweners);
                    }else if(listFilter == 2) {
                        adapter.updateData(inactiveOweners);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_button:
                listFilter = 0;
                allButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryMidDark));
                allButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

                activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.active_button:
                listFilter = 1;
                allButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryMidDark));
                activeButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.inactive_button:
                listFilter = 2;
                allButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryMidDark));
                inactiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                filterUpdated();
                break;
        }
    }

    private void filterUpdated() {
        if(listFilter == 0) {
            adapter.updateData(allOweners);
        }else if(listFilter == 1) {
            adapter.updateData(activeOweners);
        }else if(listFilter == 2) {
            adapter.updateData(inactiveOweners);
        }
    }

    @Override
    public void response(int position, boolean isBlocking) {
        loadingHelper.showLoading("");
        UserModel user = adapter.getData().get(position);
        user.setState(isBlocking ? -1 : 1);
        user.setActivated(isBlocking ? -1 : 1);
        new UserController().save(user, new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> oweners) {
                loadingHelper.dismissLoading();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new UserController().delete(adapter.getData().get(position), new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> oweners) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, "deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {

    }
}