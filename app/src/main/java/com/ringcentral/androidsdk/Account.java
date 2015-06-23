package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew.pang on 6/19/15.
 */
public class Account {

    public interface AccountResponse {
        void AccountProcessFinish(String result);
    }
    public AccountResponse delegate = null;

    //Currently works with ~, need to implement passing in accountID
    public void getAccountInfo(String... params){
        String accessToken = params[0];
        String[] accountParams = {"account", accessToken};
        new AccountAsyncTask().execute(accountParams);
    }

    public void getExtensionInfo(String... params){
        String accessToken = params[0];
        String[] accountParams = {"extensionInfo", accessToken};
        new AccountAsyncTask().execute(accountParams);
    }

    public void getExtensionList(String... params){
        String accessToken = params[0];
        String[] accountParams = {"extensionList", accessToken};
        new AccountAsyncTask().execute(accountParams);
    }

    public class AccountAsyncTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String responseType = params[0];
            String accessToken = params[1];
            String responseString = "";
            HttpsURLConnection httpConn = null;
            String url = "";
            if(responseType == "account") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~";
            }
            if(responseType == "extensionInfo") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~/extension/~";
            }
            if(responseType == "account") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/~/extension";
            }
            try {
                URL request = new URL(url);
                httpConn = (HttpsURLConnection) request.openConnection();
                httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);
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
            delegate.AccountProcessFinish(result);
        }

        protected void onProgressUpdate(String... progress) {
        }
}

}
