package com.example.ivica.tranny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    ViewGroup buckLayout;

    private static int Visina,Duzina;
    private boolean prviKlik = false;
    private boolean glavniUlaz = true;

    private  static final  String TAG="buckysMesseges";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"OnCreate");

        buckLayout = (ViewGroup) findViewById(R.id.buckysLayout);


        View buckysButton = findViewById(R.id.buckysButton);
        if (glavniUlaz) {
            Duzina = buckysButton.getMeasuredWidth();
            Visina = buckysButton.getMeasuredHeight();
        }
        glavniUlaz = false;


        buckLayout.setOnTouchListener(
                new RelativeLayout.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        if (prviKlik == false){
                            moveButton();
                        }
                        else {
                            returnButton();
                        }

                        return true;    //handled toutch event
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }﻿

  /*  @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }*/
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(TAG, "onStop");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }
    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }﻿*/

    public void moveButton(){


        View buckysButton = findViewById(R.id.buckysButton);
        TransitionManager.beginDelayedTransition(buckLayout);

        Duzina = buckysButton.getMeasuredWidth();
        Visina = buckysButton.getMeasuredHeight();

        // change the position of button
        //prebacivanje dugmada u doljnjem dijelu
        RelativeLayout.LayoutParams positionRules = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        positionRules.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        positionRules.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        buckysButton.setLayoutParams(positionRules);

        //change the size of button
        ViewGroup.LayoutParams sizeRules = buckysButton.getLayoutParams();
        sizeRules.width = 450;
        sizeRules.height = 300;
        buckysButton.setLayoutParams(sizeRules);    //upotrijebi ovo

        prviKlik = true;
    }

    public void returnButton()
    {

        View buckysButton = findViewById(R.id.buckysButton);
        TransitionManager.beginDelayedTransition(buckLayout);

        // change the position of button
        //prebacivanje dugmada u doljnjem dijelu
        RelativeLayout.LayoutParams positionRules = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        positionRules.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        positionRules.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        buckysButton.setLayoutParams(positionRules);

        //change the size of button
        ViewGroup.LayoutParams sizeRules = buckysButton.getLayoutParams();
        sizeRules.width = 250;
        sizeRules.height = 150;
        buckysButton.setLayoutParams(sizeRules);    //upotrijebi ovo

        prviKlik = !prviKlik;
    }













}
