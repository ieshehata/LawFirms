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
    TextInputEditText name,description;
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
        description = findViewById(R.id.description);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        if(SharedData.userType==2){
            avatarLayout.setVisibility(View.GONE);
            description.setVisibility(View.VISIBLE);

        }

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
                user = SharedData.currentUser;
                name.setText(SharedData.currentUser.getName());
                phone.setText(SharedData.currentUser.getPhone());
                password.setText(SharedData.currentUser.getPass());
                email.setText(SharedData.currentUser.getEmail());
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
                user.setName(name.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setPass(password.getText().toString());
                user.setEmail(email.getText().toString());
                SharedData.currentUser = user;

                new UserController().save(user, new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> users) {
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