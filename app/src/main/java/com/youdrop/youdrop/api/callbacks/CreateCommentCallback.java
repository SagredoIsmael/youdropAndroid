package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Publication;

/**
 * 
 * Class definition for a callback to be invoked when the response for the data 
 * submission is available.
 * 
 */
public abstract class CreateCommentCallback {
    /**
     * Called when a POST success response is received. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public abstract void onSuccess(Comment comment);
    public abstract void onError();

}