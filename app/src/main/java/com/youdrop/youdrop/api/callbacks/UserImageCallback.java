package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.UserImage;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class UserImageCallback {

    public abstract void onSuccess(UserImage file);
    public abstract void onError();
}