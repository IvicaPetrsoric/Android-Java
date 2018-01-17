package com.example.ivica.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {    // super class


    private final IBinder mojBinder = new MyLocalBinder();  // most, povezivanje klijenta sa servisom // drugo


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mojBinder;// prvo
    }

    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        return  (df.format(new Date()));
    }

    public class MyLocalBinder extends Binder{
        MyService getService(){
            System.out.println("USAO");
            return MyService.this;  // povratak u super klasu // trece, te nakon ovoga mozemo pristupiti svemu u ovoj klasi
        }
    }










}
