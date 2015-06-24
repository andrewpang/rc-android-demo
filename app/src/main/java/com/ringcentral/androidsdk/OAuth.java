package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by andrew.pang on 6/12/15.
 */
public class OAuth {

    //public String encodedBasic;

    public interface OAuthResponse {
        void OAuthProcessFinish(String result);
    }
    public OAuthResponse delegate = null;

    public void OAuthorizer(Map<String, String> parameters) {
        new MyAsyncTask().execute(parameters);
    }

    public void Revoke(Map<String, String> parameters){
        new RevokeAsyncTask().execute(parameters);
    }

    public class MyAsyncTask extends AsyncTask<Object, Void, String> {
        //@Override
        protected String doInBackground(Object... params) {
            HashMap<String, String> parameters = (HashMap) params[0];
            String grant_type = parameters.get("grant_type");
            String username = parameters.get("username");
            String password = parameters.get("password");
            String key = parameters.get("key");
            String secret = parameters.get("secret");
            //Optional parameters
            String extension = "";
            String access_token_ttl = "";
            String refresh_token_ttl = "";
            String scope = "";
            String refresh_token = "";
            if(parameters.containsKey("extension")) {
                extension = parameters.get("extension");
            }
            if(parameters.containsKey("access_token_ttl")) {
                access_token_ttl = parameters.get("access_token_ttl");
            }
            if(parameters.containsKey("refresh_token_ttl")) {
                refresh_token_ttl = parameters.get("refresh_token_ttl");
            }
            if(parameters.containsKey("scope")) {
                scope = parameters.get("scope");
            }
            if(parameters.containsKey("refresh_token")) {
                refresh_token = parameters.get("refresh_token");
            }

            String url = "https://platform.devtest.ringcentral.com/restapi/oauth/token";
            String responseBody = "";
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
                data.append("grant_type=" + URLEncoder.encode(grant_type, "UTF-8"));
                data.append("&username=" + URLEncoder.encode(username, "UTF-8"));
                data.append("&password=" + URLEncoder.encode(password, "UTF-8"));
                if(!(extension.equals(""))){
                    data.append("&extension=" + URLEncoder.encode(extension, "UTF-8"));
                }
                if(!(access_token_ttl.equals(""))){
                    data.append("&access_token_ttl=" + URLEncoder.encode(access_token_ttl, "UTF-8"));
                }
                if(!(refresh_token_ttl.equals(""))){
                    data.append("&refresh_token_ttl=" + URLEncoder.encode(refresh_token_ttl, "UTF-8"));
                }
                if(!(scope.equals(""))){
                    data.append("&scope=" + URLEncoder.encode(scope, "UTF-8"));
                }
                if(!(refresh_token.equals(""))){
                    data.append("&refresh_token=" + URLEncoder.encode(refresh_token, "UTF-8"));
                }
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

                int responseCode = httpConn.getResponseCode();
                InputStream stream = null;
                if(responseCode == 200) {
                    stream = httpConn.getInputStream();
                } else {
                    stream = httpConn.getErrorStream();
                }
                InputStreamReader reader = new InputStreamReader(stream);
                in = new BufferedReader(reader);
                StringBuffer content = new StringBuffer();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line + "\n");
                }
                in.close();
                String json = content.toString();
                //Return whole json body
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> ser = gson.fromJson(json, mapType);
                if(responseCode == 200)
                    responseBody = ser.get("access_token");
                else
                    responseBody = ser.get("error");
                } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (httpConn != null)
                  httpConn.disconnect();
            }
            return responseBody;

        }
        protected void onPostExecute(String result) {
            delegate.OAuthProcessFinish(result);
        }

        protected void onProgressUpdate(String... progress) {
        }
    }

    public class RevokeAsyncTask extends AsyncTask<Object, Void, Void> {
        //@Override
        protected Void doInBackground(Object... params) {
            HashMap<String, String> parameters = (HashMap) params[0];
            String key = parameters.get("app_key");
            String secret = parameters.get("app_secret");
            String token = parameters.get("token");

            String url = "https://platform.devtest.ringcentral.com/restapi/oauth/revoke";

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
                data.append("token=");
                data.append(URLEncoder.encode(token, "UTF-8"));
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
                int code = httpConn.getResponseCode();
                System.out.println(code);
//                InputStreamReader reader = new InputStreamReader(
//                        httpConn.getInputStream());
//                in = new BufferedReader(reader);
//                StringBuffer content = new StringBuffer();
//                String line;
//                while ((line = in.readLine()) != null) {
//                    content.append(line + "\n");
//                }
//                in.close();
//                String json = content.toString();
//                Gson gson = new Gson();
//                Type mapType = new TypeToken<Map<String, String>>() {
//                }.getType();
//                Map<String, String> ser = gson.fromJson(json, mapType);
//                accessToken = ser.get("access_token");

            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            } finally {
                if (httpConn != null)
                    httpConn.disconnect();
            }
        return null;
        }


        protected void onPostExecute(String result) {
            delegate.OAuthProcessFinish(result);
        }

        protected void onProgressUpdate(String... progress) {
        }


    }
}
