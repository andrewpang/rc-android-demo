package com.ringcentral.androidsdk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew.pang on 6/19/15.
 */
public class CallLog extends AsyncTask<String, Void, String> {
    public interface CallLogResponse {
        void CallLogProcessFinish(String result);
    }
    public CallLogResponse delegate = null;

    protected String doInBackground(String... params) {
        String accessToken = params[0];
        String url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~/call-log";
        HttpsURLConnection httpConn = null;
        BufferedReader in = null;
        String output =  "";
        try {
            URL request = new URL(url);
            httpConn = (HttpsURLConnection) request.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty(
                    "Authorization",
                    "Bearer " + accessToken);
            httpConn.setDoOutput(false);

            InputStreamReader reader = new InputStreamReader(
                    httpConn.getInputStream());
            in = new BufferedReader(reader);

            StringBuffer content = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line + "\n");
            }
            in.close();

            String json = content.toString();
            Date date = new Date();
            output = "--------------"+date+"-------------------\n";
            output = output+ json;
            //System.out.println("Call-log : "+ json);

        } catch (java.io.IOException e) {
            output = output+ e.getMessage();
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

        output=output+"\n --------------------------------------------\n\n\n";

        //writing to file
        //try {
            //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/Users/vyshakh.babji/Logger_folder/log.txt", true)));
            //out.println(output);
            //out.close();
        //} catch (IOException e) {
            //exception handling left as an exercise for the reader
        //}
        return output;
    }

    protected void onPostExecute(String result) {
        delegate.CallLogProcessFinish(result);
    }

    protected void onProgressUpdate(String... progress) {
    }
}
