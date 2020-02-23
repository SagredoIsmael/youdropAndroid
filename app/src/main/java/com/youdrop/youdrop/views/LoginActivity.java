package com.youdrop.youdrop.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.youdrop.youdrop.controllers.LoginController;
import com.youdrop.youdrop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginController controller;

    @BindView(R.id.fullscreen_content) View mContentView;
    @BindView(R.id.login_button) LoginButton loginButton;
    @BindView(R.id.legal_accept)
    TextView legalAccept;
    @BindView(R.id.legal_button) Button legalButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        controller = new LoginController(this);
        try{
            loginButton.clearPermissions();
        }catch (Exception e){

        }
        hide();
    }

    private void hide(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @OnClick(R.id.login_button)
    public void loginButtonClicked(LoginButton view) {
        // call login
        controller.loginButtonClicked();
        Log.d("","Login click");

    }



    @OnClick(R.id.fake_login_button)
    public void fakeLoginButtonCLicked(View view) {
        // call login
        controller.fakeLogin();

    }
    @OnClick(R.id.legal_button)
    public void legalButtonCLicked(Button view) {
        // call login
        controller.legalButtonClicked();

    }

    @Override
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        loginButton.setEnabled(false);
    }

    @Override
    public void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.manageIntentFilter(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void gotoMain() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent("finish");
        sendBroadcast(intent);
        finish();
    }
}
