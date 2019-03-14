package com.fyang21117.rdiot1.log;

import android.util.Log;

import com.fyang21117.rdiot1.BuildConfig;

public class FPLog {

    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.i("FPLog", message);//打印信息
        }
    }
}
