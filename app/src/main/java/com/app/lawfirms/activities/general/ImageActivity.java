package com.app.lawfirms.activities.general;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;

import com.app.lawfirms.R;
import com.app.lawfirms.utils.SharedData;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        PhotoView photoView = findViewById(R.id.photo_view);
        if(!TextUtils.isEmpty(SharedData.imageUrl)) {
            Picasso.get()
                    .load(SharedData.imageUrl)
                    .into(photoView);
        }else {
            onBackPressed();
        }
    }
}