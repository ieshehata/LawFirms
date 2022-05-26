package com.app.lawfirms.activities.general;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.lawfirms.android.R;
import com.lawfirms.android.callbacks.ImageLabelsCallback;
import com.lawfirms.android.callbacks.StringCallback;
import com.lawfirms.android.callbacks.TrustedLabelsCallback;
import com.lawfirms.android.controllers.ImageLabelsController;
import com.lawfirms.android.controllers.TrustedLabelsController;
import com.lawfirms.android.controllers.UploadController;
import com.lawfirms.android.models.ImageLabelsModel;
import com.lawfirms.android.utils.LoadingHelper;
import com.lawfirms.android.utils.SharedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageCheckerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 55;

    Uri imageUri;
    TextView textView;
    ImageView image;
    Button save;

    private FirebaseFunctions mFunctions;

    LoadingHelper loadingHelper;

    ArrayList<String> trustedLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_checker);
        setTitle("Add Image");
        textView = findViewById(R.id.text);
        image = findViewById(R.id.image);
        save = findViewById(R.id.save);
        loadingHelper = new LoadingHelper(this);

        image.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        save.setOnClickListener(v -> {
            SharedData.imageUri = imageUri;
            onBackPressed();
        });
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
            image.setImageURI(imageUri);


            save.setEnabled(false);
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightGray)));
            loadingHelper.showLoading("");
            new TrustedLabelsController().getLabels(new TrustedLabelsCallback() {
                @Override
                public void onSuccess(ArrayList<String> list) {
                    trustedLabels = list;
                    labelImages(imageUri);
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }


    private void labelImages(Uri uri) {
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(this, uri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ImageLabelerOptions options =
                new ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build();

        // [START get_detector_options]
        ImageLabeler labeler = ImageLabeling.getClient(options);

        Task<List<ImageLabel>> result =
                labeler.process(image)
                        .addOnSuccessListener(
                                labels -> {
                                    loadingHelper.dismissLoading();
                                    saveLabels(labels);
                                    for (ImageLabel label : labels) {
                                        String text = label.getText();
                                        for(String trustedLabel : trustedLabels) {
                                            if(text.toLowerCase().contains(trustedLabel.toLowerCase())) {
                                                textView.setText("Accepted Image");
                                                save.setEnabled(true);
                                                save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                                            }
                                        }
                                    }

                                    if(!save.isEnabled()) {
                                        textView.setText("Not Accepted Image!");
                                    }
                                })
                        .addOnFailureListener(
                                e -> {
                                    textView.setText("Failed");
                                });
    }

    private void saveLabels(List<ImageLabel> labels) {
        ImageLabelsModel imageModel = new ImageLabelsModel();

        for (ImageLabel label : labels) {
            ImageLabelsModel.LabelModel labelModel = new ImageLabelsModel.LabelModel();
            labelModel.setText(label.getText());
            labelModel.setConfidence(label.getConfidence());

            imageModel.getLabels().add(labelModel);
        }
        if(imageUri != null) {
            new UploadController().uploadImage(imageUri, new StringCallback() {
                @Override
                public void onSuccess(String text) {
                    imageModel.setImage(text);
                    new ImageLabelsController().save(imageModel, new ImageLabelsCallback() {
                        @Override
                        public void onSuccess(ArrayList<ImageLabelsModel> list) {

                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });
                }

                @Override
                public void onFail(String error) {

                }
            });
        }

    }

    private void recognizeText(Uri uri) {
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(this, uri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // [START get_detector_default]
        TextRecognizerOptions options = new TextRecognizerOptions.Builder()
        .build();
        TextRecognizer recognizer = TextRecognition.getClient(options);
        // [END get_detector_default]

        // [START run_detector]
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                for (Text.TextBlock block : visionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();
                                    textView.setText(text);

                                    for (Text.Line line: block.getLines()) {
                                        // ...
                                        for (Text.Element element: line.getElements()) {
                                            // ...
                                        }
                                    }
                                }
                                // [END get_text]
                                // [END_EXCLUDE]
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        textView.setText("Failed");

                                        // Task failed with an exception
                                        // ...
                                    }
                                });
        // [END run_detector]
    }

    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
        // [END mlkit_process_text_block]
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                });
    }
}