package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew.pang on 6/17/15.
 */

public class Version extends AsyncTask<Void, Void, String> {
    public VersionResponse delegate = null;

    protected String doInBackground(Void... params) {
        //JSONArray response = new JSONArray();
        String responseString = "";
        HttpsURLConnection httpConn = null;
        String url = "https://platform.devtest.ringcentral.com/restapi/";
        try {
            URL request = new URL(url);
            httpConn = (HttpsURLConnection) request.openConnection();

            int responseCode = httpConn.getResponseCode();
            if (responseCode == 200) {
                responseString = readStream(httpConn.getInputStream());
            } else {
                Log.v("CatalogClient", "Response code:" + responseCode);
            }
        } catch (java.io.IOException e) {
            System.out.println("Error: IOException");
            System.out.println(e.getMessage());

        } finally {
            if (httpConn != null)
                httpConn.disconnect();
        }
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(responseString);
//        return jsonString;
        return responseString;

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    protected void onPostExecute(String result) {
        delegate.versionProcessFinish(result);
    }

    protected void onProgressUpdate(String... progress) {
        //setProgress(progress[0]);
    }

    public interface VersionResponse {
        void versionProcessFinish(String result);
    }
}

