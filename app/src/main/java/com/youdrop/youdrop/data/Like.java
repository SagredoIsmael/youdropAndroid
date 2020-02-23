package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pau on 17/09/17.
 */

public class Like implements java.io.Serializable {

    @SerializedName("_id")
    String id;
    @SerializedName("user")
    String userId;
    User user;
    String publication;


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

}
