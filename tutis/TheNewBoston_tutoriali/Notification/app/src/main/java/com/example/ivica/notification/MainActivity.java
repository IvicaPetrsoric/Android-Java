package com.example.ivica.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    NotificationCompat.Builder notification;
    private static final int uniqueID = 123;    // da bi znali o kojoj notifikaciji je riječ


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);   // da nestane notifikacija kada kliknemo na nju!


    }

    public void mojButtonClicked(View view){
        // build the notification
        notification.setSmallIcon(R.mipmap.ic_launcher);    // ikona na traci
        notification.setTicker("Ovo je TICKER "); // ticker poruka
        notification.setWhen(System.currentTimeMillis());   // trenutno vrijeme
        notification.setContentTitle("Ovo je title");
        notification.setContentText("ovo je core poruke");
        notification.setSound(RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_NOTIFICATION)); // ZA ZVUK
        notification.setNumber(3);  // broj ispod vremena
        notification.setLights(Color.CYAN,2000,1000);   // 2 sekunde dela, 1 ne dela

        // pri kliku da udu u ovaj activity
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);   // dopusta axes u intente , tj activiye
        notification.setContentIntent(pendingIntent);

        // Builds notification and issues
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID,notification.build());

    }














}
