package com.youdrop.youdrop.views.publications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youdrop.youdrop.controllers.PublicationController;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.R;

import butterknife.ButterKnife;

public class PublicationActivity extends AppCompatActivity implements PublicationView {

    public static final String ARG_CONTENT = "content";
    public static final String ARG_PARAM2 = "location";

    PublicationController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        ButterKnife.bind(this);
        controller = new PublicationController(this);
        if (getIntent() != null) {
            Publication publication = new Publication();
            if ((publication = (Publication) getIntent().getSerializableExtra(ARG_CONTENT)) != null){
                controller.setContentId(publication.getId());
            }else{
                Intent appLinkIntent = getIntent();
                String appLinkAction = appLinkIntent.getAction();
                Uri appLinkData = appLinkIntent.getData();
                String uri = appLinkData.toString();
                String[] id = uri.split("=");
                controller.setContentId(id[1]);
            }
        }
        controller.reload();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLockedPublication(Publication publication, Location location) {
        try{
        getFragmentManager().beginTransaction().replace(R.id.fragmentPublicationContainer, PublicationLockedFragment.newInstance(publication,location)).commit();
        }catch (Exception e){

        }
        }

    @Override
    public void showUnlockedPublication(Publication publication, Location location) {
        try{
            getFragmentManager().beginTransaction().replace(R.id.fragmentPublicationContainer, PublicationFragment.newInstance(publication,location)).commit();

        }catch (Exception e){

        }
    }
}
