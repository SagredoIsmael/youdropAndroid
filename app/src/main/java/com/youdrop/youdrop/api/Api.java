package com.youdrop.youdrop.api;

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
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.File;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.data.Message;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;

/**
 * Created by pau on 27/09/17.
 */

public interface Api {

    public void getPrivateNotifications( String authToken,  final GetNotificationsCallback callback);
    public void getNotifications(String authToken, Location location, final GetContentCallback callback);
    public void getNearNotifications(String authToken, Location location,  final GetContentCallback callback);
    public void getContent(String authToken, String contentId, Location location,  final GetSingleContentCallback callback);
    public void facebookLogin(String accessToken, final FBLoginCallback callback);
    public void getFriends( String authToken,  final GetUsersCallback callback);
    public void getConversations( String authToken,  final GetConversationsCallback callback);
    public void getCategories( String authToken,  final GetCategoriesCallback callback);
    public void getUser(String authToken, String userId,  final GetSingleUserCallback callback);
    public void getUserImages(String authToken, String userId,  final GetUserImagesCallback callback);
    public void searchUsers(String search, String authToken,  final GetUsersCallback callback);

    public void getFriendship(String authToken, String userId,  final GetFriendshipCallback callback);
    public void addFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback);
    public void getFriendshipRequests( String authToken,  final GetFriendshipsCallback callback);
    public void acceptFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback);
    public void denyFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback);

    public void createPublication(String authToken, Publication publication, final CreatePublicationCallback callback);
    public void deletePublication(String authToken, Publication publication, final GetSingleContentCallback callback);

    public void addUserImage(String authToken, String userID, UserImage file, final UserImageCallback callback);
    public void deleteUserImage(String authToken, UserImage file, final UserImageCallback callback);
    public void addFile(String authToken, File file, final FileCallback callback);
    public void updateUser(String authToken, User user, final GetSingleUserCallback callback);

    public void getComments( String authToken, String contentId,   final GetCommentsCallback callback);
    public void getCommentsCount( String authToken, String contentId,   final CountCallback callback);
    public void addComment(String authToken, String publicationId, Comment comment, final CreateCommentCallback callback);

    public void getLikes( String authToken, String contentId,   final LikesCallback callback);
    public void getLikesCount( String authToken, String contentId,   final CountCallback callback);
    public void addLike(String authToken, String contentId, final LikeCallback callback);
    public void getLike(String authToken, String contentId,  final LikeCallback callback);
    public void dislike(String authToken, String contentId,  final LikeCallback callback);

    public void getConnection(String authToken, String userId,  final GetConnectionCallback callback);
    public void follow(String authToken, String userId,  final GetConnectionCallback callback);
    public void unfollow(String authToken, String userId,  final GetConnectionCallback callback);
    public void addFile(String authToken, String filepath, String mime, final FileCallback callback);

    public void addConversation(String authToken, String receiverId, final ConversationCallback callback);
    public void getMessages(String authToken, String conversationId, final MessagesCallback callback);
    public void addMessage(String authToken, String conversationId, Message message, final MessageCallback callback);

}
