package com.youdrop.youdrop.api;

import android.content.Context;
import android.location.Location;

import com.paugonzaleza.learning.api.callbacks.MessageCallback;
import com.paugonzaleza.talks.api.callbacks.MessagesCallback;
import com.youdrop.youdrop.api.callbacks.ConversationCallback;
import com.youdrop.youdrop.api.callbacks.CountCallback;
import com.youdrop.youdrop.api.callbacks.CreateCommentCallback;
import com.youdrop.youdrop.api.callbacks.CreateFriendshipCallback;
import com.youdrop.youdrop.api.callbacks.CreatePublicationCallback;
import com.youdrop.youdrop.api.callbacks.FBLoginCallback;
import com.youdrop.youdrop.api.callbacks.FileCallback;
import com.youdrop.youdrop.api.callbacks.GetCategoriesCallback;
import com.youdrop.youdrop.api.callbacks.GetCommentsCallback;
import com.youdrop.youdrop.api.callbacks.GetConnectionCallback;
import com.youdrop.youdrop.api.callbacks.GetContentCallback;
import com.youdrop.youdrop.api.callbacks.GetConversationsCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipsCallback;
import com.youdrop.youdrop.api.callbacks.GetNotificationsCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleContentCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleUserCallback;
import com.youdrop.youdrop.api.callbacks.GetUserImagesCallback;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.api.callbacks.LikeCallback;
import com.youdrop.youdrop.api.callbacks.LikesCallback;
import com.youdrop.youdrop.api.callbacks.UserImageCallback;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.File;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.data.Message;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.data.results.CategoriesResult;
import com.youdrop.youdrop.data.results.ConversationsResult;
import com.youdrop.youdrop.data.results.NotificationsResult;
import com.youdrop.youdrop.data.results.PublicationsResult;
import com.youdrop.youdrop.data.results.UsersResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pau on 27/09/17.
 */

public class ContentManager implements Api {

    YoudropApi api;

    ContentCache cache;

    public YoudropApi getApi() {
        return api;
    }

    public static ContentManager instance;

    public ContentManager(Context c) {
        api = YoudropApi.getInstance();
        cache = ContentCache.getInstance(c);
    }

    public static ContentManager getInstance(Context c){
        //Choose an appropriate creation strategy.
        if (instance == null) instance = new ContentManager(c);

        return instance;
    }

    @Override
    public void getPrivateNotifications(String authToken, final GetNotificationsCallback callback) {
        NotificationsResult privateNotifications = cache.getPrivateNotifications();
        if (privateNotifications != null) {
            callback.onDataReceived(privateNotifications);
        } else {
            GetNotificationsCallback internalCallback = new GetNotificationsCallback() {

                @Override
                public void onDataReceived(NotificationsResult result) {
                    if (result != null){
                        cache.setPrivateNotifications(result);
                        callback.onDataReceived(result);
                    } else {
                        callback.onDataReceived(new NotificationsResult(new ArrayList<Notification>()));
                    }
                }
            };
            api.setAuthToken(authToken);
            api.getPrivateNotifications(authToken, internalCallback);
        }

    }

    @Override
    public void deleteUserImage(String authToken, UserImage file, UserImageCallback callback) {
        api.deleteUserImage(authToken, file, callback);
    }

    @Override
    public void deletePublication(String authToken, Publication publication, GetSingleContentCallback callback) {
        api.deletePublication(authToken, publication, callback);
    }

    @Override
    public void getNotifications(String authToken, Location location, final GetContentCallback callback) {
        PublicationsResult notificationList = cache.getNotifications();
        if (notificationList != null) {
            callback.onDataReceived(notificationList);
        } else {
            GetContentCallback internalCallback = new GetContentCallback() {
                @Override
                public void onDataReceived(PublicationsResult notifications) {
                    cache.setNotifications(notifications);
                    callback.onDataReceived(notifications);
                }
            };
            api.setAuthToken(authToken);
            api.getNotifications(authToken, location, internalCallback);
        }
    }

    @Override
    public void getNearNotifications(String authToken, Location location, final GetContentCallback callback) {
        PublicationsResult notificationList = cache.getNearNotifications();
        if (notificationList != null) {
            callback.onDataReceived(notificationList);
        } else {
            GetContentCallback internalCallback = new GetContentCallback() {
                @Override
                public void onDataReceived(PublicationsResult notifications) {
                    cache.setNearNotifications(notifications);
                    callback.onDataReceived(notifications);
                }
            };
            api.setAuthToken(authToken);
            api.getNearNotifications(authToken, location, internalCallback);
        }
    }

    @Override
    public void getComments(String authToken, String contentId, GetCommentsCallback callback) {
        api.setAuthToken(authToken);
        api.getComments(authToken, contentId, callback);
    }

    @Override
    public void getCommentsCount(String authToken, String contentId, CountCallback callback) {
        api.setAuthToken(authToken);
        api.getCommentsCount(authToken, contentId, callback);
    }

    @Override
    public void addComment(String authToken, String publicationId, Comment comment, CreateCommentCallback callback) {
        api.setAuthToken(authToken);
        api.addComment(authToken, publicationId, comment, callback);
    }

    @Override
    public void getLikesCount(String authToken, String contentId, CountCallback callback) {
        api.setAuthToken(authToken);
        api.getLikesCount(authToken, contentId, callback);
    }

    @Override
    public void facebookLogin(String accessToken, FBLoginCallback callback) {
        api.facebookLogin(accessToken, callback);
    }

