package com.app.lawfirms.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import com.app.lawfirms.R;
import com.app.lawfirms.utils.SharedData;

import android.content.Intent;
import android.os.Bundle;

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
    }
}