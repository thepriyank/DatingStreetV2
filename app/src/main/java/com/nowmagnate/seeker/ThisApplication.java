package com.nowmagnate.seeker;

import android.app.Application;

public class ThisApplication extends Application {

    public static String PlayStore_URL = "https://play.google.com/store/apps/details?id=com.nowmagnate.seeker";
    @Override
    public void onCreate() {
        super.onCreate();
//        MobileAds.initialize(this,getString(R.string.admob_app_id));
    }
}
