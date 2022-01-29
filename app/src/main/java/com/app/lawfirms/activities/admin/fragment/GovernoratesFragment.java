package com.app.lawfirms.activities.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lawfirms.R;
import com.app.lawfirms.adapter.GovernoratesAdapter;
import com.app.lawfirms.callback.GovernorateCallback;
import com.app.lawfirms.controllers.GovernorateController;
import com.app.lawfirms.dialogs.DataDialog;
import com.app.lawfirms.models.GovernorateModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;

import java.util.ArrayList;

public class GovernoratesFragment extends Fragment implements GovernoratesAdapter.GovernorateListener, DataDialog.DataDialogListener {

    private LinearLayout root;
    private LoadingHelper loadingHelper;
    private GovernoratesAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noList;
    private ArrayList<GovernorateModel> currentList = new ArrayList<>();
    private GovernorateModel chosenGovernorate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        root = view.findViewById(R.id.root);
        recyclerView = view.findViewById(R.id.recycler_view);
        noList = view.findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(getActivity());

        getData();

        return view;
    }

    private void getData() {
        loadingHelper.showLoading("");
        new GovernorateController().getGovernoratesAlways(new GovernorateCallback() {
            @Override
            public void onSuccess(ArrayList<GovernorateModel> governorates) {
                loadingHelper.dismissLoading();
                currentList = governorates;
                SharedData.allGovernorates = governorates;
                noList.setText("No Governoraties Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new GovernoratesAdapter(currentList, GovernoratesFragment.this);
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

    @Override
    public void onClick(int position) {
        chosenGovernorate = adapter.getData().get(position);
        DataDialog dialog = new DataDialog(adapter.getData().get(position).getName());
        dialog.show(GovernoratesFragment.this.getChildFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new GovernorateController().delete(adapter.getData().get(position), new GovernorateCallback() {
            @Override
            public void onSuccess(ArrayList<GovernorateModel> governorates) {
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

    @Override
    public void getData(String name) {
        chosenGovernorate.setName(name);
        new GovernorateController().save(chosenGovernorate, new GovernorateCallback() {
            @Override
            public void onSuccess(ArrayList<GovernorateModel> governorates) { }

            @Override
            public void onFail(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
