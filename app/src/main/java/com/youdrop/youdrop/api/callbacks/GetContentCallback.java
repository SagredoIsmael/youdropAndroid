package com.youdrop.youdrop.api.callbacks;

import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.results.PublicationsResult;

import java.util.List;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class GetContentCallback {

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     * 
     * @param notifications The {@code Profile} that was received from the server.
     */
    public abstract void onDataReceived(PublicationsResult publication);

    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}