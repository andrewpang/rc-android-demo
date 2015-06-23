package com.ringcentral.androidsdk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew.pang on 6/23/15.
 */
public class DeleteMessage extends AsyncTask<String, Void, Void> {
    public interface DeleteMessageResponse {
        void DeleteMessageProcessFinish(String result);
    }

    public DeleteMessageResponse delegate = null;

    protected Void doInBackground(String... params) {
        String accessToken = params[0];
        String messageId = params[1];
        String url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~/extension/~/message-store/";
        url += messageId;
        HttpsURLConnection httpConn = null;
        BufferedReader in = null;

        try {

            URL request = new URL(url);
            httpConn = (HttpsURLConnection) request.openConnection();
            httpConn.setRequestMethod("DELETE");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpConn.setRequestProperty(
                    "Authorization",
                    "Bearer " + accessToken);
            httpConn.setDoOutput(true);
            httpConn.connect();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (httpConn != null)
                httpConn.disconnect();
        }
        return null;
    }


    protected void onPostExecute(String result) { delegate.DeleteMessageProcessFinish(result);
    }

    protected void onProgressUpdate(String... progress) {
    }

}

