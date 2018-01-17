package com.example.ivica.stopwatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ivica on 15.4.2016..
 */
public class StopWatchService extends Service {

    NotificationManager mNotificationManager;
    Notification mNotification;
    Intent mIntent;
    PendingIntent pIntent;
    Timer mTimer;
    int NOTIFICATION_ID = 99;

    long measure_start;
    long measure_stop;
    Date startDate, endDate;

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    DateFormat sdf2 = new SimpleDateFormat("hh:mm:ss:SSS");
    String timeInfo = "";

    @Override
    public IBinder onBind(Intent arg0) { return null;}


    @Override
    public void onCreate()
    {
        super.onCreate();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT + 2"));
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT + 2"));

        makeNotification();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateNotification();
            }
        },3000,1000);
    }

    @Override
    public void onDestroy()
    {
        mTimer.cancel();
        mNotificationManager.cancelAll();

        endDate = new Date();
        measure_stop = SystemClock.elapsedRealtime();
        long diff = (measure_stop - measure_start);

       /* System.out.println(measure_stop);
        System.out.println(measure_start);
        System.out.println(diff);*/
        // gmt + 2
      //  diff = diff - 2 * 60 * 60 * 1000;

        timeInfo = sdf.format(startDate) + " / " +
                sdf.format(endDate)  + " / " +
                sdf.format(new Date(diff));

        Intent f = new Intent(getBaseContext(),StopwatchResult.class);
        f.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        f.putExtra("time",timeInfo);
        getApplication().startActivity(f);

        /*mTimer.cancel();
        mNotificationManager.cancelAll();*/
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId)
    {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }


    // Notification init
    private  void makeNotification()
    {
        mIntent = new Intent(this,MyStopWatch.class);
        pIntent = PendingIntent.getActivity(this,0,mIntent,0);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        measure_start = SystemClock.elapsedRealtime();
        startDate = new Date();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        mNotification = builder
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.stopwatchwhite)
                .setContentTitle("Štoperica")
                //  text ce se prikazati 5 sek i onda ide refresh
                .setContentText("Pocetak: "+sdf.format(startDate))
                .build();

        mNotificationManager.notify(NOTIFICATION_ID,mNotification);

    }

    //  Notification update
    private void updateNotification()
    {

        endDate = new Date();
        measure_stop = SystemClock.elapsedRealtime();

        long diff = endDate.getTime() - startDate.getTime();

       // diff = diff - 2 * 60 * 60 * 1000;

        timeInfo = sdf.format(startDate) + " / " +
                sdf.format(endDate) + " / " +
                sdf.format(new Date(diff));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        mNotification = builder.setContentIntent(pIntent)
                .setSmallIcon(R.drawable.stopwatchwhite)
                .setContentTitle("Štoperica")
                .setContentText(timeInfo)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);


    }

}
