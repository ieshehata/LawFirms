package com.app.lawfirms.callback;

import com.app.lawfirms.models.ChatModel;

import java.util.ArrayList;

public interface ChatCallback {
    void onSuccess(ArrayList<ChatModel> chats);

    void onFail(String error);
}




    