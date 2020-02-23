package com.youdrop.youdrop.views.sections.friends;

import android.content.Context;

import com.youdrop.youdrop.data.User;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface SearchUsersView {

    Context getContext();

    void clearList();
    void addUsersToList(List<User> users);

}
