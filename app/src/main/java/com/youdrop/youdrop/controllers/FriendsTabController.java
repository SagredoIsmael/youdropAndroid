package com.youdrop.youdrop.controllers;

import com.youdrop.youdrop.views.sections.FriendsTabView;

/**
 * Created by pau on 16/09/17.
 */

public class FriendsTabController {

    private FriendsTabView view;



    public FriendsTabController(FriendsTabView view) {
        this.view = view;
    }


    public void showFriends(){
        view.showFriends();
    }

    public void showConversations(){
        view.showXats();
    }


}
