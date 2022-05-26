package com.app.lawfirms.callback;

import com.lawfirms.android.models.ImageLabelsModel;

import java.util.ArrayList;

public interface ImageLabelsCallback {
    void onSuccess(ArrayList<ImageLabelsModel> list);

    void onFail(String error);
}




    