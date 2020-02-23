package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Conversation;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class ConversationCallback {

    public abstract void onSuccess(Conversation conversation);
    public abstract void onError();
}