package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;

/**
 * Created by Ivica on 19.4.2017..
 */

public class Counter implements GameObject {

    private int counter;
    private long timeDelay;
    private long timeDelay_2;
    private int alphaValue;

    public void startCounter(){
        counter = 4;
        enableDraw = true;
        counterY = 0.1f;
        alphaValue = 255;
        timeDelay = System.nanoTime();
        timeDelay_2 = System.nanoTime();
    }

    private void animAlpha(){
        if (alphaValue > 50) {

            alphaValue -= 15;
        }else {
            alphaValue = 0;
            enableAnim = false;
        }
    }

    private boolean enableAnim = false;

    @Override
    public void update() {
        long elapsed = (System.nanoTime() - timeDelay) / 1000000;
        if (elapsed > 750){
            timeDelay = System.nanoTime();
            timeDelay_2 = System.nanoTime();
            counter--;
            enableAnim = true;
            alphaValue = 255;
            counterY += 0.07;

            if (counter == -1){
                enableDraw = false;
            }
        }

        if (enableAnim){
            long elapsed_2 = (System.nanoTime() - timeDelay_2) / 1000000;
            if (elapsed_2 > 35){
                //System.out.print("ALPHA");
                timeDelay_2 = System.nanoTime();
                animAlpha();
            }
        }
    }

    public boolean timerActive(){
        if (counter < 0){
            return false;
        }else {
            return true;
        }
    }

    private boolean enableDraw;
    private float counterY;

    @Override
    public void draw(Canvas canvas) {
        if (enableDraw) {
            Paint paint = new Paint();

            paint.setTextSize(Constants.objectFrameWidth * 1.2f);
            paint.setColor(Color.WHITE);
            paint.setAlpha(alphaValue);
            paint.setFakeBoldText(true);
            paint.isFakeBoldText();
            paint.setTextAlign(Paint.Align.CENTER);
            if (counter < 4 && counter > 0){
                canvas.drawText("" + counter, Constants.SCREEN_WIDTH / 2, (Constants.SCREEN_HEIGHT * counterY)  + paint.descent() - paint.ascent(), paint);
            }
            else if (counter == 0){
                canvas.drawText("GO!", Constants.SCREEN_WIDTH / 2, (Constants.SCREEN_HEIGHT * counterY)  + paint.descent() - paint.ascent(), paint);
            }
        }
    }
}
