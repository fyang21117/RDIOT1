package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;

public class FingerprintUtil {

    private static final String ACTION_SETTING = "android.settings.SETTINGS";

    public static void openFingerPrintSettingPage(Context context) {
        //进入系统设置指纹界面
        Intent intent = new Intent(ACTION_SETTING);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();//2018.11.5
        }
    }
}
