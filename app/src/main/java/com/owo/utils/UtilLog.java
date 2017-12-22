package com.owo.utils;

import android.util.Log;

/**
 * @author XQF
 * @created 2017/5/4
 */
public class UtilLog {

    private static boolean state = true;

    public static void v(String tag, String str) {
        if (state) {
            Log.v(tag, str);
        }
    }
    public static void d(String tag, String str) {
        if (state) {
            Log.d(tag, str);
        }
    }
    public static void i(String tag, String str) {
        if (state) {
            Log.i(tag, str);
        }
    }
    public static void w(String tag, String str) {
        if (state) {
            Log.w(tag, str);
        }
    }
    public static void e(String tag, String str) {
        if (state) {
            Log.e(tag, str);
        }
    }
 
}
