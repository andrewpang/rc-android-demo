package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by andrew.pang on 6/12/15.
 */
public class OAuth {

    public interface OAuthResponse {
        void OAuthProcessFinish(String result);
    }
    public OAuthResponse delegate = null;

    public void OAuthorizer(String grantType, String username, String password, String key, String secret) throws Exception {
        String[] myTaskParams = {grantType, username, password, key, secret};
        new MyAsyncTask().execute(myTaskParams);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        //@Override
        protected String doInBackground(String... params) {
            String grantType = params[0];
            String username = params[1];
            String password = params[2];
            String key = params[3];
            String secret = params[4];

            String url = "https://platform.devtest.ringcentral.com/restapi/oauth/token";
            String accessToken = "";
            String keySec = key + ":" + secret;
            byte[] message = new byte[0];
            try {
                message = keySec.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String encoded = Base64.encodeToString(message, Base64.DEFAULT);
            String encodedBasic = ("Basic " + encoded).replace("\n", "");
            BufferedReader in = null;
            HttpsURLConnection httpConn = null;

            try {
                StringBuilder data = new StringBuilder();
                data.append("grant_type=" + URLEncoder.encode(grantType, "UTF-8"));
                data.append("&username=" + URLEncoder.encode(username, "UTF-8"));
                data.append("&password=" + URLEncoder.encode(password, "UTF-8"));
                byte[] byteArray = data.toString().getBytes("UTF-8");
                URL request = new URL(url);
                httpConn = (HttpsURLConnection) request.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpConn.setRequestProperty(
                        "Authorization",
                        encodedBasic);
                httpConn.setDoOutput(true);
                OutputStream postStream = httpConn.getOutputStream();
                postStream.write(byteArray, 0, byteArray.length);
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
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> ser = gson.fromJson(json, mapType);
                accessToken = ser.get("access_token");

            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            } finally {
                if (httpConn != null)
                  httpConn.disconnect();
            }
            System.out.println(accessToken);
            return accessToken;

        }


        protected void onPostExecute(String result) {
            delegate.OAuthProcessFinish(result);
        }

        protected void onProgressUpdate(String... progress) {
        }


    }
}
