package com.app.lawfirms.models;

import java.util.ArrayList;
import java.util.Date;

public class OrderModel {
    private String key = "";
    private UserModel user;
    private UserModel lawyer;
    private double totalPrice;
    private int state;//1->booked, 2->maintenance, 0->waiting
    private ArrayList<Date> days = new ArrayList<>();
    private String description = "";
    private Date createdAt;

    public OrderModel() {
    }

    public OrderModel(String key, UserModel user, UserModel lawyer, double totalPrice, int state, ArrayList<Date> days, String description, Date createdAt) {
        this.key = key;
        this.user = user;
        this.lawyer = lawyer;
        this.totalPrice = totalPrice;
        this.state = state;
        this.days = days;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getLawyer() {
        return lawyer;
    }

    public void setLawyer(UserModel lawyer) {
        this.lawyer = lawyer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ArrayList<Date> getDays() {
        return days;
    }

    public void setDays(ArrayList<Date> days) {
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}


