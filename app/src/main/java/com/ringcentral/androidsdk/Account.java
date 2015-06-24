package com.ringcentral.androidsdk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

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
    public void getAccountInfo(HashMap<String, String> parameters){
//        String accessToken = params[0];
//        String accountID = params[1];
//        String[] accountParams = {accessToken, accountID, "account"};
        new AccountAsyncTask().execute(parameters);
    }

    public void getExtensionInfo(String... params){
//        String accessToken = params[0];
//        String accountID = params[1];
//        String extensionId = params[2];
//        String[] accountParams = {accessToken, accountID, extensionId, "extensionInfo"};
//        new AccountAsyncTask().execute(accountParams);
    }

    public void getExtensionList(String... params){
//        String accessToken = params[0];
//        String accountID = params[1];
//        String[] accountParams = {accessToken, accountID, "extensionList"};
//        new AccountAsyncTask().execute(accountParams);
    }

    public class AccountAsyncTask extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... params) {
            HashMap<String, String> parameters = (HashMap) params[0];
            String access_token = parameters.get("access_token");
            String accountId = parameters.get("accountId");
            String extensionId = "";
            String page = "";
            String perPage = "";
            if(parameters.containsKey(extensionId)){
                extensionId = parameters.get(extensionId);
            }
            if(parameters.containsKey(page)){
                page = parameters.get(page);
            }
            if(parameters.containsKey(perPage)){
                perPage = parameters.get(perPage);
            }
            //using responsetype
            String responseType = parameters.get("responseType");
            String responseString = "";
            HttpsURLConnection httpConn = null;
            String url = "";
            if(responseType == "account") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/" + accountId;
            }
            if(responseType == "extensionInfo") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/";
                url += accountId;
                url += "/extension/";
                url += extensionId;
            }
            if(responseType == "account") {
                url = "https://platform.devtest.ringcentral.com/restapi/v1.0/account/" +
                        accountId + "/extension";
            }
            try {
                URL request = new URL(url);
                httpConn = (HttpsURLConnection) request.openConnection();
                httpConn.setRequestProperty("Authorization", "Bearer " + access_token);
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
