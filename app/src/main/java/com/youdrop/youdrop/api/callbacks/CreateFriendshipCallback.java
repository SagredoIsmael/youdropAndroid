package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.data.Publication;

/**
 * 
 * Class definition for a callback to be invoked when the response for the data 
 * submission is available.
 * 
 */
public abstract class CreateFriendshipCallback {
    /**
     * Called when a POST success response is received. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public abstract void onSuccess(Friendship friendship);
    public abstract void onError();

}