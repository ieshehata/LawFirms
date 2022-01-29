package com.app.lawfirms.callback;

import java.util.ArrayList;

public interface StringListCallback {
    void onSuccess(ArrayList<String> list);

    void onFail(String error);
}




    