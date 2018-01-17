package com.example.ivica.activitiyexample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Ivica on 17.4.2016..
 */
public class SecondActivity extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_activity);

        Bundle from = getIntent().getExtras();
        tv = (TextView) findViewById(R.id.textView);

        tv.setText(from.getString("info"));

    }
}
