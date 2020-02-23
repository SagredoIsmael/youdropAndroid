package com.youdrop.youdrop.views.sections;

import android.content.Context;

/**
 * Created by pau on 16/09/17.
 */

public interface FriendsTabView {

    Context getContext();

    void showFriends();
    void showXats();

    void reload();
}
