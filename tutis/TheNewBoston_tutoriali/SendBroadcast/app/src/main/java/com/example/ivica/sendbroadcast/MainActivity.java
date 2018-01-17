package com.example.ivica.sendbroadcast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SendOutBroadcast(View view){

        Intent i = new Intent(); // ne treba klasa jer ce poslati svima koji slusaju
        i.setAction("com.example.ivica.sendbroadcast");//za ogranicavanje apk koje imaju ovo za izvor apk, pa samo one slusaju
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);   // za kompatibilnost svih androida
        sendBroadcast(i);


    }




}
