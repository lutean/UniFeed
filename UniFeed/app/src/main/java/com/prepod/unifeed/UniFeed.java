package com.prepod.unifeed;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.vk.sdk.VKSdk;

public class UniFeed extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        VKSdk.initialize(this);

    }
}