    @Override
    public void getFriends(String authToken, final GetUsersCallback callback) {
        UsersResult notificationList = cache.getFriends();
        if (notificationList != null) {
            callback.onDataReceived(notificationList);
        } else {
            GetUsersCallback internalCallback = new GetUsersCallback() {
                @Override
                public void onDataReceived(UsersResult result) {
                    cache.setFriends(result);
                    callback.onDataReceived(result);
                }
            };
            api.setAuthToken(authToken);
            api.getFriends(authToken, internalCallback);
        }
    }

    @Override
    public void getConversations(String authToken, final GetConversationsCallback callback) {
        ConversationsResult notificationList = cache.getConversations();
        if (notificationList != null) {
            callback.onDataReceived(notificationList);
        } else {
            GetConversationsCallback internalCallback = new GetConversationsCallback() {
                @Override
                public void onDataReceived(ConversationsResult notifications) {
                   // cache.setConversations(notifications);
                    callback.onDataReceived(notifications);
                }
            };
            api.setAuthToken(authToken);
            api.getConversations(authToken, internalCallback);
        }
    }

    @Override
    public void getCategories(String authToken, final GetCategoriesCallback callback) {
            api.setAuthToken(authToken);
            api.getCategories(authToken, callback);

    }

    @Override
    public void getContent(String authToken, String contentId, Location location, GetSingleContentCallback callback) {
        api.setAuthToken(authToken);
        api.getContent(authToken, contentId, location, callback);
    }

    @Override
    public void getUser(String authToken, String userId, final GetSingleUserCallback callback) {
        api.setAuthToken(authToken);
        api.getUser(authToken, userId, callback);
    }

    @Override
    public void getUserImages(String authToken, String userId, GetUserImagesCallback callback) {
        api.setAuthToken(authToken);
        api.getUserImages(authToken, userId, callback);
    }

    @Override
    public void searchUsers(String search, String authToken, GetUsersCallback callback) {
        api.setAuthToken(authToken);
        api.searchUsers(search, authToken, callback);
    }

    @Override
    public void getFriendship(String authToken, String userId, GetFriendshipCallback callback) {
        api.setAuthToken(authToken);
        api.getFriendship(authToken, userId, callback);
    }

    @Override
    public void getConnection(String authToken, String userId, GetConnectionCallback callback) {
        api.setAuthToken(authToken);
        api.getConnection(authToken, userId, callback);
    }

    @Override
    public void createPublication(String authToken, Publication publication, CreatePublicationCallback callback) {
        api.setAuthToken(authToken);
        api.createPublication(authToken, publication, callback);
    }

    @Override
    public void addFriendship(String authToken, Friendship friendship, CreateFriendshipCallback callback) {
        api.setAuthToken(authToken);
        api.addFriendship(authToken, friendship, callback);
    }

    @Override
    public void getFriendshipRequests(String authToken, GetFriendshipsCallback callback) {
        api.setAuthToken(authToken);
        api.getFriendshipRequests(authToken, callback);
    }

    @Override
    public void acceptFriendship(String authToken, Friendship friendship, CreateFriendshipCallback callback) {
        api.setAuthToken(authToken);
        api.acceptFriendship(authToken, friendship, callback);
    }

    @Override
    public void denyFriendship(String authToken, Friendship friendship, CreateFriendshipCallback callback) {
        api.setAuthToken(authToken);
        api.denyFriendship(authToken, friendship, callback);

    }

    @Override
    public void follow(String authToken, String userId, GetConnectionCallback callback) {
        api.setAuthToken(authToken);
        api.follow(authToken, userId, callback);
    }

    @Override
    public void unfollow(String authToken, String userId, GetConnectionCallback callback) {
        api.setAuthToken(authToken);
        api.unfollow(authToken, userId, callback);
    }

    @Override
    public void addFile(String authToken, File file, FileCallback callback) {
        api.setAuthToken(authToken);
        api.addFile(authToken, file, callback);
    }

    @Override
    public void updateUser(String authToken, User user, GetSingleUserCallback callback) {
        api.setAuthToken(authToken);
        api.updateUser(authToken, user, callback);
    }

    @Override
    public void addLike(String authToken, String contentId, LikeCallback callback) {
        api.setAuthToken(authToken);
        api.addLike(authToken, contentId, callback);
    }

    @Override
    public void getLikes(String authToken, String contentId, LikesCallback callback) {
        api.setAuthToken(authToken);
        api.getLikes(authToken, contentId, callback);
    }

    @Override
    public void getLike(String authToken, String contentId, LikeCallback callback) {
        api.setAuthToken(authToken);
        api.getLike(authToken, contentId, callback);
    }

    @Override
    public void dislike(String authToken, String contentId, LikeCallback callback) {
        api.setAuthToken(authToken);
        api.dislike(authToken, contentId, callback);
    }

    @Override
    public void addFile(String authToken, String filepath,String mime, FileCallback callback) {
        api.setAuthToken(authToken);
        api.addFile(authToken, filepath, mime, callback);
    }


    @Override
    public void addUserImage(String authToken, String userID, UserImage file, UserImageCallback callback) {
        api.setAuthToken(authToken);
        api.addUserImage(authToken, userID, file, callback);
    }

    @Override
    public void addConversation(String authToken, String receiverId, ConversationCallback callback) {
        api.setAuthToken(authToken);
        api.addConversation(authToken, receiverId, callback);
    }

    @Override
    public void getMessages(String authToken, String conversationId, MessagesCallback callback) {
        api.setAuthToken(authToken);
        api.getMessages(authToken, conversationId, callback);
    }

    @Override
    public void addMessage(String authToken, String conversationId, Message message, MessageCallback callback) {
        api.setAuthToken(authToken);
        api.addMessage(authToken, conversationId, message, callback);
    }
}
