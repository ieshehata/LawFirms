package com.app.lawfirms.activities.user.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment implements Validator.ValidationListener{

    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText  name;

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



    private UserModel user = new UserModel();
    Button register;
    LoadingHelper loadingHelper;
    private Validator validator;
    private static final int PICK_IMAGE = 55;
    private static final int PICK_PET_IMAGE = 99;

    ImageView avatar,imagePet;
    Uri imageUri, petUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        phone = view.findViewById(R.id.phone);
        register = view.findViewById(R.id.register);
        loadingHelper = new LoadingHelper(getActivity());
        validator = new Validator(this);
        validator.setValidationListener(this);

        avatar.setOnClickListener(v -> {
            if (checkReadPermission()) {
                pickImage();
            }
        });


        register.setOnClickListener(v -> {
            validator.validate();
        });
        setData();
        return view;
    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        register.setText("Update");
        assert SharedData.user != null;
        user = SharedData.user;
        name.setText(SharedData.user.getName());
        phone.setText(SharedData.user.getPhone());
        password.setText(SharedData.user.getPass());
        email.setText(SharedData.user.getEmail());

        if (!TextUtils.isEmpty(SharedData.user.getProfileImage())) {
            Picasso.get()
                    .load(SharedData.user.getProfileImage())
                    .into(avatar);
        }


    }

    private boolean checkReadPermission() {
        int permissionWriteExternal = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        }
    }

    private void pickImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            avatar.setImageURI(imageUri);}
    }

    @Override
    public void onValidationSucceeded() {
        user.setName(name.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setPass(password.getText().toString());
        user.setEmail(email.getText().toString());

        SharedData.user = user;

        if(imageUri != null) {
            loadingHelper.showLoading("");
            new UploadController().uploadImage(imageUri, new StringCallback() {
                @Override
                public void onSuccess(String text) {
                    SharedData.user.setProfileImage(text);
                    if(petUri != null) {
                        new UploadController().uploadImage(petUri, new StringCallback() {
                            @Override
                            public void onSuccess(String text) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(String error) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        new UserController().save(SharedData.user, new UserCallback() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> users) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(String error) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }

    }
}