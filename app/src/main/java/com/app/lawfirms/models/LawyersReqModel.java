package com.app.lawfirms.models;

import java.util.Date;

public class LawyersReqModel {
    private String key;
    private UserModel lawyerer;
    private int state; // 0->Unseen, 1->Accepted, -1->Rejected
    private Date createdAt;

    public LawyersReqModel() {
    }

    public LawyersReqModel(String key, UserModel lawyerer, int state, Date createdAt) {
        this.key = key;
        this.lawyerer = lawyerer;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserModel getLawyerer() {
        return lawyerer;
    }

    public void setLawyerer(UserModel lawyerer) {
        this.lawyerer = lawyerer;
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
