package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.results.LikesResult;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class LikeCallback {

    public abstract void onSuccess(Like file);
    public abstract void onError();
}