package com.example.ivica.seekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar mySeekbar;
    TextView myTehxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbarr();
    }

    public void seekbarr(){
        mySeekbar = (SeekBar)findViewById(R.id.seekBar);

        myTehxView = (TextView)findViewById(R.id.textView);
        myTehxView.setText("Stanje: " + mySeekbar.getProgress() + " / " + mySeekbar.getMax());

        mySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                myTehxView.setText("Stanje: " + progress_value + " / " + mySeekbar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myTehxView.setText("Stanje: " + progress_value + " / " + mySeekbar.getMax());
            }
        });

    }

}
