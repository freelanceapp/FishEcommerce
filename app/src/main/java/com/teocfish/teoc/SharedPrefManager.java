package com.teocfish.teoc;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SHARED_PREF = "userData";
    public static void saveUserData(Context context, String key, String value)
    {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getSavedUserData(Context context, String key)
    {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        return pref.getString(key, "");
    }

}
