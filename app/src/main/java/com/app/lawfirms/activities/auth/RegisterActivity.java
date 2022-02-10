package com.app.lawfirms.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.lawfirms.R;
import com.app.lawfirms.callback.StringCallback;
import com.app.lawfirms.callback.UserCallback;
import com.app.lawfirms.controllers.UploadController;
import com.app.lawfirms.controllers.UserController;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.NotNull;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener{
    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText name;
    @NotNull
    @NotEmpty
    @Length(min = 8)
    TextInputEditText phone;
    @NotNull
    @NotEmpty
    @Length(min = 6)
    TextInputEditText password;
    @NotNull
    @NotEmpty
    @Email
    TextInputEditText email;
    private boolean isEditing = false;
    private UserModel user = new UserModel();
    private UserModel lawyer = new UserModel();
    private static final int PICK_IMAGE = 55;
    Uri imageUri;
    LinearLayout avatarLayout;
    ImageView avatar;
    Button login,register;
    LoadingHelper loadingHelper;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        isEditing = getIntent().getExtras().getBoolean("isEditing", false);
        avatarLayout = findViewById(R.id.avatar_layout);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);



        avatar.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        register.setOnClickListener(v -> {
            validator.validate();
        });

        login.setOnClickListener(v -> onBackPressed());

        setData();
    }

    private void setData() {
        if (SharedData.userType == 2){
            if (isEditing) {
                Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");
                register.setText("Update");
                login.setVisibility(View.GONE);
                lawyer = SharedData.currentLawyer;
                name.setText(SharedData.currentLawyer.getName());
                phone.setText(SharedData.currentLawyer.getPhone());
                password.setText(SharedData.currentLawyer.getPass());
                email.setText(SharedData.currentLawyer.getEmail());
            }
        }

        if (SharedData.userType == 3){
            if (isEditing) {
                Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");
                register.setText("Update");
                login.setVisibility(View.GONE);
                assert SharedData.currentLawyer != null;
                user = SharedData.currentUser;
                name.setText((CharSequence) SharedData.currentUser.getName());
                phone.setText(SharedData.currentUser.getPhone());
                password.setText(SharedData.currentUser.getPass());
                email.setText(SharedData.currentUser.getEmail());
                if(!TextUtils.isEmpty(SharedData.currentUser.getProfileImage())) {
                    Picasso.get()
                            .load(SharedData.currentUser.getProfileImage())
                            .into(avatar);
                }
            }else {
                user = new UserModel();
                user.setState(1);
            }
        }
    }

    private boolean checkReadPermission(){
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
        }
    }

    private void pickImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
        }
    }

    @Override
    public void onValidationSucceeded() {
        if(SharedData.userType == 2) {
            if(isEditing) {
                loadingHelper.showLoading("");
                lawyer.setName(name.getText().toString());
                lawyer.setPhone(phone.getText().toString());
                lawyer.setPass(password.getText().toString());
                lawyer.setEmail(email.getText().toString());
                SharedData.currentLawyer = lawyer;

                new UserController().save(lawyer, new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> representatires) {
                        loadingHelper.dismissLoading();
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String error) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }else if(SharedData.userType == 3) {
            if(imageUri != null) {
                loadingHelper.showLoading("");
                new UploadController().uploadImage(imageUri, new StringCallback() {
                    @Override
                    public void onSuccess(String text) {
                        loadingHelper.dismissLoading();
                        user.setName(name.getText().toString());
                        user.setPhone(phone.getText().toString());
                        user.setPass(password.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setProfileImage(text);
                        user.setState(1);
                        SharedData.currentUser = user;
                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("from", 1);
                        intent.putExtra("phone", user.getPhone());
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String error) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(isEditing) {
                user.setName(name.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setPass(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.setState(1);

                SharedData.currentUser = user;
                Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                intent.putExtra("from", 3);
                intent.putExtra("phone", user.getPhone());
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "Pick your profile picture!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error: errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if( view instanceof EditText){
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}