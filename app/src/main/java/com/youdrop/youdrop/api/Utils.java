package com.youdrop.youdrop.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pau on 17/09/17.
 */

public class Utils {

    public static String serializeFacebookLoginRequest(String auth) throws JSONException{
        JSONObject object = new JSONObject();
        object.put("token", auth);
        return object.toString();
    }

    public static String serializeSearchUsersRequest(String search) {
        return "?search=" + search;
    }

    public static String serializeLocation(Location loc)throws JSONException{
        JSONObject object = new JSONObject();
        object.put("latotude", loc.getLatitude());
        object.put("longitude", loc.getLongitude());
        return object.toString();
    }

    public static String serializeLocationToParams(Location loc){

        return "?latitude=" + loc.getLatitude() + "&longitude=" + loc.getLongitude();
    }

    public static User deserialize(String value){
        Gson gson = getDeserializer();
        return gson.fromJson(value, User.class);
    }

    public static NotificationsResult deserializeNotifications(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .create();
        NotificationsResult result = gson.fromJson(value, NotificationsResult.class);
        return result;
    }

    public static PublicationsResult deserializeContents(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();

        PublicationsResult result = gson.fromJson(value, PublicationsResult.class);
        return result;
    }

    public static UsersResult deserializeUsers(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        UsersResult result = gson.fromJson(value, UsersResult.class);
        return result;
    }

    public static CountResult deserializeCount(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        CountResult result = gson.fromJson(value, CountResult.class);
        return result;
    }

    public static ConversationsResult deserializeConversations(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        ConversationsResult result = gson.fromJson(value, ConversationsResult.class);
        return result;
    }


    public static Conversation deserializeConversation(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        Conversation result = gson.fromJson(value, Conversation.class);
        return result;
    }

    public static MessagesResult deserializeMessages(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        MessagesResult result = gson.fromJson(value, MessagesResult.class);
        return result;
    }


    public static Message deserializeMessage(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        Message result = gson.fromJson(value, Message.class);
        return result;
    }

    public static String serializeMessage(Message c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, Message.class);
    }


    public static CommentsResult deserializeComments(String value){
        Gson gson = getDeserializer();
        CommentsResult array = gson.fromJson(value, CommentsResult.class);
        return array;
    }

    public static CategoriesResult deserializeCategories(String value){
        Gson gson = new Gson();
        CategoriesResult result = gson.fromJson(value, CategoriesResult.class);
        return result;
    }


    public static String serializeContent(Publication c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, Publication.class);
    }

    public static String serializeComment(Comment c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, Comment.class);
    }

    public static String serializeUserImage(UserImage c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, UserImage.class);
    }

    public static Comment deserializeComment(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        Comment p = gson.fromJson(value, Comment.class);
        return p;
    }

    public static Publication deserializeContent(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        Publication p = gson.fromJson(value, Publication.class);
        return p;
    }
    public static File deserializeFile(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        File p = gson.fromJson(value, File.class);
        return p;
    }

    public static UserImage deserializeUserImage(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        UserImage p = gson.fromJson(value, UserImage.class);
        return p;
    }

    private static Gson getDeserializer(){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson;
    }

    private static Gson getSerializer(){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson;
    }

    public static LikesResult deserializeLikes(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        LikesResult result = gson.fromJson(value, LikesResult.class);
        return result;
    }

    public static Like deserializeLike(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        Like result = gson.fromJson(value, Like.class);
        return result;
    }

    public static User deserializeUser(String value){
        Gson gson = getDeserializer();
        return gson.fromJson(value, User.class);
    }
    public static String serializeFile(File c) throws JSONException{
        Gson gson = getSerializer();
        return gson.toJson(c, File.class);
    }

    public static String serializeUser(User c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, User.class);
    }
    public static UsersImageResult deserializeUserImages(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson.fromJson(value, UsersImageResult.class);
    }

    public static String getAuthToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences( "session", Context.MODE_PRIVATE);
        return sharedPref.getString("authToken", null);
    }

    public static String getUserId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences( "session", Context.MODE_PRIVATE);
        return sharedPref.getString("userId", null);
    }

    public static Friendship deserializeFriendship(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson.fromJson(value, Friendship.class);
    }

    public static FriendshipsResult deserializeFriendships(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson.fromJson(value, FriendshipsResult.class);
    }

    public static Connection deserializeConnection(String value){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DeserializeStrategy())
                //.serializeNulls() <-- uncomment to serialize NULL fields as well
                .create();
        return gson.fromJson(value, Connection.class);
    }

    public static String serializeFriendship(Friendship c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, Friendship.class);
    }

    public static String serializeConnection(Connection c) throws JSONException{
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SerializeStrategy()).create();
        return gson.toJson(c, Connection.class);
    }

    private static Location location;
    public static void saveLocation(Location loc){
        location = loc;
    }

    public static String calculateDistance(double lat, double lon){

        Location loc2 = new Location("");
        loc2.setLatitude(lat);
        loc2.setLongitude(lon);

        float distanceInMeters = loc2.distanceTo(location);
        return String.format("%.2f km",distanceInMeters/1000);
    }


    /*public static String encodeFileToBase64Binary(String fileName)
            throws IOException {

        java.io.File file = new java.io.File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public static byte[] loadFile(java.io.File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }*/
}
