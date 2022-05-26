package com.app.lawfirms.activities.admin;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;


public class TipEditorActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 55;

    LinearLayout inLayout, outLayout;
    ImageView imageIn, imageOut;
    EditText titleET, textET;
    TextView title, text;
    Button save;
    LoadingHelper loadingHelper;
    Uri imageUri;
    boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_editor);
        isNew = getIntent().getBooleanExtra("isNew", false);
        inLayout = findViewById(R.id.in);
        outLayout = findViewById(R.id.out);
        imageIn = findViewById(R.id.image_in);
        imageOut = findViewById(R.id.image_out);
        titleET = findViewById(R.id.title_et);
        textET = findViewById(R.id.text_et);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        save = findViewById(R.id.save);
        loadingHelper = new LoadingHelper(this);


        imageIn.setOnClickListener(v -> {
            if (checkReadPermission()) {
                pickImage();
            }
        });
        
        title.setText(SharedData.currentTip.getTitle());
        text.setText(SharedData.currentTip.getText());
        titleET.setText(SharedData.currentTip.getTitle());
        textET.setText(SharedData.currentTip.getText());

        if (!TextUtils.isEmpty(SharedData.currentTip.getImage())) {
            Picasso.get()
                    .load(SharedData.currentTip.getImage())
                    .into(imageIn);

            Picasso.get()
                    .load(SharedData.currentTip.getImage())
                    .into(imageOut);
        }
        
        if(SharedData.userType == 1) {
            inLayout.setVisibility(View.VISIBLE);
            outLayout.setVisibility(View.GONE);
            setTitle("Tip Editor");
        }else {
            inLayout.setVisibility(View.GONE);
            outLayout.setVisibility(View.VISIBLE);
            setTitle("Tip Details");
        }


        save.setOnClickListener(v -> {
            if(!titleET.getText().toString().trim().isEmpty() && !textET.getText().toString().trim().isEmpty()) {
                loadingHelper.showLoading("");
                if(imageUri != null) {
                    uploadImage(new StringCallback() {
                        @Override
                        public void onSuccess(String text) {
                            uploadData(text);
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(TipEditorActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                } else if(!isNew) {
                    uploadData("");
                } else {
                    Toast.makeText(this, "Pick image please!", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, "Enter all the data please!\nImage, Title, Text", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadData(String imageUrl) {
        if(isNew) {
            new TipController().newTip(imageUrl, titleET.getText().toString(), textET.getText().toString(),  new TipCallback() {
                @Override
                public void onSuccess(ArrayList<TipModel> tips) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(TipEditorActivity.this, "Added", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(TipEditorActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            SharedData.currentTip.setTitle(titleET.getText().toString());
            SharedData.currentTip.setText(textET.getText().toString());
            SharedData.currentTip.setImage(imageUrl);
            new TipController().save(SharedData.currentTip, new TipCallback() {
                @Override
                public void onSuccess(ArrayList<TipModel> tips) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(TipEditorActivity.this, "Saved", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(TipEditorActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void uploadImage(StringCallback callback) {
        new UploadController().uploadImage(imageUri, callback);
    }
    private boolean checkReadPermission() {
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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
            imageIn.setImageURI(imageUri);
        }
    }
}