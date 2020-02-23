package com.youdrop.youdrop.data

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Created by pau on 16/09/17.
 */

class Message : java.io.Serializable {
    @SerializedName("_id")
    var id: String? = null
    var body: String? = null
    var user: User? = null
    @SerializedName("user")
    var userId: String? = null
    var conversation: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
}
