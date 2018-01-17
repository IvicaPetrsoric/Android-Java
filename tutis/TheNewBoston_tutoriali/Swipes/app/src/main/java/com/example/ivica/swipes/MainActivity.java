package com.example.ivica.swipes;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    private TextView mojaPorula;
    private TextView mojaPorula2;
    private GestureDetectorCompat gestureDetektor;
    Button mojBotun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mojBotun = (Button)findViewById(R.id.BALUN);
        mojaPorula = (TextView) findViewById(R.id.Massager);
        mojaPorula2 = (TextView)findViewById(R.id.textView2);
        this.gestureDetektor = new GestureDetectorCompat(this,this);
        gestureDetektor.setOnDoubleTapListener(this);

        mojBotun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mojaPorula2.setText("Kliknut");
            }
        });
    }

    //  alt+insert  -- gesture


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetektor.onTouchEvent(event);   //prvo gleda da li je scroll ili nesto
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mojaPorula.setText("onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mojaPorula.setText("onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        mojaPorula.setText("onDoubleTapEvent");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mojaPorula.setText("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        mojaPorula.setText("onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mojaPorula.setText("onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mojaPorula.setText("onScroll");
        mojaPorula2.setText("SWIPEAN");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mojaPorula.setText("onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mojaPorula.setText("onFling");
        return true;
    }
}
