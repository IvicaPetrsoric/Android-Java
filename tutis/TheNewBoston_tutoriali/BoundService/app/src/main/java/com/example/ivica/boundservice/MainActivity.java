package com.example.ivica.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.ivica.boundservice.MyService.MyLocalBinder;

public class MainActivity extends AppCompatActivity {


    MyService mojService;
    boolean isBouNd = false; // da li je povezano sa servisom


    public void showTime(View view)
    {
        System.out.println("PRIKAZI TEXT");
        String currentTime = mojService.getCurrentTime();
        final TextView mojText = (TextView)findViewById(R.id.textView);
        mojText.setText(currentTime);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this,MyService.class);
        bindService(i,mojServiceConnection, Context.BIND_AUTO_CREATE);

    }


    private ServiceConnection mojServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {   // sta kada se spojimo
            MyLocalBinder binder = (MyLocalBinder) service;
            mojService = binder.getService();
            isBouNd = true;
            System.out.println("tu sam");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { // sta kada se odspojimo
            isBouNd = false;
        }
    };





}
