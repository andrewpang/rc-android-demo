package com.ringcentral.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class MainActivity extends Activity implements View.OnClickListener, Version.VersionResponse,
        OAuth.OAuthResponse,
        SMS.SMSResponse {

    public final static String EXTRA_MESSAGE = "com.ringcentral.androidSDK.MESSAGE";
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    String access_token = "";

    public static void log(String message) {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("/Users/andrew.pang/log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info(message);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Yoto");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8  = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(this);
        button9  = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button1:

                TextView TextView1 = (TextView) findViewById(R.id.textView1);
                String grantType = "password";
                String username = "15856234166";
                String password = "P@ssw0rd";
                String key = "xhK3uzISTEaEYhFAtadVug";
                String secret = "1YRoPu64TeCOe_ZJy3ggLwGg-QDQd6QaWpSyIT8AxmjA";

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("grantType", grantType);
                params.put("username", username);
                params.put("password", password);
                params.put("key", key);
                params.put("secret", secret);

                OAuth o = new OAuth();
                o.delegate = this;
                try {
                    //o.OAuthorizer(grantType, username, password, key, secret);
                    o.OAuthorizer(params);
                    TextView1.setText("Pressed log in");
                } catch (Exception e) {
                    e.printStackTrace();
                    access_token = "Error";
                    TextView1.setText(access_token);
                }
                //TextView TextView1 = (TextView) findViewById(R.id.textView1);
                //TextView1.setText(access_token);

                break;

            case R.id.button2:
                Version myVersion = new Version();
                myVersion.delegate = this;
                myVersion.execute();
                break;

            case R.id.button3:
                Intent accountIntent = new Intent(this, DisplayAccountActivity.class);
                accountIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(accountIntent);
                break;

            case R.id.button4:
                Intent ringOutIntent = new Intent(this, DisplayRingOutActivity.class);
                ringOutIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(ringOutIntent);
                break;

            case R.id.button5:
                //Start a new activity for call log, and pass the access token to the new activity
                Intent callLogIntent = new Intent(this, DisplayCallLogActivity.class);
                callLogIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(callLogIntent);
                break;

            case R.id.button6:
                Intent SMSIntent = new Intent(this, DisplaySMSActivity.class);
                SMSIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(SMSIntent);
                break;

            case R.id.button7:
                //Start a new activity for message store, and pass the access token to the new activity
                Intent messageStoreIntent = new Intent(this, DisplayMessageStoreActivity.class);
                messageStoreIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(messageStoreIntent);
                break;

            case R.id.button8:
                TextView TextView2 = (TextView) findViewById(R.id.textView1);

                //Hard code
                String key1 = "xhK3uzISTEaEYhFAtadVug";
                String secret1 = "1YRoPu64TeCOe_ZJy3ggLwGg-QDQd6QaWpSyIT8AxmjA";

                OAuth j = new OAuth();
                j.delegate = this;
                try {
                    j.Revoke(key1, secret1, access_token);
                    TextView2.setText("Token Revoked");
                } catch (Exception e) {
                    e.printStackTrace();
                    TextView2.setText("Error");
                }
                break;

            case R.id.button9:
                //Start a new activity for message store, and pass the access token to the new activity
                Intent deleteMessageIntent = new Intent(this, DisplayDeleteMessageActivity.class);
                deleteMessageIntent.putExtra(EXTRA_MESSAGE, access_token);
                startActivity(deleteMessageIntent);
                break;

        }
    }

    public void VersionProcessFinish(String output) {
        TextView TextView1 = (TextView) findViewById(R.id.textView1);
        TextView1.setText(output);
    }

    public void OAuthProcessFinish(String output) {
        access_token = output;
        TextView TextView1 = (TextView) findViewById(R.id.textView1);
        TextView1.setText(access_token);
    }

    public void SMSProcessFinish(String output){
        TextView TextView1 = (TextView) findViewById(R.id.textView1);
        TextView1.setText(output);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
