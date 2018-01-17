package com.example.ivica.clumsybox.GameView.TopLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 18.4.2017..
 */

public class Lifes implements GameObject{

    private Bitmap life;
    private Bitmap res;
    private int lifeWidth;
    private int lifePozX;
    private int lifePozY;
    private float resizeCounter = 1;
    public boolean updateCanvas = true;
    private long timeDelay;
    private boolean enableResize = false;

    public Lifes(int potX){

        this.lifeWidth = Constants.objectFrameWidth;
        lifePozY = (int)(lifeWidth * 0.1f);

        lifePozX =  3 + (int)((lifeWidth * 2.5f) + potX * lifeWidth * 0.65);

        res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.life);
        life = Bitmap.createScaledBitmap(res,(int)(lifeWidth * 0.6), (int)(lifeWidth * 0.6),false);
    }

    public void prepareResizeLife(){
        resizeCounter = 0.6f;
        enableResize = true;

        resizeLife();
    }

    //  Animacija smanjenja igrača
    private void resizeLife(){
        lifeWidth = (int)(lifeWidth * resizeCounter);

        resizeCounter = (float) (resizeCounter - 0.005);

        lifePozX =  lifePozX + (int)(Constants.SCREEN_WIDTH_10 * 0.05) ;
        lifePozY =  lifePozY + (int)(Constants.SCREEN_WIDTH_10 * 0.05) ;

        if (resizeCounter == 0.1){

            updateCanvas = false;
        }

        if (lifeWidth > 0){
            life = Bitmap.createScaledBitmap(res, lifeWidth, lifeWidth, false);
        }else {
            enableResize = false;
            //life.recycle();
        }
    }

    public void recycleLife(){
        life.recycle();
    }

    @Override
    public void update() {

        if (enableResize){
            long elapsed = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed > 30){
                timeDelay = System.nanoTime();
                resizeLife();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (updateCanvas) {
            canvas.drawBitmap(life, lifePozX, lifePozY, null);
        }
    }

    public void recycle(){
        life.recycle();
        res.recycle();
    }

}
