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
    private LoadingHelper loadingHelper;
    private RecyclerView recyclerView;
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
    private LawyersAdapter adapter;
    private ArrayList<UserModel> personals = new ArrayList<>();
    private ArrayList<UserModel> criminals = new ArrayList<>();
    private ArrayList<UserModel> commercials = new ArrayList<>();
    private ArrayList<UserModel> internationals = new ArrayList<>();
    private ArrayList<UserModel> publics = new ArrayList<>();
    private ArrayList<UserModel> financials = new ArrayList<>();
    private ArrayList<UserModel> reals = new ArrayList<>();
    private ArrayList<UserModel> labors = new ArrayList<>();
    private ArrayList<UserModel> administratives = new ArrayList<>();
    private ArrayList<UserModel> civils = new ArrayList<>();

    private int listFilter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
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
        loadingHelper = new LoadingHelper(getActivity());

        getData();
        return view;
    }

    private void getData() {
        loadingHelper.showLoading("");
        new UserController().getUsersAlways(new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
               /* lawyers = new ArrayList<>();
                for(UserModel user : users) {
                    if(user.getState() == 1 && user.getUserType() == 2) {
                        lawyers.add(user);
                    }else if(user.getState() == -1 && user.getUserType() == 2) {
                        lawyers.add(user);
                        lawyers.add(user);
                    }
                }*/
                if(adapter == null || adapter.getData().size() == 0) {
                    if(listFilter == 0) {
                        adapter = new LawyersAdapter(personals, UserHomeFragment.this);
                    }else if(listFilter == 1) {
                        adapter = new LawyersAdapter(criminals, UserHomeFragment.this);
                    }else if(listFilter == 2) {
                        adapter = new LawyersAdapter(commercials, UserHomeFragment.this);
                    }else if(listFilter == 3) {
                        adapter = new LawyersAdapter(publics, UserHomeFragment.this);
                    }else if(listFilter == 4) {
                        adapter = new LawyersAdapter(internationals, UserHomeFragment.this);
                    }else if(listFilter == 5) {
                        adapter = new LawyersAdapter(financials, UserHomeFragment.this);
                    }else if(listFilter == 6) {
                        adapter = new LawyersAdapter(reals, UserHomeFragment.this);
                    }else if(listFilter == 7) {
                        adapter = new LawyersAdapter(labors, UserHomeFragment.this);
                    }else if(listFilter == 8) {
                        adapter = new LawyersAdapter(administratives, UserHomeFragment.this);
                    }else if(listFilter == 9) {
                        adapter = new LawyersAdapter(civils, UserHomeFragment.this);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else {
                    if(listFilter == 0) {
                        adapter.updateData(personals);
                    }else if(listFilter == 1) {
                        adapter.updateData(criminals);
                    }else if(listFilter == 2) {
                        adapter.updateData(commercials);
                    }else if(listFilter == 3) {
                        adapter.updateData(publics);
                    }else if(listFilter == 4) {
                        adapter.updateData(internationals);
                    }else if(listFilter == 5) {
                        adapter.updateData(financials);
                    }else if(listFilter == 6) {
                        adapter.updateData(reals);
                    }else if(listFilter == 7) {
                        adapter.updateData(labors);
                    }else if(listFilter == 8) {
                        adapter.updateData(administratives);
                    }else if(listFilter == 9) {
                        adapter.updateData(civils);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal:
                listFilter = 0;
                filterUpdated();
                break;

            case R.id.criminal:
                listFilter = 1;

                filterUpdated();
                break;

            case R.id.commercial:
                listFilter = 2;
                filterUpdated();
                break;

            case R.id.publicc:
                listFilter = 3;
                filterUpdated();
                break;

            case R.id.international:
                listFilter = 4;
                filterUpdated();
                break;

            case R.id.financial:
                listFilter = 5;
                filterUpdated();
                break;

            case R.id.layout_real:
                listFilter = 6;
                filterUpdated();
                break;

            case R.id.labor:
                listFilter = 7;
                filterUpdated();
                break;

            case R.id.administrative:
                listFilter = 8;
                filterUpdated();
                break;

            case R.id.civil:
                listFilter = 9;
                filterUpdated();
                break;
        }

    }

    private void filterUpdated() {
        if(listFilter == 0) {
            adapter.updateData(personals);
        }else if(listFilter == 1) {
            adapter.updateData(criminals);
        }else if(listFilter == 2) {
            adapter.updateData(commercials);
        }else if(listFilter == 3) {
            adapter.updateData(publics);
        }else if(listFilter == 4) {
            adapter.updateData(internationals);
        }else if(listFilter == 5) {
            adapter.updateData(financials);
        }else if(listFilter == 6) {
            adapter.updateData(reals);
        }else if(listFilter == 7) {
            adapter.updateData(labors);
        }else if(listFilter == 8) {
            adapter.updateData(administratives);
        }else if(listFilter == 9) {
            adapter.updateData(civils);
        }
    }

    @Override
    public void response(int position, boolean isBlocking) {
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