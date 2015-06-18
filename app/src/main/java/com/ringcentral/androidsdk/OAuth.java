package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Base64;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by andrew.pang on 6/12/15.
 */
public class OAuth {

    public void OAuthorizer(String grantType, String username, String password, String key, String secret) throws Exception {
        String[] myTaskParams = {grantType, username, password, key, secret};
        new MyAsyncTask().execute(myTaskParams);
        //String token = MyAsyncTask.get();
//        MyAsyncTask o = new MyAsyncTask();
//        o.doInBackground(myTaskParams);
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
            OkHttpClient client = new OkHttpClient();
            BufferedReader in = null;

            try {
                MediaType MEDIA_TYPE_MARKDOWN
                        = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
                StringBuilder data = new StringBuilder();
                data.append("username=" + URLEncoder.encode(username, "UTF-8"));
                data.append("&password=" + URLEncoder.encode(password, "UTF-8"));
                data.append("&grant_type=" + URLEncoder.encode(grantType, "UTF-8"));
                byte[] byteArray = data.toString().getBytes("UTF-8");
                Request request = new Request.Builder()
                        .url(url)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", encodedBasic)
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, byteArray)) //(MEDIA_TYPE_MARKDOWN,
                                // dataString))
                        .build();
                Response response = client.newCall(request).execute();
                accessToken = response.body().string();
//                httpConn = (HttpsURLConnection) request.openConnection();
//
//                httpConn.setDoOutput(true);
//                httpConn.setRequestMethod("POST");
//                //httpConn.setRequestProperty("Content-Length", String.valueOf(byteArray.length));
//                httpConn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded");
//                httpConn.setRequestProperty("Authorization","Basic " + encoded);
                //httpConn.setRequestProperty("Content-Language", "en-US");

                //httpConn.setDoInput(true);
                // httpConn.setReadTimeout(7000);
                //httpConn.setConnectTimeout(7000);

//                OutputStream postStream = httpConn.getOutputStream();
//                postStream.write(byteArray, 0, byteArray.length);
//                postStream.close();
//                httpConn.connect();
//
//                System.out.println(httpConn.getRequestProperty("Authorization"));
//
//                if (httpConn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
//                    System.out.println("Server returned HTTP " + httpConn.getResponseCode()
//                            + " " + httpConn.getResponseMessage());
//                }

                //System.out.println(httpConn.getHeaderFields());

                //IO Exception is thrown here
//                InputStreamReader reader = new InputStreamReader(
//                        httpConn.getInputStream());
//                System.out.println("hey2");
//                in = new BufferedReader(reader);
//                System.out.println("hey3");
//                StringBuffer content = new StringBuffer();
//                String line;
//                while ((line = in.readLine()) != null) {
//                    content.append(line + "\n");
//                }
//                in.close();
//
//                String json = content.toString();
//                Gson gson = new Gson();
//                Type mapType = new TypeToken<Map<String, String>>() {
//                }.getType();
//                Map<String, String> ser = gson.fromJson(json, mapType);
//                accessToken = ser.get("access_token");
//
//                //System.out.println(ser.get("expires_in"));
//                //String refreshToken= ser.get("refresh_token");
//                System.out.println("Access Token = " + accessToken);
//                //System.out.println("Refresh Token = " + refreshToken);

            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            } finally {
                //if (httpConn != null)
                //  httpConn.disconnect();
            }
            System.out.println(accessToken);
            return accessToken;

        }


        protected void onPostExecute(String result) {

        }

        protected void onProgressUpdate(String... progress) {
            //setProgress(progress[0]);
        }


    }
}
