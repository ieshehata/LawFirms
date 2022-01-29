package com.app.lawfirms.callback;

import com.app.lawfirms.models.LawyersReqModel;

import java.util.ArrayList;

public interface LawyerReqCallback {
    void onSuccess(ArrayList<LawyersReqModel> lawyerReqs);

    void onFail(String error);
}




    