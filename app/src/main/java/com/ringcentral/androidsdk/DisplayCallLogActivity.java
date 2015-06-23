package com.ringcentral.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DisplayCallLogActivity extends Activity implements View.OnClickListener, CallLog.CallLogResponse, CallLog.ActiveCallResponse {

    Button button1, button2, button3;
    String access_token = "";
    EditText callRecordID;
    TextView TextView1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_call_log);
        Intent intent = getIntent();
        access_token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        callRecordID = (EditText) findViewById(R.id.callRecordID);
        TextView1 = (TextView) findViewById(R.id.textView1);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button1:
                CallLog myCallLog = new CallLog();
                myCallLog.delegate = this;
                myCallLog.getCallLogs(access_token);

            case R.id.button2:
                CallLog myCallLog1 = new CallLog();
                myCallLog1.delegate = this;
                myCallLog1.getCallLogs(access_token);
//                String callID = callRecordID.getText().toString();
//                myCallLog.getCallRecord(access_token, callID);

            case R.id.button3:
//                myCallLog.getActiveCalls(access_token);
//                CallLog myActiveCalls = new CallLog();
//                myActiveCalls.delegate = this;ß
//                myActiveCalls.getActiveCalls(access_token);

        }
    }

    public void CallLogProcessFinish(String output){
        TextView1.setText(output);
    }

    public void ActiveCallProcessFinish(String output){
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
