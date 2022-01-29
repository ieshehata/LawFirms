package com.app.lawfirms.callback;

import com.app.lawfirms.models.GovernorateModel;

import java.util.ArrayList;

public interface GovernorateCallback {
    void onSuccess(ArrayList<GovernorateModel> governorates);

    void onFail(String error);
}




    