package com.youdrop.youdrop.views.sections;

import android.content.Context;

import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;

import java.util.List;

/**
 * Created by pau on 03/10/17.
 */

public interface ProfileView {
    Context getContext();

    void setUserData(User user);
    void setUserImages(List<UserImage> images);

}
