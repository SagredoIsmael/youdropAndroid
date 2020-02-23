package com.paugonzaleza.talks.api.callbacks

import com.youdrop.youdrop.data.results.MessagesResult

/**
 *
 * Class definition for a callback to be invoked when the response for the data
 * submission is available.
 *
 */
abstract class MessagesCallback {
    /**
     * Called when a POST success response is received. <br></br>
     * This method is guaranteed to execute on the UI thread.
     */
    abstract fun onSuccess(result: MessagesResult)
    abstract fun onError(error: String)

}
