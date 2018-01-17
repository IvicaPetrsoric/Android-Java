package com.example.ivica.ClumsyBox.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.ivica.ClumsyBox.R;


public class BackgroundSoundService extends Service{

    //public static final String TAG = "com.example.ivica.smartclicker";

    MediaPlayer player;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.muzika);
        player.setLooping(true); // Set looping

        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        int progressMusic = sharedPref.getInt("seekBarMusic",100);

        player.setVolume((float)progressMusic/100,(float)progressMusic/100);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String stanjeMuzike = intent.getStringExtra("stanjeMuzike");

        if(stanjeMuzike != null){

            switch (stanjeMuzike) {

                case "1":
                    player.start();
                    break;

                case "2":
                    player.pause();
                    break;

                default:
                    float jacinaMuzike = Float.parseFloat(stanjeMuzike);
                    player.setVolume(jacinaMuzike / 100, jacinaMuzike / 100);
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {
    }
}

