package com.app.lawfirms.network;

import com.app.lawfirms.models.NotificationResponseModel;
import com.app.lawfirms.models.SendNotificationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIInterface {
    @POST("send")
    Call<NotificationResponseModel> sendNotification(@Body SendNotificationModel notification);
}
