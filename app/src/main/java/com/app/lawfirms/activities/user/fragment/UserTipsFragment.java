package com.app.lawfirms.activities.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lawfirms.android.R;
import com.lawfirms.android.adapters.TipAdapter;
import com.lawfirms.android.callbacks.TipCallback;
import com.lawfirms.android.controllers.TipController;
import com.lawfirms.android.models.TipModel;
import com.lawfirms.android.utils.LoadingHelper;

import java.util.ArrayList;


public class UserTipsFragment extends Fragment {

    private LoadingHelper loadingHelper;
    private TipAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_tips, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        noList = view.findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(getActivity());
        
        getData();
        
        return view;
    }


    private void getData() {
        new TipController().getTipsAlways(new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipModel> tips) {
                if(tips.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if(adapter == null || adapter.getData().size() == 0) {
                        adapter = new TipAdapter(tips, null);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }else {
                        adapter.updateData(tips);
                    }
                }else {
                    noList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}