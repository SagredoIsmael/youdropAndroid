package com.youdrop.youdrop.controllers;

import android.content.Intent;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.results.UsersResult;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.views.sections.friends.SearchUsersView;

import java.util.List;

/**
 * Created by pau on 08/11/17.
 */

public class SearchUsersController implements UserRecyclerViewAdapter.OnUserInteractionListener {

    SearchUsersView view;

    ContentManager contentManager;

    String authToken;

    public SearchUsersController(SearchUsersView view) {
        this.view = view;
        contentManager = ContentManager.getInstance(view.getContext());
        authToken = Utils.getAuthToken(view.getContext());

    }

    public void search(String searchValue) {
        if (searchValue.length() < 3){
            view.clearList();
            return;
        }
        contentManager.searchUsers(searchValue, authToken, callback);
    }

    GetUsersCallback callback = new GetUsersCallback() {
        @Override
        public void onDataReceived(UsersResult result) {
            view.clearList();
            view.addUsersToList(result.getItems());
        }
    };

    @Override
    public void onUserInteraction(User item) {
        Intent i = new Intent(view.getContext(), ProfileActivity.class);
        Bundle args = new Bundle();
        args.putString(ProfileActivity.ARG_USERID, item.getId());
        i.putExtras(args);
        view.getContext().startActivity(i);
    }
}
