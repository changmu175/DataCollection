package com.ycm.kata.datacollection.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by changmuyu on 2017/8/21.
 * Description:
 */

public class SharePreferenceUtil {
    private static SharePreferenceUtil instance;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String name;
    private SharePreferenceUtil(String name, Context context) {
        if (context == null || name == null) {
            return;
        }
        this.context = context;
        this.name = name;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtil getInstance(String name, Context context) {
        if (instance == null) {
            synchronized (SharePreferenceUtil.class) {
                if (instance == null) {
                    instance = new SharePreferenceUtil(name, context);
                }
            }
        }
        return instance;
    }

    private void getSharedPreferences(String name) {
        if (context == null) {
            return ;
        }
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return ;
    }

    public void addData(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void addData(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void addData(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public int getIntData(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public long getLongData(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public String getStringData(String key) {
        return sharedPreferences.getString(key, "");
    }
}
