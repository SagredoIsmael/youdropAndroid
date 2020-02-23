package com.youdrop.youdrop.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pau on 27/09/17.
 */

public class Data {

    String content;
    String filename;
    @SerializedName("content_type")
    String contentType;

    public Data(String content, String filename, String contentType) {
        this.content = content;
        this.filename = filename;
        this.contentType = contentType;
    }
}
