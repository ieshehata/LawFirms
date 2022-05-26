package com.app.lawfirms.activities.general.fragment;

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
import com.lawfirms.android.adapters.ConversationAdapter;
import com.lawfirms.android.callbacks.ConversationCallback;
import com.lawfirms.android.controllers.ConversationController;
import com.lawfirms.android.models.ConversationModel;
import com.lawfirms.android.utils.LoadingHelper;
import com.lawfirms.android.utils.SharedData;

import java.util.ArrayList;


public class ConversationsFragment extends Fragment {

    private LoadingHelper loadingHelper;
    private ConversationAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;

    private ArrayList<ConversationModel> currentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        loadingHelper = new LoadingHelper(getActivity());
        noList = view.findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(getActivity());
        getData();

        return view;
    }

    private void getData() {
        loadingHelper.showLoading("");
        new ConversationController().getConversationsByUser(SharedData.currentUser.getKey(),
                new ConversationCallback() {
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