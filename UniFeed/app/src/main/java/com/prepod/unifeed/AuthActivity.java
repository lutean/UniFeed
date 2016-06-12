package com.prepod.unifeed;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class AuthActivity extends FragmentActivity {

    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;

    private static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        hideBars();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                Log.i("111", "111");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("111", "111");
            }
        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.i("111", "111");
                //getInfoFb();
            }
        };

        if (AccessToken.getCurrentAccessToken()!=null){
            if (VKSdk.isLoggedIn()){
                Intent dialogIntent = new Intent(this, MainActivity.class);
                startActivity(dialogIntent);

            } else {
                VKSdk.login(this, sMyScope);
            }
        } else {

            showLogin();
        }
    }

    private void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_content_frame, new LoginFragment())
                .commit();
    }

    private void hideBars(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Intent dialogIntent = new Intent(this, MainActivity.class);
        if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            VKSdk.login(this, sMyScope);
        }
        if (requestCode == 10485) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    startActivity(dialogIntent);
                }

                @Override
                public void onError(VKError error) {
                }
            })) {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }
}
