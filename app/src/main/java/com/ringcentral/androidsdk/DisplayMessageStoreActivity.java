package com.ringcentral.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageStoreActivity extends Activity implements MessageStore.MessageStoreResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String access_token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        MessageStore myMessageStore = new MessageStore();
        myMessageStore.delegate = this;
        //Call the async task, passing in the access token as a parameter
        myMessageStore.execute(access_token);
    }

    //When async task is finished, this method is called and the output from the task is passed in
    public void MessageStoreProcessFinish(String output){
        TextView textView = new TextView(this);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(output);
        setContentView(textView);
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
