package com.youdrop.youdrop.views.sections.friends;

import android.content.Context;

import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface FriendsView {

    Context getContext();
    Context getActivity();

    void clearList();
    void addUsersToList(List<User> notifications);

    void showEmpty(boolean showEmpty);
}
