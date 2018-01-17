package com.example.ivica.java_rawfolder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class menu extends Activity {

    MediaPlayer logoMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patak);

       // logoMusic = MediaPlayer.create(menu.this,R.raw.muzika);
      //  logoMusic.start();

        //  button sound
        final MediaPlayer buttonSound = MediaPlayer.create(menu.this,R.raw.button_sound);

        Button but = (Button)findViewById(R.id.button2);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
               // startActivity(new Intent("asd"));
            }
        });

        /*Thread logoTimer = new Thread(){

            public void run(){

                try {
                    sleep(5000);
                    Intent menu = new Intent(menu.this,MainActivity.class);
                    startActivity(menu);
                }
                 catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                    finish();   // zavrsava trenutno activity
                }
            }
        };
        logoTimer.start();*/

    }

    @Override
    protected void onPause() {
        super.onPause();
       // logoMusic.release();

    }

}
