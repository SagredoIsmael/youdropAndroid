package com.youdrop.youdrop.views.sections.friends;

import android.content.Context;

import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.User;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface ConversationsView {

    Context getContext();
    Context getActivity();

    void clearList();
    void addConversationsToList(List<Conversation> notifications);
    void showEmpty(boolean showEmpty);
}
