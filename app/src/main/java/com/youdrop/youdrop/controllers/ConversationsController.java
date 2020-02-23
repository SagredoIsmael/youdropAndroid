package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.YoudropApi;
import com.youdrop.youdrop.api.callbacks.GetConversationsCallback;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.results.ConversationsResult;
import com.youdrop.youdrop.views.ConversationActivity;
import com.youdrop.youdrop.views.publications.PublicationActivity;
import com.youdrop.youdrop.views.sections.friends.ConversationsFragment;
import com.youdrop.youdrop.views.sections.friends.ConversationsView;
import com.youdrop.youdrop.views.sections.friends.FriendsFragment;
import com.youdrop.youdrop.views.sections.friends.FriendsView;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class ConversationsController implements ConversationsFragment.OnConversationInteractionListener {

    private ConversationsView view;

    private ContentManager api;

    private String authToken;


    public ConversationsController(ConversationsView view) {
        this.view = view;
        api = ContentManager.getInstance(view.getContext());
    }

    private void getAuthToken(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
    }

    public void reload(){
        if (authToken == null) getAuthToken();
        if (authToken != null){
            api.getConversations(authToken, callback);
        } else {
            //Show empty view
        }
    }

    GetConversationsCallback callback = new GetConversationsCallback() {

        @Override
        public void onDataReceived(ConversationsResult notifications) {
            if (notifications.getItems().size() != 0) {
                view.showEmpty(false);
                view.clearList();
                view.addConversationsToList(notifications.getItems());            }
        }
    };

    @Override
    public void onConversationInteraction(Conversation item) {
        Intent i = new Intent(view.getActivity(), ConversationActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("conversation", item);
        i.putExtras(args);
        view.getActivity().startActivity(i);

    }
}
