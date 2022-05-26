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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    TextInputEditText description, price;


    private AutoCompleteTextView governorate, city, category;
    private ArrayList<CityModel> currentCities = new ArrayList<>();

    private ArrayList<String> governoratesNames = new ArrayList<>();
    private ArrayList<String> citiesNames = new ArrayList<>();
    private ArrayList<String> categoriesNames = new ArrayList<>();
    private int chosenCategory;
    private GovernorateModel chosenGovernorate;
    private CityModel chosenCity;

    private boolean isEditing = false;
    private UserModel user = new UserModel();
    private UserModel lawyer = new UserModel();
    private static final int PICK_IMAGE = 55;
    Uri imageUri;
    LinearLayout avatarLayout;
    ImageView avatar;
    Button login, register, location;
    LoadingHelper loadingHelper;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        isEditing = getIntent().getExtras().getBoolean("isEditing", false);
        governorate = findViewById(R.id.governorate);
        city = findViewById(R.id.city);
        category = findViewById(R.id.category);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        avatarLayout = findViewById(R.id.avatar_layout);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        location = findViewById(R.id.location);
        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);


        initDropdowns();

        avatar.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        register.setOnClickListener(v -> {
            validator.validate();
        });

        location.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, PlacePickerActivity.class);
            SharedData.latitude = (SharedData.currentLawyer == null || SharedData.currentLawyer.getLatitude() == null) ? 29.282478 : SharedData.currentLawyer.getLatitude();
            SharedData.longitude = (SharedData.currentLawyer == null || SharedData.currentLawyer.getLongitude() == null) ? 47.912792 : SharedData.currentLawyer.getLongitude();
            startActivityForResult(intent,2);
        });

        login.setOnClickListener(v -> onBackPressed());

        setData();
    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        if (SharedData.userType == 2){
            location.setVisibility(View.VISIBLE);
            if (isEditing) {
                Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");
                register.setText("Update");
                login.setVisibility(View.GONE);
                assert SharedData.currentLawyer != null;
                lawyer = SharedData.currentLawyer;
                name.setText((CharSequence) SharedData.currentLawyer.getName());
                phone.setText(SharedData.currentLawyer.getPhone());
                password.setText(SharedData.currentLawyer.getPass());
                email.setText(SharedData.currentLawyer.getEmail());
                description.setText(SharedData.currentLawyer.getDescription());
                price.setText(String.format("%.3f",SharedData.currentLawyer.getPrice()));

                chosenCategory = SharedData.currentLawyer.getCategory();
                category.getOnItemClickListener().onItemClick(null, null, chosenCategory, chosenCategory);
                category.setText(SharedData.servicesNames.get(chosenCategory));

                chosenGovernorate = SharedData.currentLawyer.getGovernorate();
                governorate.setText(chosenGovernorate.getName());

                chosenCity = SharedData.currentLawyer.getCity();
                city.setText(chosenCity.getName());

                if(!TextUtils.isEmpty(SharedData.currentLawyer.getProfileImage())) {
                    Picasso.get()
                            .load(SharedData.currentLawyer.getProfileImage())
                            .into(avatar);
                }
            }else {
                lawyer = new UserModel();
                lawyer.setUserType(2);
            }
        }else if (SharedData.userType == 3){
            location.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            price.setVisibility(View.GONE);

            if (isEditing) {
                Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");
                register.setText("Update");
                login.setVisibility(View.GONE);
                assert SharedData.currentUser != null;
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
                user.setUserType(3);
                user.setState(1);
                user.setActivated(1);
            }
        }
    }

    private void initDropdowns() {
        if(SharedData.userType == 2) {
            categoriesNames = SharedData.servicesNames;

            ArrayAdapter categoryAdapter = new ArrayAdapter(this, R.layout.list_item, categoriesNames);
            category.setAdapter(categoryAdapter);
            category.setOnItemClickListener(((parent, view, position, id) -> {
                chosenCategory = position;
            }));

            if(SharedData.allGovernorates.size() > 0) {
                governoratesNames = new ArrayList<>();
                for(GovernorateModel gov : SharedData.allGovernorates) {
                    governoratesNames.add(gov.getName());
                }

                ArrayAdapter govAdapter = new ArrayAdapter(RegisterActivity.this, R.layout.list_item, governoratesNames);
                governorate.setAdapter(govAdapter);
                governorate.setOnItemClickListener(((parent, view, position, id) -> {
                    chosenGovernorate = SharedData.allGovernorates.get(position);

                    if(SharedData.allCities.size() > 0) {
                        citiesNames = new ArrayList<>();
                        for(CityModel city : SharedData.allCities) {
                            if(chosenGovernorate != null && chosenGovernorate.getKey() != null && city.getGovernorateKey().equals(chosenGovernorate.getKey())) {
                                currentCities.add(city);
                                citiesNames.add(city.getName());
                            }
                        }

                        ArrayAdapter cityAdapter = new ArrayAdapter(RegisterActivity.this, R.layout.list_item, citiesNames);
                        city.setAdapter(cityAdapter);
                        city.setOnItemClickListener(((parentC, viewC, positionC, idC) -> {
                            chosenCity = currentCities.get(positionC);
                        }));
                    }else {
                        Toast.makeText(RegisterActivity.this, "Add Cities from admin first!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }));
            }else {
                Toast.makeText(RegisterActivity.this, "Add Governorates from admin first!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

        }else {
            findViewById(R.id.governorate_field).setVisibility(View.GONE);
            findViewById(R.id.city_field).setVisibility(View.GONE);
            findViewById(R.id.category_field).setVisibility(View.GONE);

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
        if(resultCode == RESULT_OK && requestCode == 2){
            lawyer.setLatitude(SharedData.currentLatLng.getLatitude());
            lawyer.setLongitude(SharedData.currentLatLng.getLongitude());
        }else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
        }
    }

    @Override
    public void onValidationSucceeded() {


        if(SharedData.userType == 2) { //lawyer
            if(lawyer.getLatitude() == null || lawyer.getLongitude() == null) {
                Toast.makeText(RegisterActivity.this, "place your location first!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (chosenGovernorate == null) {
                Toast.makeText(this, "Choose Governorate first!", Toast.LENGTH_LONG).show();
                return;
            }

            if (chosenCity == null) {
                Toast.makeText(this, "Choose City first!", Toast.LENGTH_LONG).show();
                return;
            }

            double priceValue = 0;
            try {
                priceValue = Double.parseDouble(price.getText().toString());
            }catch (Exception ignored) {
                Toast.makeText(this, "Enter Valid price for consultant!", Toast.LENGTH_LONG).show();
                return;
            }

            if(imageUri != null) {
                loadingHelper.showLoading("");
                double finalPriceValue = priceValue;
                new UploadController().uploadImage(imageUri, new StringCallback() {
                    @Override
                    public void onSuccess(String text) {
                        loadingHelper.dismissLoading();
                        lawyer.setName(name.getText().toString());
                        lawyer.setPhone(phone.getText().toString());
                        lawyer.setPass(password.getText().toString());
                        lawyer.setEmail(email.getText().toString());
                        lawyer.setDescription(description.getText().toString());
                        lawyer.setCategory(chosenCategory);
                        lawyer.setGovernorate(chosenGovernorate);
                        lawyer.setCity(chosenCity);
                        lawyer.setProfileImage(text);
                        lawyer.setPrice(finalPriceValue);
                        SharedData.currentLawyer = lawyer;
                        new UserController().save(lawyer, new UserCallback() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> representatires) {
                                if(!isEditing) {
                                    new LawyerReqController().save(new LawyersReqModel(lawyer), new LawyerReqCallback() {
                                        @Override
                                        public void onSuccess(ArrayList<LawyersReqModel> lawyerReqs) {
                                            loadingHelper.dismissLoading();
                                            Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                            onBackPressed();
                                        }

                                        @Override
                                        public void onFail(String error) {
                                            loadingHelper.dismissLoading();
                                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    loadingHelper.dismissLoading();
                                    Toast.makeText(RegisterActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            }

                            @Override
                            public void onFail(String error) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFail(String error) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(isEditing) {
                loadingHelper.showLoading("");
                lawyer.setName(name.getText().toString());
                lawyer.setPhone(phone.getText().toString());
                lawyer.setPass(password.getText().toString());
                lawyer.setEmail(email.getText().toString());
                lawyer.setDescription(description.getText().toString());
                lawyer.setCategory(chosenCategory);
                lawyer.setGovernorate(chosenGovernorate);
                lawyer.setCity(chosenCity);
                lawyer.setPrice(priceValue);
                SharedData.currentLawyer = lawyer;

                new UserController().save(lawyer, new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> representatires) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String error) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                Toast.makeText(RegisterActivity.this, "Pick your profile picture!", Toast.LENGTH_SHORT).show();
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
                        user.setActivated(1);

                        SharedData.currentUser = user;
                        new UserController().save(user, new UserCallback() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> representatires) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(RegisterActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }

                            @Override
                            public void onFail(String error) {
                                loadingHelper.dismissLoading();
                                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
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

                new UserController().save(user, new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> representatires) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String error) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
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