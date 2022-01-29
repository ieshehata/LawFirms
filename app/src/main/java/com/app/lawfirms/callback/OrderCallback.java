package com.app.lawfirms.callback;

import com.app.lawfirms.models.OrderModel;

import java.util.ArrayList;

public interface OrderCallback {
    void onSuccess(ArrayList<OrderModel> orders);

    void onFail(String error);
}




    