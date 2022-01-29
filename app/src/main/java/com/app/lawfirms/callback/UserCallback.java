package com.app.lawfirms.callback;

import com.app.lawfirms.models.UserModel;

import java.util.ArrayList;

public interface UserCallback {
    void onSuccess(ArrayList<UserModel> users);

    void onFail(String error);
}




    