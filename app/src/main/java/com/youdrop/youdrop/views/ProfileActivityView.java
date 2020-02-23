package com.youdrop.youdrop.views;

import android.content.Context;

import com.youdrop.youdrop.data.Connection;
import com.youdrop.youdrop.data.Friendship;

/**
 * Created by pau on 17/11/17.
 */

public interface ProfileActivityView {
    Context getContext();

    void showFriendship(Friendship friendship, Connection connection);
    void showMessage(String message);
}
