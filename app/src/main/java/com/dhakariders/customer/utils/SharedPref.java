package com.dhakariders.customer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rezwan on 6/3/17.
 */

public class SharedPref {

    private static final String prefName = "DHAKA_RIDERS";
    public static final String BASE_URL = "http://ec2-35-163-88-251.us-west-2.compute.amazonaws.com/api/v1/";
    //public static final String BASE_URL = "http://192.168.21.101:9000/api/v1/";

    private static SharedPreferences pref;

    public static SharedPreferences SharedPref(Context context){
        if(pref == null){
           pref = context.getApplicationContext().getSharedPreferences(prefName , Context.MODE_PRIVATE);
        }
        return pref;
    }

    public static SharedPreferences.Editor Editor(Context context){
        return SharedPref(context).edit();
    }


    private static final String isLoggedIn = "IS_LOGGED_IN";
    public static boolean isLoggedIn(Context context){
        return SharedPref(context).getBoolean(isLoggedIn, false);
    }
    public static void setIsLoggedIn(Context context, Boolean isLoggedIn){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putBoolean(SharedPref.isLoggedIn, isLoggedIn);
        editor.apply();
    }


    private static final String userName = "USER_NAME";
    private static final String defaultUserName = "User Name";
    public static String getUserName(Context context){
        return SharedPref(context).getString(userName, defaultUserName);
    }
    public static void setUserName(Context context, String userName){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putString(SharedPref.userName, userName);
        editor.apply();
    }

    private static final String password = "PASSWORD";
    public static String getPassword(Context context){
        return SharedPref(context).getString(password, "12345678");
    }
    public static void setPassword(Context context, String password){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putString(SharedPref.password, password);
        editor.apply();
    }

    private static final String sessionId = "SESSION_ID";
    public static String getSessionId(Context context){
        return SharedPref(context).getString(sessionId, "NULL");
    }
    public static void setSessionId(Context context, String sessionId){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putString(SharedPref.sessionId, sessionId);
        editor.apply();
    }


    private static final String isOrderActive = "IS_ORDER_ACTIVE";
    public static void setHasAnActiveOrder(Context context, boolean hasAnActiveOrder){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putBoolean(SharedPref.isOrderActive, hasAnActiveOrder);
        editor.apply();
    }
    public static boolean isOrderActive(Context context){
        return SharedPref(context).getBoolean(isOrderActive, false);
    }


    private static final String orderID = "ORDER_ID";
    public static String getOrderID(Context context){
        return SharedPref(context).getString(orderID, "NULL");
    }
    public static void setOrderID(Context context, String orderID){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putString(SharedPref.orderID, orderID);
        editor.apply();
    }

    private static final String driverDetails = "DRIVER_DETAILS";
    public static void setDriverDetails(Context context, String driverDetails){
        SharedPreferences.Editor editor  =  Editor(context);
        editor.putString(SharedPref.driverDetails, driverDetails);
        editor.apply();
    }
    public static String getDriverDetails(Context context){
        return SharedPref(context).getString(driverDetails, "NULL");
    }

}
