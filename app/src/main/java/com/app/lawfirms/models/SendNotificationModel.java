package com.app.lawfirms.models;

public class SendNotificationModel {
    private String to;
    private NotificationModel data;

    public SendNotificationModel() {
    }

    public SendNotificationModel(String to, NotificationModel data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationModel getData() {
        return data;
    }

    public void setData(NotificationModel data) {
        this.data = data;
    }
}
