package com.example.ivica.zenclocksurface;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZenClockSurface sfvTrack = (ZenClockSurface)findViewById(R.id.zenClockSurface1);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrack = sfvTrack.getHolder();
        sfhTrack.setFormat(PixelFormat.TRANSLUCENT);
    }
}
