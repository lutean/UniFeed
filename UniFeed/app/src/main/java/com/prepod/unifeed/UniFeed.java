package com.prepod.unifeed;

import android.app.Application;

import com.facebook.FacebookSdk;

public class UniFeed extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}
