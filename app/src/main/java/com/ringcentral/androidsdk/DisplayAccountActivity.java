package com.ringcentral.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayAccountActivity extends Activity implements Account.AccountResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_account);
            Intent intent = getIntent();
            String access_token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            Account myAccount = new Account();
            myAccount.delegate = this;
            myAccount.execute(access_token);
            }

    public void AccountProcessFinish(String output){
            TextView TextView1 = (TextView) findViewById(R.id.textView1);
            TextView1.setText(output);
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_display_call_log, menu);
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
