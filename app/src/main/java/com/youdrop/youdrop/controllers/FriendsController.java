package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.YoudropApi;
import com.youdrop.youdrop.api.callbacks.ConversationCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipsCallback;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.results.FriendshipsResult;
import com.youdrop.youdrop.data.results.UsersResult;
import com.youdrop.youdrop.views.ConversationActivity;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.FriendRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.views.publications.PublicationActivity;
import com.youdrop.youdrop.views.sections.friends.FriendsFragment;
import com.youdrop.youdrop.views.sections.friends.FriendsView;
import com.youdrop.youdrop.views.sections.notifications.PrivateNotificationsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class FriendsController implements FriendRecyclerViewAdapter.OnUserInteractionListener {

    private FriendsView view;

    private ContentManager api;

    private String authToken;


    public FriendsController(FriendsView view) {
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
            view.clearList();
            api.getFriends(authToken, callback);
            api.getFriendshipRequests(authToken, friendshipsCallback);
        } else {
            //Show empty view
        }
    }

    GetFriendshipsCallback friendshipsCallback = new GetFriendshipsCallback() {
        @Override
        public void onDataReceived(FriendshipsResult result) {
            List<User> items = new ArrayList<>();

            for (Friendship f: result.getItems()){
                items.add(f.getUser());
            }
            if (items.size() != 0) {
                view.showEmpty(false);
                view.addUsersToList(items);
            }
        }
    };

    GetUsersCallback callback = new GetUsersCallback() {
        @Override
        public void onDataReceived(UsersResult result) {

            if (result.getItems().size() != 0) {
                view.showEmpty(false);
                view.addUsersToList(result.getItems());
            }
        }
    };


    @Override
    public void onUserInteraction(User item) {
            Intent i = new Intent(view.getActivity(), ProfileActivity.class);
            Bundle args = new Bundle();
            args.putString(ProfileActivity.ARG_USERID, item.getId());
            i.putExtras(args);
            view.getActivity().startActivity(i);


    }

    @Override
    public void onAddConversationInteraction(User item) {
            api.addConversation(authToken, item.getId(), new ConversationCallback() {
                @Override
                public void onSuccess(Conversation conversation) {
                    Intent i = new Intent(view.getActivity(), ConversationActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("conversation", conversation);
                    i.putExtras(args);
                    view.getActivity().startActivity(i);
                }

                @Override
                public void onError() {

                }
            });
    }
}
