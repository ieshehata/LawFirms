package com.app.lawfirms.callback;

import com.app.lawfirms.models.ConversationModel;

import java.util.ArrayList;

public interface ConversationCallback {
    void onSuccess(ArrayList<ConversationModel> conversations);

    void onFail(String error);
}




    