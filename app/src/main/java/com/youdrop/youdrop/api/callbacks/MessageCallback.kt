package com.paugonzaleza.learning.api.callbacks

import com.youdrop.youdrop.data.Message


/**
 *
 * Class definition for a callback to be invoked when the response for the data
 * submission is available.
 *
 */
abstract class MessageCallback {
    /**
     * Called when a POST success response is received. <br></br>
     * This method is guaranteed to execute on the UI thread.
     */
    abstract fun onSuccess(message: Message)

    abstract fun onError(error:String)

}
