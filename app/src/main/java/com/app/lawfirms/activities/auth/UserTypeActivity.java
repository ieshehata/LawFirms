package com.app.lawfirms.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import com.app.lawfirms.R;
import com.app.lawfirms.utils.SharedData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        findViewById(R.id.admin).setOnClickListener(view -> {
            SharedData.userType = 1;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.lawyer).setOnClickListener(view -> {
            SharedData.userType = 2;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.user).setOnClickListener(view -> {
            SharedData.userType = 3;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });




        new GovernorateController().getGovernorates(new GovernorateCallback() {
            @Override
            public void onSuccess(ArrayList<GovernorateModel> governorates) {
                if(governorates.size() > 0) {
                    SharedData.allGovernorates = governorates;
                }else {
                    Toast.makeText(UserTypeActivity.this, "Add Governorates from admin first!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(UserTypeActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        new CityController().getCities(new CityCallback() {
            @Override
            public void onSuccess(ArrayList<CityModel> cities) {
                if(cities.size() > 0) {
                    SharedData.allCities = cities;
                }else {
                    Toast.makeText(UserTypeActivity.this, "Add Cities from admin first!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(UserTypeActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}