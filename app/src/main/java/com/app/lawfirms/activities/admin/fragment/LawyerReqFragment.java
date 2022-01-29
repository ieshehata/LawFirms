package com.app.lawfirms.activities.admin.fragment;

import android.content.Intent;
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
import com.app.lawfirms.activities.general.LawyerDetailsActivity;
import com.app.lawfirms.adapter.LawyerReqAdapter;
import com.app.lawfirms.callback.LawyerReqCallback;
import com.app.lawfirms.controllers.LawyerReqController;
import com.app.lawfirms.models.LawyersReqModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;

import java.util.ArrayList;

public class LawyerReqFragment extends Fragment implements LawyerReqAdapter.LawyerReqListener {

    private LinearLayout root;
    private LoadingHelper loadingHelper;
    private LawyerReqAdapter adapter;
    private RecyclerView recyclerView;
    private LawyersReqModel req = new LawyersReqModel();
    private LawyersReqModel.ClassNew newClass = new LawyersReqModel.ClassNew();
    private TextView noList;
    private ArrayList<LawyersReqModel> currentList = new ArrayList<>();

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
        new LawyerReqController().geRequestsAlways(new LawyerReqCallback() {
            @Override
            public void onSuccess(ArrayList<LawyersReqModel> requests) {
                loadingHelper.dismissLoading();
                currentList = requests;
                noList.setText("No Requests Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new LawyerReqAdapter(currentList, LawyerReqFragment.this);
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
    public void response(int position, boolean isAccepted) {
        loadingHelper.showLoading("");
        new LawyerReqController().responseOnRequest(adapter.getData().get(position), isAccepted, new LawyerReqCallback() {
            @Override
            public void onSuccess(ArrayList<LawyersReqModel> requests) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), "Done!", Toast.LENGTH_LONG).show();
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
        SharedData.lawyer = adapter.getData().get(position).getLawyerer();
        Intent intent = new Intent(getActivity(), LawyerDetailsActivity.class);
        startActivity(intent);
    }
}