package com.youdrop.youdrop.api;

import android.content.Context;

import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.results.ConversationsResult;
import com.youdrop.youdrop.data.results.NotificationsResult;
import com.youdrop.youdrop.data.results.PublicationsResult;
import com.youdrop.youdrop.data.results.UsersResult;

import org.afinal.simplecache.ACache;

import java.util.List;

/**
 * Created by pau on 01/10/17.
 */

public class ContentCache {


 private Context context;

    public ContentCache(Context context) {
        this.context = context;
        cache = ACache.get(context);
    }

    public static ContentCache instance;

    private ACache cache;

    public static ContentCache getInstance(Context c){
        //Choose an appropriate creation strategy.
        if (instance == null) instance = new ContentCache(c);

        return instance;
    }


    public NotificationsResult getPrivateNotifications() {
        return (NotificationsResult) cache.getAsObject("privateNotifications");
    }

    public void setPrivateNotifications(NotificationsResult privateNotificatons) {
        cache.put("privateNotifications", privateNotificatons, ACache.TIME_DAY);
    }
    public void clearPrivateNotifications() {
        cache.remove("privateNotifications");
    }

    public PublicationsResult getNotifications() {
        return (PublicationsResult) cache.getAsObject("notifications");
    }

    public void setNotifications(PublicationsResult privateNotificatons) {
        cache.put("notifications", privateNotificatons, ACache.TIME_DAY);
    }
    public void clearNotifications() {
        cache.remove("notifications");
    }

    public PublicationsResult getNearNotifications() {
        return (PublicationsResult) cache.getAsObject("nearNotifications");
    }

    public void setNearNotifications(PublicationsResult privateNotificatons) {
        cache.put("nearNotifications", privateNotificatons, ACache.TIME_DAY);
    }
    public void clearNearNotifications() {
        cache.remove("nearNotifications");
    }


    public UsersResult getFriends() {
        return (UsersResult) cache.getAsObject("friends");
    }

    public void setFriends(UsersResult privateNotificatons) {
        cache.put("friends", privateNotificatons, ACache.TIME_DAY);
    }
    public void clearFriends() {
        cache.remove("friends");
    }

    public ConversationsResult getConversations() {
        return (ConversationsResult) cache.getAsObject("conversations");
    }

    public void setConversations(ConversationsResult privateNotificatons) {
        cache.put("conversations", privateNotificatons, ACache.TIME_DAY);
    }
    public void clearConversations() {
        cache.remove("conversations");
    }

}
