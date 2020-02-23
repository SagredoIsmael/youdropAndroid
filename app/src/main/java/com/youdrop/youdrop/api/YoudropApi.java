package com.youdrop.youdrop.api;

import android.location.Location;
import android.util.Log;

import com.paugonzaleza.learning.api.callbacks.MessageCallback;
import com.paugonzaleza.talks.api.callbacks.MessagesCallback;
import com.youdrop.youdrop.api.callbacks.ConversationCallback;
import com.youdrop.youdrop.api.callbacks.CountCallback;
import com.youdrop.youdrop.api.callbacks.CreateCommentCallback;
import com.youdrop.youdrop.api.callbacks.CreateFriendshipCallback;
import com.youdrop.youdrop.api.callbacks.CreatePublicationCallback;
import com.youdrop.youdrop.api.callbacks.FileCallback;
import com.youdrop.youdrop.api.callbacks.GetCategoriesCallback;
import com.youdrop.youdrop.api.callbacks.GetCommentsCallback;
import com.youdrop.youdrop.api.callbacks.GetConnectionCallback;
import com.youdrop.youdrop.api.callbacks.GetContentCallback;
import com.youdrop.youdrop.api.callbacks.GetConversationsCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipCallback;
import com.youdrop.youdrop.api.callbacks.GetFriendshipsCallback;
import com.youdrop.youdrop.api.callbacks.GetNotificationsCallback;
import com.youdrop.youdrop.api.callbacks.FBLoginCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleContentCallback;
import com.youdrop.youdrop.api.callbacks.GetSingleUserCallback;
import com.youdrop.youdrop.api.callbacks.GetUserImagesCallback;
import com.youdrop.youdrop.api.callbacks.GetUsersCallback;
import com.youdrop.youdrop.api.callbacks.LikeCallback;
import com.youdrop.youdrop.api.callbacks.LikesCallback;
import com.youdrop.youdrop.api.callbacks.UserImageCallback;
import com.youdrop.youdrop.api.tasks.DeleteTask;
import com.youdrop.youdrop.api.tasks.GetTask;
import com.youdrop.youdrop.api.tasks.PostTask;
import com.youdrop.youdrop.api.tasks.RestTaskCallback;
import com.youdrop.youdrop.api.tasks.UpdateTask;
import com.youdrop.youdrop.api.tasks.UploadTask;
import com.youdrop.youdrop.data.Category;
import com.youdrop.youdrop.data.Comment;
import com.youdrop.youdrop.data.Connection;
import com.youdrop.youdrop.data.File;
import com.youdrop.youdrop.data.Friendship;
import com.youdrop.youdrop.data.Like;
import com.youdrop.youdrop.data.Message;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.data.results.CategoriesResult;
import com.youdrop.youdrop.data.results.CommentsResult;
import com.youdrop.youdrop.data.results.ConversationsResult;
import com.youdrop.youdrop.data.results.CountResult;
import com.youdrop.youdrop.data.results.FriendshipsResult;
import com.youdrop.youdrop.data.results.LikesResult;
import com.youdrop.youdrop.data.results.MessagesResult;
import com.youdrop.youdrop.data.results.NotificationsResult;
import com.youdrop.youdrop.data.results.PublicationsResult;
import com.youdrop.youdrop.data.results.UsersImageResult;
import com.youdrop.youdrop.data.results.UsersResult;

import org.json.JSONException;

import java.util.List;

public class YoudropApi implements Api{

