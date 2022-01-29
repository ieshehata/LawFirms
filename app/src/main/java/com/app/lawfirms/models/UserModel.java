package com.app.lawfirms.models;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Date;

public class UserModel {
        private String key;
        private String name = "";
        private String email;
        private String phone;
        private String pass;
        private String age;
        private int gender;
        private String level;
        private Double latitude;
        private Double longitude;
        private double price = 0.0;
        private int state; //-1>Blocked, 0->waiting, 1->Allowed
        private GovernorateModel governorate = new GovernorateModel();
        private CityModel city = new CityModel();
        private String profileImage = "";
        private int activated; // -1->Blocked, 0->waiting, 1->Active
        private int userType = 0;
        private Date createdAt;
        private boolean isPublic = true;
        private String fcmToken = "";


        public UserModel() {
    }

    public UserModel(String key, String name, String email, String phone, String pass, String age, int gender, String level, Double latitude, Double longitude, double price, int state, GovernorateModel governorate, CityModel city, String profileImage, int activated, int userType, Date createdAt, boolean isPublic, String fcmToken) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.age = age;
        this.gender = gender;
        this.level = level;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.state = state;
        this.governorate = governorate;
        this.city = city;
        this.profileImage = profileImage;
        this.activated = activated;
        this.userType = userType;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.fcmToken = fcmToken;
    }

    public UserModel(String phone, String password) {
        this.phone = phone;
        this.pass = password;
    }

    public UserModel(boolean isAdmin) {
        this.key = "Admin";
        this.name = "Admin";
        this.email = "Admin";
        this.userType = 1;
    }

    public UserHeaderModel toHeader() {
        return new UserHeaderModel(this);
    }

    public UserModel(String phone, String pass, int userType) {
        this.phone = phone;
        this.pass = pass;
        this.userType = userType;
    }


    public boolean validate() {
        return  (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(phone);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public GovernorateModel getGovernorate() {
        return governorate;
    }

    public void setGovernorate(GovernorateModel governorate) {
        this.governorate = governorate;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }



    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
