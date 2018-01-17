package com.example.ivica.activitiyexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button nextActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextActivityButton = (Button)findViewById(R.id.myButotn);
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent();
                newIntent.setClass(MainActivity.this,SecondActivity.class);

                Bundle from = new Bundle();//PRIJENOS PODATAKA
                from.putString("info","Napustite APK!");

                newIntent.putExtras(from);
                startActivity(newIntent);
            }
        });
    }
}