    public static final String FB_LOGIN_API = "http://api.you-drop.com/sessions/fb";
    public static final String PRIVATE_NOTIFICATIONS_API = "http://api.you-drop.com/notifications";
    public static final String PUBLIC_NOTIFICATIONS_API = "http://api.you-drop.com/publications";
    public static final String NEAR_NOTIFICATIONS_API = "http://api.you-drop.com/near-publications";
    public static final String FRIENDS_API = "http://api.you-drop.com/friends";
    public static final String FILES_API = "http://api.you-drop.com/files/json-upload";
    public static final String FILES_UPLOAD_API = "http://api.you-drop.com/files/upload";
    public static final String CONVERSATIONS_API = "http://api.you-drop.com/conversations";
    public static final String USER_CONVERSATION_API = "http://api.you-drop.com/users/:id/conversation";
    public static final String MESSAGES_API = "http://api.you-drop.com/conversations/:id/messages";
    public static final String CATEGORIES_API = "http://api.you-drop.com/categories";
    public static final String COMMENTS_API = "http://api.you-drop.com/publications/:id/comments";
    public static final String COMMENTS_COUNT_API = "http://api.you-drop.com/publications/:id/counts/comments";
    public static final String LIKES_COUNT_API = "http://api.you-drop.com/publications/:id/counts/likes";
    public static final String LIKES_API = "http://api.you-drop.com/publications/:id/likes";
    public static final String LIKE_API = "http://api.you-drop.com/publications/:id/like";
    public static final String CONTENTS_API = "http://api.you-drop.com/publications";
    public static final String CONTENT_API = "http://api.you-drop.com/publications/:id";
    public static final String SEARCH_USERS_API = "http://api.you-drop.com/users";
    public static final String USERS_API = "http://api.you-drop.com/users/:id";
    public static final String FRIENDSHIPS_API = "http://api.you-drop.com/friendships";
    public static final String ACCEPT_FRIENDSHIPS_API = "http://api.you-drop.com/friendships/:id/accept";
    public static final String DENY_FRIENDSHIPS_API = "http://api.you-drop.com/friendships/:id/deny";
    public static final String USER_FRIENDSHIP_API = "http://api.you-drop.com/users/:id/friendship";
    public static final String USER_CONNECTION_API = "http://api.you-drop.com/users/:id/follow";
    public static final String USER_IMAGES_API = "http://api.you-drop.com/users/:id/images";
    public static final String USER_IMAGE_API = "http://api.you-drop.com/user_images/:id";

    public static YoudropApi instance;

    public YoudropApi() {
    }

