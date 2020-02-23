package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by pau on 17/09/17.
 */

public class Notification implements java.io.Serializable {

    int id;
    @SerializedName("user_id")
    int userId;
    String title;
    String description;
    String key;
    boolean dismiss;
    Date createdAt;
    Date updatedAt;

    User user;

    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }

    User profile;
    Publication publication;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDismiss() {
        return dismiss;
    }

    public void setDismiss(boolean dismiss) {
        this.dismiss = dismiss;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }
}
