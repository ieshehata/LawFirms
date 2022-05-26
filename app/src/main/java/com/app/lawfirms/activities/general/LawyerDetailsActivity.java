package com.app.lawfirms.activities.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lawfirms.R;
import com.app.lawfirms.activities.lawyer.LawyerNavigationActivity;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.controllers.UserController;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LawyerDetailsActivity extends AppCompatActivity {
    ViewPager viewPager;
    TextView name, governorate, city, description, phone, email, price, category;
    Button calender,location,sendMsg, rate;
    ImageView avatar;
    LoadingHelper loadingHelper;
    UserModel lawyer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        governorate = findViewById(R.id.governorate);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        phone = findViewById(R.id.phone);
        price = findViewById(R.id.price);
        email = findViewById(R.id.email);
        category = findViewById(R.id.category);
        calender = findViewById(R.id.calender);
        sendMsg =findViewById(R.id.chat);
        rate =findViewById(R.id.rate);

        location = findViewById(R.id.location);

        loadingHelper = new LoadingHelper(this);

        calender.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerDetailsActivity.this, LawyersCalenderActivity.class);
            startActivity(intent);
        });

        rate.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerDetailsActivity.this, RateActivity.class);
            startActivity(intent);
        });

        sendMsg.setOnClickListener(v -> {
            SharedData.stalkedUser = SharedData.currentLawyer;
            new ConversationController().getConversationsByTwoUsers(SharedData.currentUser.getKey(),
                    SharedData.currentLawyer.getKey(), new ConversationCallback() {
                        @Override
                        public void onSuccess(ArrayList<ConversationModel> conversations) {
                            if (conversations.size() > 0) {
                                loadingHelper.dismissLoading();
                                SharedData.currentConversation = conversations.get(0);
                                Intent intent = new Intent(LawyerDetailsActivity.this, ChatActivity.class);
                                startActivity(intent);
                            } else {
                                new ConversationController().newConversation(SharedData.stalkedUser, new ConversationCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<ConversationModel> conversations) {
                                        loadingHelper.dismissLoading();
                                        SharedData.currentConversation = conversations.get(0);
                                        Intent intent = new Intent(LawyerDetailsActivity.this, ChatActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFail(String error) {
                                        loadingHelper.dismissLoading();
                                        Toast.makeText(LawyerDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(LawyerDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        location.setOnClickListener(v -> {
            Intent intent = new Intent(this, LawyerNavigationActivity.class);
            startActivity(intent);
        });

        getData();
    }

    private void getData() {
        loadingHelper.showLoading("");
        new UserController().getUserByKey(SharedData.currentLawyer.getKey(), new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> lawyers) {
                loadingHelper.dismissLoading();
                if(lawyers.size() > 0) {
                    SharedData.currentLawyer = lawyers.get(0);
                    lawyer = lawyers.get(0);
                    setData();
                }else {
                    Toast.makeText(LawyerDetailsActivity.this, "error!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(LawyerDetailsActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        name.setText(lawyer.getName());
        phone.setText(lawyer.getPhone());
        email.setText(lawyer.getEmail());
        governorate.setText(lawyer.getGovernorate().getName());
        city.setText(lawyer.getCity().getName());
        description.setText(lawyer.getDescription());
        category.setText(SharedData.servicesNames.get(lawyer.getCategory()));
        price.setText(String.format("%.3f KWD", lawyer.getPrice()));

        if (!TextUtils.isEmpty(SharedData.currentLawyer.getProfileImage())) {
            Picasso.get()
                    .load(SharedData.currentLawyer.getProfileImage())
                    .into(avatar);
        }
    }
}