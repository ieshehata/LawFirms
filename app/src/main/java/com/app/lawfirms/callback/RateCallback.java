package com.app.lawfirms.callback;

import com.app.lawfirms.models.RateModel;

import java.util.ArrayList;

public interface RateCallback {
    void onSuccess(ArrayList<RateModel> rates);

    void onFail(String error);
}




    