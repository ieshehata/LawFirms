package com.app.lawfirms.activities.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lawfirms.R;
import com.app.lawfirms.activities.general.LawyerDetailsActivity;
import com.app.lawfirms.adapter.LawyersAdapter;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.controllers.UserController;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;

import java.util.ArrayList;

public class LawyersFragment extends Fragment implements LawyersAdapter.LawyerListener , View.OnClickListener{

    private LoadingHelper loadingHelper;
    private RecyclerView recyclerView;
    private Button allButton, activeButton, inactiveButton;
    private LawyersAdapter adapter;
    private ArrayList<UserModel> allSuppliers = new ArrayList<>();
    private ArrayList<UserModel> activeSuppliers = new ArrayList<>();
    private ArrayList<UserModel> inactiveSuppliers = new ArrayList<>();
    private int listFilter = 0; //0 -> All, 1 -> active, 2 -> inactive

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lawyers, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        allButton = view.findViewById(R.id.all_button);
        activeButton = view.findViewById(R.id.active_button);
        inactiveButton = view.findViewById(R.id.inactive_button);

        allButton.setOnClickListener( LawyersFragment.this);
        activeButton.setOnClickListener(LawyersFragment.this);
        inactiveButton.setOnClickListener( LawyersFragment.this);
        loadingHelper = new LoadingHelper(getActivity());

        load();
        return view;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_button:
                listFilter = 0;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.active_button:
                listFilter = 1;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.inactive_button:
                listFilter = 2;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                filterUpdated();
                break;
        }
    }


    private void load() {
        loadingHelper.showLoading("");
        new UserController().getUsersAlways(new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
                allSuppliers = new ArrayList<>();
                activeSuppliers = new ArrayList<>();
                inactiveSuppliers = new ArrayList<>();
                for(UserModel user : users) {
                    if(user.getState() == 1 && user.getUserType() == 2) {
                        allSuppliers.add(user);
                        activeSuppliers.add(user);
                    }else if(user.getState() == -1 && user.getUserType() == 2) {
                        allSuppliers.add(user);
                        inactiveSuppliers.add(user);
                    }
                }
                if(adapter == null || adapter.getData().size() == 0) {
                    if(listFilter == 0) {
                        adapter = new LawyersAdapter(allSuppliers, LawyersFragment.this);
                    }else if(listFilter == 1) {
                        adapter = new LawyersAdapter(activeSuppliers, LawyersFragment.this);
                    }else if(listFilter == 2) {
                        adapter = new LawyersAdapter(inactiveSuppliers, LawyersFragment.this);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else {
                    if(listFilter == 0) {
                        adapter.updateData(allSuppliers);
                    }else if(listFilter == 1) {
                        adapter.updateData(activeSuppliers);
                    }else if(listFilter == 2) {
                        adapter.updateData(inactiveSuppliers);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterUpdated() {
        if(listFilter == 0) {
            adapter.updateData(allSuppliers);
        }else if(listFilter == 1) {
            adapter.updateData(activeSuppliers);
        }else if(listFilter == 2) {
            adapter.updateData(inactiveSuppliers);
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
            public void onSuccess(ArrayList<UserModel> stations)  {
                loadingHelper.dismissLoading();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {
        SharedData.lawyer = adapter.getData().get(position);
        Intent intent = new Intent(getActivity(), LawyerDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new UserController().delete(adapter.getData().get(position), new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> caregivers) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), "deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
