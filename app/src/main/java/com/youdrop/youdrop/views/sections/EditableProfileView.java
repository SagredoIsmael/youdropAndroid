package com.youdrop.youdrop.views.sections;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;

import java.io.File;
import java.util.List;

/**
 * Created by pau on 03/10/17.
 */

public interface EditableProfileView {
    Context getContext();
    Activity getActivity();

    void setUserData(User user);
    void setUserImages(List<UserImage> images);
    void addUserImage(UserImage image);

    void showLoading(boolean show);

    void dispatchTakePictureIntent(File file);
    void dispatchGetPictureIntent();
    void dispatchTakePictureIntentForImages(File file);
    void dispatchGetPictureIntentForImages();
    void processImage(Uri uri);
    void editUser(String nameValue, String usernameValue, String emailValue, String descr);
}
