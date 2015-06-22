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
import android.widget.TextView;


public class DisplayRingOutActivity extends Activity implements View.OnClickListener, RingOut.RingOutResponse {

    EditText fromText, toText, callerIDText;
    CheckBox checkPrompt;
    String access_token;
    String hasPrompt = "false";
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ring_out);
        Intent intent = getIntent();
        access_token = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        addListenerOnCheckBox();

        fromText = (EditText) findViewById(R.id.fromMessage);
        toText = (EditText) findViewById(R.id.toMessage);
        callerIDText = (EditText) findViewById(R.id.callerIDMessage);

    }

    public void addListenerOnCheckBox() {
        checkPrompt = (CheckBox) findViewById(R.id.checkPrompt);
        checkPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(((CheckBox) v).isChecked()) {
                        hasPrompt = "true";
                    }
                }
            });
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                RingOut myRingOut = new RingOut();
                myRingOut.delegate = this;
                String[] arr = {access_token, fromText.getText().toString(),
                        toText.getText().toString(), callerIDText.getText().toString(), hasPrompt};
                myRingOut.execute(arr);
        }
    }

    public void RingOutProcessFinish(String output){
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
