package com.gau.simplehttp.http;

import android.util.Log;

public class Logger {

    private static boolean ENABLE = true;
    private static final String sTAG = "SimpleHttp";

    public static void d(String tag, String msg) {
        if (ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (ENABLE) {
            Log.d(sTAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (ENABLE) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg) {
        if (ENABLE) {
            Log.w(sTAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ENABLE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (ENABLE) {
            Log.e(sTAG, msg);
        }
    }


}
