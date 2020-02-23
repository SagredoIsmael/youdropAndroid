package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pau on 17/09/17.
 */

public class Connection implements java.io.Serializable {

    @SerializedName("_id")
    String id;
    @SerializedName("user")
    String userId;
    User user;
    @SerializedName("receiver")
    String receiverId;
    User receiver;


    public Connection(String id, String userId, String receiverId) {
        this.id = id;
        this.userId = userId;
        this.receiverId = receiverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

}
