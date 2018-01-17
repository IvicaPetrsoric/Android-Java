package com.example.ivica.clumsybox.GameView.MidLayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.Loading.Loading;

/**
 * Created by Ivica on 22.4.2017..
 */

public class BackgroundEnd implements GameObject{

    private int alphaBack;
    private boolean enableAnim;
    private boolean toBlack;
    private boolean enableFading;
    private long timeDelay;

    public void activateBackground(boolean show){

        enableAnim = true;
        enableFading = true;

        if (show){
            alphaBack = 255;

            toBlack = false;
        }else {
            alphaBack = 0;

            toBlack = true;
        }
    }

    private void animAlpha(){
        // zacrni
        if (toBlack){

            if (alphaBack < 245){
                alphaBack += 3;
            }
            else {
                alphaBack = 255;
                enableAnim = false;
                openLoadingScene();

                //enableFading = false;
            }
        }

        // makni crnilo
        else{

            if (alphaBack > 10){
                alphaBack -= 3;
            }
            else {
                alphaBack = 0;
                enableAnim = false;
                enableFading = false;
            }
        }
    }

    private void openLoadingScene(){
        Intent intent = new Intent(Constants.CURRENT_CONTEXT,Loading.class);
        intent.putExtra("destination", "menu");
        Constants.CURRENT_CONTEXT.startActivity(intent);
        ((Activity)Constants.CURRENT_CONTEXT).overridePendingTransition(0,0);
        ((Activity)Constants.CURRENT_CONTEXT).finish();
    }

    public boolean status(){
        return enableFading;
    }

    @Override
    public void update() {

        if (enableAnim){
            long elapsed_2 = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed_2 > 5){
                //System.out.println("DA");

                animAlpha();
                timeDelay = System.nanoTime();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // pozadina kada se aktiviraju dugmici
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(alphaBack);

        canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, paint);
    }

}
