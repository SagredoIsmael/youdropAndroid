package com.youdrop.youdrop.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetTask extends AsyncTask<String, String, String> {

    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRestAuth;

    public GetTask(String mRestUrl, String mRestAuth, RestTaskCallback mCallback) {
        this.mRestUrl = mRestUrl;
        this.mCallback = mCallback;
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

            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            //httpURLConnection.setRequestProperty("Content-Length", "" + mRequestBody.getBytes().length);
            // httpURLConnection.setRequestProperty("Content-Language", "en-US");
            if (mRestAuth != null){
                httpURLConnection.setRequestProperty("Authorization", "JWT: " + mRestAuth);
            }

            int statusCode = httpURLConnection.getResponseCode();
            Log.d("", " The status code is " + statusCode);

            if (statusCode == 200 || statusCode == 201) {
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                byte[] contents = new byte[1024];

                int bytesRead = 0;
                String strFileContents = "";
                while((bytesRead = inputStream.read(contents)) != -1) {
                    strFileContents += new String(contents, 0, bytesRead);
                }
                return strFileContents;
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
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}


