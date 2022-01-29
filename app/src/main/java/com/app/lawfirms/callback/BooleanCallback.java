package com.app.lawfirms.callback;

public interface BooleanCallback {
    void onSuccess(boolean bool);

    void onFail(String error);
}
