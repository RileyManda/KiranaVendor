package com.riley.kiranavendor.base;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String TAG = "Prefs";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREF_NAME = "Welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public Prefs(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();

    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}