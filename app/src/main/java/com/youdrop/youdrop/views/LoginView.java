package com.youdrop.youdrop.views;

import android.content.Context;

/**
 * Created by pau on 16/09/17.
 */

public interface LoginView {

    void enableLoginButton();
    void disableLoginButton();
    void openBrowser(String url);
    void gotoMain();
    Context getContext();
}
