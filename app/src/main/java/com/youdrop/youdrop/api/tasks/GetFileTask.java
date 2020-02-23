package com.youdrop.youdrop.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetFileTask extends AsyncTask<String, Integer, String> {

    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRestAuth;
    private String mLocalPath;

    public GetFileTask(String mRestUrl, String mRestAuth,String mLocalPath, RestTaskCallback mCallback) {
        this.mRestUrl = mRestUrl;
        this.mCallback = mCallback;
        this.mLocalPath = mLocalPath;
        this.mRestAuth = mRestAuth;
    }


    @Override
    protected String doInBackground(String... params) {
        BufferedInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(mRestUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            //httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");

          //  httpURLConnection.setRequestProperty("Content-Type", "application/json");
            //httpURLConnection.setRequestProperty("Content-Length", "" + mRequestBody.getBytes().length);
            // httpURLConnection.setRequestProperty("Content-Language", "en-US");
            if (mRestAuth != null){
                httpURLConnection.setRequestProperty("Authorization", "JWT: " + mRestAuth);
            }

            int statusCode = httpURLConnection.getResponseCode();
            Log.d("", " The status code is " + statusCode);

            if (statusCode == 200 || statusCode == 201) {
                int fileLength = httpURLConnection.getContentLength();

                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                FileOutputStream output = new FileOutputStream(mLocalPath);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        inputStream.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            }
            return null;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(mLocalPath);
        super.onPostExecute(result);
    }
}


