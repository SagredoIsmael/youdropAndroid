package com.youdrop.youdrop.views.publications;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.User;

import java.io.File;
import java.util.List;

/**
 * Created by pau on 26/09/17.
 */

public interface PublishView {
    Context getContext();
    Activity getActivity();

    void showLoading(boolean show);

    void setFriendList(List<String> users);
    void setPublicMode();
    void setPrivateMode();

    void addSelectedFriend(User name);
    void removeSelectedFriend(int pos);

    public void setAnonymous(boolean value);

    public void setDownloadable(boolean value);

    void dispatchTakePictureIntent(File file);
    void dispatchGetPictureIntent();
    void dispatchTakeVideoIntent();
    void dispatchGetVideoIntent();
    void setPreview(Bitmap bitmap);
    void setPreviewVideo(String uri);

    void showError(String message);

    void gotoContent(Publication c);

}
