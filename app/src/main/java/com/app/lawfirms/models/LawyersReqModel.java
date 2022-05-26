package com.app.lawfirms.models;

import java.util.Calendar;
import java.util.Date;

public class LawyersReqModel {
    private String key;
    private UserModel lawyer;
    private int state; // 0->Unseen, 1->Accepted, -1->Rejected
    private Date createdAt;

    public LawyersReqModel() {
    }

    public LawyersReqModel(UserModel lawyer) {
        this.lawyer = lawyer;
        this.state = 0;
        this.createdAt = Calendar.getInstance().getTime();
    }

    public LawyersReqModel(String key, UserModel lawyer, int state, Date createdAt) {
        this.key = key;
        this.lawyer = lawyer;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserModel getLawyer() {
        return lawyer;
    }

    public void setLawyer(UserModel lawyer) {
        this.lawyer = lawyer;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class  ClassNew {
        public void func() {
        }
    }

}
