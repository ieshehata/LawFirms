package com.app.lawfirms.activities.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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


public class UserHomeFragment extends Fragment implements LawyersAdapter.LawyerListener , View.OnClickListener{
    private View personal;
    private LinearLayout criminal;
    private LinearLayout commercial;
    private LinearLayout publicc;
    private LinearLayout international;
    private LinearLayout financial;
    private LinearLayout real;
    private LinearLayout labor;
    private LinearLayout administrative;
    private LinearLayout civil;

    private int listFilter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        personal = view.findViewById(R.id.layout_personal);
        criminal = view.findViewById(R.id.layout_criminal);
        commercial = view.findViewById(R.id.layout_commercial);
        publicc = view.findViewById(R.id.layout_public);
        international = view.findViewById(R.id.layout_international);
        financial = view.findViewById(R.id.layout_financial);
        real = view.findViewById(R.id.layout_real);
        labor = view.findViewById(R.id.layout_labor);
        administrative = view.findViewById(R.id.layout_administrative);
        civil = view.findViewById(R.id.layout_civil);

        personal.setOnClickListener( UserHomeFragment.this);
        criminal.setOnClickListener(UserHomeFragment.this);
        commercial.setOnClickListener( UserHomeFragment.this);
        publicc.setOnClickListener( UserHomeFragment.this);
        international.setOnClickListener(UserHomeFragment.this);
        financial.setOnClickListener( UserHomeFragment.this);
        real.setOnClickListener( UserHomeFragment.this);
        labor.setOnClickListener(UserHomeFragment.this);
        administrative.setOnClickListener( UserHomeFragment.this);
        civil.setOnClickListener( UserHomeFragment.this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_personal:
                listFilter = 0;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_criminal:
                listFilter = 1;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_commercial:
                listFilter = 2;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_public:
                listFilter = 3;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_international:
                listFilter = 4;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_financial:
                listFilter = 5;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_real:
                listFilter = 6;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_labor:
                listFilter = 7;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_administrative:
                listFilter = 8;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;

            case R.id.layout_civil:
                listFilter = 9;
                SharedData.currentCategory = listFilter;
                startActivity(new Intent(getActivity(), LawyersListActivity.class));
                break;
        }
    }
}