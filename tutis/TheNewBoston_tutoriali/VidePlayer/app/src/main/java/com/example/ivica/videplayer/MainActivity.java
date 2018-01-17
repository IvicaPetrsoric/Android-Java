package com.example.ivica.videplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView moVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // povezivanje s videom
        moVideoView = (VideoView) findViewById(R.id.mojVideoView);
        moVideoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");


        // OMOGUĆI KOMANDE PLAYA
        /*MediaController mediaController = new MediaController(this);    // pozivamo iz clase
        mediaController.setAnchorView(moVideoView);
        moVideoView.setMediaController(mediaController);*/


        moVideoView.start();





    }







}
