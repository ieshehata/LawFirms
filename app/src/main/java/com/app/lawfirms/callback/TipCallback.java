package com.app.lawfirms.callback;


import com.lawfirms.android.models.TipModel;

import java.util.ArrayList;

public interface TipCallback {
    void onSuccess(ArrayList<TipModel> tips);
    void onFail(String error);
}
