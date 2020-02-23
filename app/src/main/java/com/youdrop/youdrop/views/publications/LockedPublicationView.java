package com.youdrop.youdrop.views.publications;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Publication;

import java.util.List;

/**
 * Created by pau on 26/09/17.
 */

public interface LockedPublicationView {

    Context getContext();
    Activity getActivity();

    void setupViewData(Publication publication, Location location);

    void setCommentList(List<Comment> comments);

    void setNumberOfComments(int number);

    void setNumberOfLikes(int number);
    void setLikeStatus(boolean status);

    void showLikes(List<Like> items);
}
