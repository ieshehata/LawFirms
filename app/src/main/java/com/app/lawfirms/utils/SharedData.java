package com.app.lawfirms.utils;

import com.app.lawfirms.models.CityModel;
import com.app.lawfirms.models.ConversationModel;
import com.app.lawfirms.models.GovernorateModel;
import com.app.lawfirms.models.OrderModel;
import com.app.lawfirms.models.RateModel;
import com.app.lawfirms.models.UserHeaderModel;
import com.app.lawfirms.models.UserModel;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class SharedData {
    public static int userType = 2; // 1->Admin, 2->lawyer, 3->user
    public static int state =0 ;//-2=Blocked,-1=Rejected 0=Waiting, 1=Accepted , 2= warning

    public static LatLng currentLatLng;

    public static UserModel adminUser = new UserModel(true);
    public static UserModel currentUser;
    public static UserModel currentLawyer;
    public static int currentCategory;
    public static TipModel currentTip = new TipModel();

    public static UserModel stalkedUser;
    public static UserHeaderModel currentUserHeader;
    public static UserHeaderModel stalkedUserHeader;
    public static ConversationModel currentConversation;

    public static String format = "EEE, d MMM yyyy hh:mm a";
    public static String formatTime = "hh:mm a";
    public static String formatDate = "dd/MM/yyyy";
    public static String formatDateTime = "dd/MM/yyyy hh:mm a";
    public static String imageUrl;
    public static Uri imageUri;
    public static final int NOTIFICATION_ID = 1303;
    public static final String PREF_KEY = "login";
    public static final String IS_USER_SAVED = "SAVED_USER";
    public static final String PHONE = "PHONE";
    public static final String PASS = "PASS";
    public static OrderModel currentOrder;
    public static double longitude;
    public static double latitude;
    public static OrderModel currentTime;
    public static RateModel currentRate;
    public static ArrayList<GovernorateModel> allGovernorates = new ArrayList<>();
    public static ArrayList<CityModel> allCities = new ArrayList<>();
    public static final String USER_TYPE = "USER_TYPE";

    public static final ArrayList<String> servicesNames = new ArrayList<>(
            Arrays.asList("Personal Status",
                    "Criminal",
                    "Commercial",
                    "Public",
                    "International",
                    "Financial",
                    "Real estate",
                    "Labor",
                    "Administrative",
                    "Civil"));
}


