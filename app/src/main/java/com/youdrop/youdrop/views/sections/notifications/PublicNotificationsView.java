package com.youdrop.youdrop.views.sections.notifications;

import android.app.Activity;
import android.content.Context;

import com.youdrop.youdrop.data.Publication;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface PublicNotificationsView {

    Context getContext();
    Activity getActivity();

    void clearList();
    void addContentToList(List<Publication> publication);
    void showEmpty(boolean showEmpty);

}
