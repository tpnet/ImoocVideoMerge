package com.tpnet.imoocvideomerge.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by litp on 2017/5/18.
 */

public class DataHelper {
    private static SharedPreferences mSharedPreferences;


    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void SetStringSF(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static String getStringSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences.getString(key, null);
    }
}
