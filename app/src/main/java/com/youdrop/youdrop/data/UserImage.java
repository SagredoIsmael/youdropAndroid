package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pau on 16/09/17.
 */

public class UserImage implements java.io.Serializable {
    @SerializedName("_id")
    String id;
    String body;
    @SerializedName("file")
    String fileId;
    File file;

    public UserImage() {
    }


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
