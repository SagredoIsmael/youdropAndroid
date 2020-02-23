package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by pau on 17/09/17.
 */

public class Friendship implements java.io.Serializable {

    @SerializedName("_id")
    String id;
    @SerializedName("user")
    String userId;
    User user;
    @SerializedName("receiver")
    String receiverId;
    User receiver;
    boolean accepted;
    boolean revoked;

    public Friendship( String receiverId) {
        this.userId = userId;
        this.receiverId = receiverId;
        this.accepted = accepted;
        this.revoked = revoked;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