    public static YoudropApi getInstance(){
        //Choose an appropriate creation strategy.
        if (instance == null) instance = new YoudropApi();

        return instance;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken;

    public static YoudropApi getInstance(String authToken){
        //Choose an appropriate creation strategy.
        if (instance == null) instance = new YoudropApi();
        //TODO set token
        return instance;
    }

    /**
     * Request a User Profile from the REST server.
     * @param callback Callback to execute when the profile is available.
     */
    public void getPrivateNotifications( String authToken,  final GetNotificationsCallback callback){
        String restUrl = PRIVATE_NOTIFICATIONS_API;
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                NotificationsResult result = Utils.deserializeNotifications(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    public void getNotifications(String authToken, Location location,  final GetContentCallback callback){
        String restUrl = PUBLIC_NOTIFICATIONS_API + Utils.serializeLocationToParams(location);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    PublicationsResult result = Utils.deserializeContents(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }

    }

    public void getNearNotifications(String authToken, Location location,  final GetContentCallback callback){
        String restUrl = NEAR_NOTIFICATIONS_API + Utils.serializeLocationToParams(location);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    PublicationsResult result = Utils.deserializeContents(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }

    }

    public void getComments( String authToken, String contentId,   final GetCommentsCallback callback){
        String restUrl = COMMENTS_API.replace(":id", ""+ contentId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                CommentsResult result = Utils.deserializeComments(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    /**
     * Submit a user profile to the server.
     * @param accessToken The profile to submit
     * @param callback The callback to execute when submission status is available.
     */
    public void facebookLogin(String accessToken, final FBLoginCallback callback){
        String restUrl = FB_LOGIN_API;
        try {
            String requestBody = Utils.serializeFacebookLoginRequest(accessToken);
            new PostTask(restUrl, requestBody, null, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    callback.onSuccess(Utils.deserialize(response));
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }

    }

    public void fakeLogin( final FBLoginCallback callback){
        String restUrl = FB_LOGIN_API;
        try {
            String requestBody = Utils.serializeFacebookLoginRequest("EAACEdEose0cBAJsYH8ueqPqr2haXAoppvFyP87osplqkUBU0fIkOx2gNy9fqVqRNCFfEdnoVZBn0FskuLXVagrZBZCiePGJTgaVAi4WZABOnV8D8GZBydMattp4XdZAreEnqpU9846gYdNXh10B3885QPqYGmLkMlUVVab0qagxehEDkxIgFOYrtZBS1t0RLNaAu2uZC8M3DMAZDZD");
            new PostTask(restUrl, requestBody, null, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    callback.onSuccess(Utils.deserialize(response));
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }

    }

    /**
     * Request a User Profile from the REST server.
     * @param callback Callback to execute when the profile is available.
     */
    public void getFriends( String authToken,  final GetUsersCallback callback){
        String restUrl = FRIENDS_API;
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                UsersResult result = Utils.deserializeUsers(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    /**
     * Request a User Profile from the REST server.
     * @param callback Callback to execute when the profile is available.
     */
    public void getConversations( String authToken,  final GetConversationsCallback callback){
        String restUrl = CONVERSATIONS_API;
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                ConversationsResult result = Utils.deserializeConversations(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    /**
     * Request a User Profile from the REST server.
     * @param callback Callback to execute when the profile is available.
     */
    public void getCategories( String authToken,  final GetCategoriesCallback callback){
        String restUrl = CATEGORIES_API;
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                CategoriesResult result = Utils.deserializeCategories(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    public void createPublication(String authToken, Publication publication, final CreatePublicationCallback callback){
        String restUrl = CONTENTS_API;
        try {
            String requestBody = Utils.serializeContent(publication);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeContent(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getContent(String authToken, String contentId, Location location, final GetSingleContentCallback callback) {
        String restUrl = CONTENT_API.replace(":id", ""+ contentId) + Utils.serializeLocationToParams(location);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Publication result = Utils.deserializeContent(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getUser(String authToken, String userId, final GetSingleUserCallback callback) {
        String restUrl = USERS_API.replace(":id",  userId);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    User result = Utils.deserializeUser(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getUserImages(String authToken, String userId, final GetUserImagesCallback callback) {
        String restUrl = USER_IMAGES_API.replace(":id", userId);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    UsersImageResult result = Utils.deserializeUserImages(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void searchUsers(String search, String authToken, final GetUsersCallback callback) {
        String restUrl = SEARCH_USERS_API+ Utils.serializeSearchUsersRequest(search);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                UsersResult result = Utils.deserializeUsers(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    @Override
    public void getConnection(String authToken, String userId, final GetConnectionCallback callback) {
        String restUrl = USER_CONNECTION_API.replace(":id",  userId);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Connection result = Utils.deserializeConnection(response);
                    if (result.getId() != null){
                        callback.onDataReceived(result);
                    } else {
                        callback.onDataReceived(null);
                    }
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getFriendship(String authToken, String userId, final GetFriendshipCallback callback) {
        String restUrl = USER_FRIENDSHIP_API.replace(":id",  userId);
        try {
            new GetTask(restUrl,authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Friendship result = Utils.deserializeFriendship(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void addFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback) {
        String restUrl = FRIENDSHIPS_API;
        try {
            String requestBody = Utils.serializeFriendship(friendship);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeFriendship(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    public void getFriendshipRequests( String authToken,  final GetFriendshipsCallback callback){
        String restUrl = FRIENDSHIPS_API;
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                FriendshipsResult result = Utils.deserializeFriendships(response);
                callback.onDataReceived(result);
            }
        }).execute();
    }

    @Override
    public void acceptFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback) {
        String restUrl = ACCEPT_FRIENDSHIPS_API.replace(":id", ""+ friendship.getId());;
        try {
            String requestBody = Utils.serializeFriendship(friendship);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeFriendship(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void denyFriendship(String authToken, Friendship friendship, final CreateFriendshipCallback callback) {
        String restUrl = DENY_FRIENDSHIPS_API.replace(":id", ""+ friendship.getId());;
        try {
            String requestBody = Utils.serializeFriendship(friendship);
            new DeleteTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeFriendship(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void follow(String authToken, String userId, final GetConnectionCallback callback) {
        String restUrl = USER_CONNECTION_API.replace(":id",  userId);
        try {
            new PostTask(restUrl, "{}",authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Connection result = Utils.deserializeConnection(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void unfollow(String authToken, String userId, final GetConnectionCallback callback) {
        String restUrl = USER_CONNECTION_API.replace(":id",  userId);
        try {
            new DeleteTask(restUrl, "{}",authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Connection result = Utils.deserializeConnection(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void addFile(String authToken, File file, final FileCallback callback) {
        String restUrl = FILES_API;
        try {
            String requestBody = Utils.serializeFile(file);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeFile(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void updateUser(String authToken, User user, final GetSingleUserCallback callback) {
        String restUrl = USERS_API.replace(":id",  user.getId());
        try {
            String requestBody = Utils.serializeUser(user);

            new UpdateTask(restUrl,requestBody, authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    User result = Utils.deserializeUser(response);
                    callback.onDataReceived(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }


    @Override
    public void getCommentsCount(String authToken, String contentId, final CountCallback callback) {
        String restUrl = COMMENTS_COUNT_API.replace(":id", ""+ contentId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                CountResult result = Utils.deserializeCount(response);
                callback.onSuccess(result.getCount());
            }
        }).execute();
    }

    @Override
    public void addComment(String authToken, String publicationId, Comment comment, final CreateCommentCallback callback) {
        String restUrl = COMMENTS_API.replace(":id", ""+ publicationId);
        try {
            String requestBody = Utils.serializeComment(comment);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeComment(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getLikesCount(String authToken, String contentId, final CountCallback callback) {
        String restUrl = LIKES_COUNT_API.replace(":id", ""+ contentId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                CountResult result = Utils.deserializeCount(response);
                if(result!=null)callback.onSuccess(result.getCount());
            }
        }).execute();
    }

    @Override
    public void addLike(String authToken, String contentId, final LikeCallback callback) {
        String restUrl = LIKE_API.replace(":id",  contentId);
        try {
            new PostTask(restUrl, "{}",authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Like result = Utils.deserializeLike(response);
                    callback.onSuccess(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void getLike(String authToken, String contentId, final LikeCallback callback) {
        String restUrl = LIKE_API.replace(":id", ""+ contentId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                Like result = Utils.deserializeLike(response);
                if (result.getId() != null){
                    callback.onSuccess(result);
                } else {
                    callback.onError();
                }
            }
        }).execute();
    }

    @Override
    public void getLikes(String authToken, String contentId, final LikesCallback callback) {
        String restUrl = LIKES_API.replace(":id", ""+ contentId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                LikesResult result = Utils.deserializeLikes(response);
                callback.onSuccess(result);
            }
        }).execute();
    }

    @Override
    public void dislike(String authToken, String contentId, final LikeCallback callback) {
        String restUrl = LIKE_API.replace(":id",  contentId);
        try {
            new DeleteTask(restUrl, "{}",authToken, new RestTaskCallback(){
                @Override
                public void onTaskComplete(String response){
                    Like result = Utils.deserializeLike(response);
                    callback.onSuccess(result);
                }
            }).execute();
        } catch (Exception e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void addFile(String authToken, String filepath,String mime, final FileCallback callback) {
        String restUrl = FILES_UPLOAD_API;
        new UploadTask(restUrl, filepath, mime, authToken, new RestTaskCallback(){
            public void onTaskComplete(String response){
                if (response!= null){
                    callback.onSuccess(Utils.deserializeFile(response));
                } else {
                    callback.onError();
                }
            }
        }).execute();
    }

    @Override
    public void addUserImage(String authToken, String userID, UserImage file, final UserImageCallback callback) {
        String restUrl = USER_IMAGES_API.replace(":id", ""+ userID);
        try {
            String requestBody = Utils.serializeUserImage(file);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeUserImage(response));
                    } else {
                        callback.onError();
                    }
                }
            }).execute();
        } catch (JSONException e){
            Log.e("",e.getMessage());
        }
    }

    @Override
    public void deleteUserImage(String authToken, UserImage file, final UserImageCallback callback) {
        String restUrl = USER_IMAGE_API.replace(":id", ""+ file.getId());
        new DeleteTask(restUrl, "{}", authToken, new RestTaskCallback(){
            public void onTaskComplete(String response){
                if (response!= null){
                    callback.onSuccess(Utils.deserializeUserImage(response));
                } else {
                    callback.onError();
                }
            }
        }).execute();
    }

    @Override
    public void deletePublication(String authToken, Publication publication, final GetSingleContentCallback callback) {
        String restUrl = CONTENT_API.replace(":id", ""+ publication.getId());
        new DeleteTask(restUrl, "{}", authToken, new RestTaskCallback(){
            public void onTaskComplete(String response){
                if (response!= null){
                    callback.onDataReceived(Utils.deserializeContent(response));
                }
            }
        }).execute();
    }

    @Override
    public void addConversation(String authToken, String receiverId, final ConversationCallback callback) {
        String restUrl = USER_CONVERSATION_API.replace(":id", ""+ receiverId);
        new PostTask(restUrl, "{}", authToken, new RestTaskCallback(){
            public void onTaskComplete(String response){
                if (response!= null){
                    callback.onSuccess(Utils.deserializeConversation(response));
                } else {
                    callback.onError();
                }
            }
        }).execute();
    }

    @Override
    public void getMessages(String authToken, String conversationId, final MessagesCallback callback) {
        String restUrl = MESSAGES_API.replace(":id", ""+ conversationId);
        new GetTask(restUrl,authToken, new RestTaskCallback(){
            @Override
            public void onTaskComplete(String response){
                MessagesResult result = Utils.deserializeMessages(response);
                callback.onSuccess(result);
            }
        }).execute();
    }

    @Override
    public void addMessage(String authToken, String conversationId, Message message,final MessageCallback callback) {
        String restUrl = MESSAGES_API.replace(":id", ""+ conversationId);
        try{
            String requestBody = Utils.serializeMessage(message);
            new PostTask(restUrl, requestBody, authToken, new RestTaskCallback(){
                public void onTaskComplete(String response){
                    if (response!= null){
                        callback.onSuccess(Utils.deserializeMessage(response));
                    } else {
                        callback.onError("");
                    }
                }
            }).execute();
        } catch (JSONException e){

        }

    }
}


