package com.ringcentral.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class DisplaySMSActivity extends Activity implements View.OnClickListener, SMS.SMSResponse {

    EditText fromText, toText, SMSText;
    String access_token;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sms);
        Intent intent = getIntent();
        access_token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        fromText = (EditText) findViewById(R.id.fromMessage);
        toText = (EditText) findViewById(R.id.toMessage);
        SMSText = (EditText) findViewById(R.id.SMSMessage);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                SMS mySMS = new SMS();
                mySMS.delegate = this;
                String[] arr = {access_token, fromText.getText().toString(),
                        toText.getText().toString(), SMSText.getText().toString()};
                mySMS.execute(arr);
        }
    }

    public void SMSProcessFinish(String output){
//        TextView TextView1 = (TextView) findViewById(R.id.textView1);
//        TextView1.setText(output);
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
