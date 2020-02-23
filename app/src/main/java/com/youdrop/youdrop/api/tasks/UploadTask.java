package com.youdrop.youdrop.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
     * An AsyncTask implementation for performing POSTs on the Hypothetical REST APIs.
     */
    public class UploadTask extends AsyncTask<String, String, String> {
    private String mRestUrl;
    private String mRestAuth;
        private RestTaskCallback mCallback;
        private String mRequestFile;
        private String mimetype;
        /**
         * Creates a new instance of PostTask with the specified URL, callback, and
         * request body.
         *
         * @param restUrl The URL for the REST API.
         * @param callback The callback to be invoked when the HTTP request
         *            completes.
         * @param requestFile The body of the POST request.
         *
         */
        public UploadTask(String restUrl, String requestFile, String mimetype,String authToken, RestTaskCallback callback){
            this.mRestUrl = restUrl;
            this.mRestAuth = authToken;
            this.mRequestFile = requestFile;
            this.mCallback = callback;
            this.mimetype = mimetype;
        }

        @Override
        protected String doInBackground(String... arg0) {
            //Use HTTP client API's to do the POST
            //Return response.
            BufferedInputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                URL url = new URL(mRestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");

                File f = new File(mRequestFile);

                httpURLConnection.setRequestProperty("Content-Type", mimetype);
                //httpURLConnection.setRequestProperty("Content-Length", "" + mRequestFile.getBytes().length);
               // httpURLConnection.setRequestProperty("Content-Language", "en-US");
                if (mRestAuth != null){
                    httpURLConnection.setRequestProperty("Authorization", "JWT: " + mRestAuth);
                }

                outputStream = httpURLConnection.getOutputStream();

               // BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                FileInputStream in = new FileInputStream(f);

                int count;
                byte[] buffer = new byte[8192]; // or 4096, or more
                while ((count = in.read(buffer)) > 0)
                {
                    outputStream.write(buffer, 0, count);
                }

               // bufferedWriter.write(mRequestFile);
               // bufferedWriter.flush();

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