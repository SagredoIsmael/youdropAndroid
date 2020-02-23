package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.youdrop.youdrop.api.YoudropApi;
import com.youdrop.youdrop.api.callbacks.FBLoginCallback;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.LoginView;

/**
 * Created by pau on 16/09/17.
 */

public class LoginController {

    private LoginView view;

    private boolean accepted = false;

    private final static String URL = "http://you-drop.com/legal";

    private YoudropApi api;

    CallbackManager callbackManager;

    public LoginController(LoginView view) {
        this.view = view;
        view.disableLoginButton();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,callback);
        api = YoudropApi.getInstance();
        legalAcceptButtonClicked(true);
    }

    public void loginButtonClicked(){

    }

    public void legalButtonClicked(){
        view.openBrowser(URL);
    }

    public void legalAcceptButtonClicked(boolean value){
        accepted = value;
        if (accepted){
            view.enableLoginButton();
        }else {
            view.disableLoginButton();
        }
    }

    public void fakeLogin(){
        api.fakeLogin( loginCallback);
    }

    FacebookCallback callback =new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            String fbauth = loginResult.getAccessToken().getToken();
            Log.w("", loginResult.toString());
            api.facebookLogin(fbauth, loginCallback);
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Log.e("", exception.toString());
        }
    };

    FBLoginCallback loginCallback = new FBLoginCallback() {
        @Override
        public void onSuccess(User session) {
            if (session.getAuthToken() != null){
                SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("authToken", session.getAuthToken());
                editor.putString("userId", session.getId());
                editor.commit();
                view.gotoMain();
            }
        }
    };

    public void manageIntentFilter(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
