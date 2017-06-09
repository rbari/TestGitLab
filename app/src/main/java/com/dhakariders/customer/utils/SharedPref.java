package com.dhakariders.customer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rezwan on 6/3/17.
 */

public class SharedPref {

    private static final String prefName = "DHAKA_RIDERS";
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
}
