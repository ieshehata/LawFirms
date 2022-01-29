package com.app.lawfirms.callback;

import com.app.lawfirms.models.CityModel;

import java.util.ArrayList;

public interface CityCallback {
    void onSuccess(ArrayList<CityModel> citys);

    void onFail(String error);
}




    