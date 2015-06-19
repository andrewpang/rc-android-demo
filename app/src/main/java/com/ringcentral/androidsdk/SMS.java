package com.ringcentral.androidsdk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew.pang on 6/19/15.
 */
public class SMS extends AsyncTask<String, Void, String> {
    public interface SMSResponse {
        void SMSProcessFinish(String result);
    }

    public SMSResponse delegate = null;

    protected String doInBackground(String... params) {
        String accessToken = params[0];
        String fromNumber = params[1];
        String toNumber = params[2];
        String textMessage = params[3];
        String url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~/extension/~/sms";
        HttpsURLConnection httpConn = null;
        BufferedReader in = null;
        String payload = "{\r\n  \"to\": [{\"phoneNumber\": \"" + toNumber + "\"}],\r\n  \"from\": {\"phoneNumber\": \"" + fromNumber + "\"},\r\n  \"text\": \"" + textMessage + "\"\r\n}";

        String ringOutId="";
        try {

            URL request = new URL(url);
            httpConn = (HttpsURLConnection) request.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            httpConn.setRequestProperty(
                    "Authorization",
                    "Bearer " + accessToken);

            httpConn.setDoOutput(true);
            OutputStream postStream = httpConn.getOutputStream();
            postStream.write(payload.getBytes("UTF-8"), 0,
                    payload.getBytes().length);
            postStream.close();
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
            String[] s = json.split(",");
            s = s[1].split(":");
            ringOutId=s[1].replace(" ", "");

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
        return ringOutId;
    }


    protected void onPostExecute(String result) {
        delegate.SMSProcessFinish(result);
    }

    protected void onProgressUpdate(String... progress) {
    }

}

