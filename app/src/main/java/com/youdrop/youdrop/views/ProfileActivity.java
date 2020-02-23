package com.youdrop.youdrop.views;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.youdrop.youdrop.controllers.MainController;
import com.youdrop.youdrop.controllers.ProfileActivityController;
import com.youdrop.youdrop.data.Connection;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.views.sections.ProfileFragment;
import com.youdrop.youdrop.R;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, ProfileActivityView {

    String userId = null;

    public static final String ARG_USERID = "userId";

    private ProfileActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        controller = new ProfileActivityController(this);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra(ARG_USERID);
            controller.loadFriendshipOptions(userId);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentProfileContainer, ProfileFragment.newInstance(userId)).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!controller.showFriendsButton()) {
            menu.removeItem(R.id.action_friendship);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_friendship:
                controller.showFriendshipOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showFriendship(Friendship friendship, Connection connection) {
        CharSequence options[] = controller.getFriendshipOptions();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_friendship);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.setFriendshipOptionClicked(which);
            }
        });
        builder.show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
