package com.app.lawfirms.callback;

import java.util.ArrayList;

public interface TrustedLabelsCallback {
    void onSuccess(ArrayList<String> list);

    void onFail(String error);
}




    