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

public class ScoreBar implements GameObject {

    private Bitmap scoreBar;
    private Bitmap scoreFrame;

    private int scoreWidth;

    private float scoreBarLen;
    private float scoreBarPozX;

    private long timeDelay;

    public ScoreBar(){
        scoreWidth = Constants.objectFrameWidth;
        scoreBarPozX = - (float)2.5 * scoreWidth;

        scoreBar = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.score_bar);
        scoreBar = Bitmap.createScaledBitmap(scoreBar,(int)(scoreWidth * 2.5), (int)(scoreWidth * 0.8),false);

        scoreFrame = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.score_bar_frame);
        scoreFrame = Bitmap.createScaledBitmap(scoreFrame,(int)(scoreWidth * 2.5), (int)(scoreWidth * 0.8),false);
    }

    public void updateScoreBarW(boolean upgSize){
        if (upgSize){
            if (scoreBarLen < (2.5) ){

                scaleUp = true;
                timeDelay = System.nanoTime();
                scoreBarLen = scoreBarLen + 0.5f;
                if (currentLen < scoreBarLen){
                    enableResize = true;
                }
            }
        }
        else{
            timeDelay = System.nanoTime();
            scaleUp = false;
        }

        enableResize = true;
    }

    private float currentLen = 0;
    private boolean scaleUp = true;

    // postepeno povećanje duzine bara
    private void resizeAnim(){
        //  POVECANJE
        if (scaleUp){
            if (currentLen < scoreBarLen){


                currentLen = currentLen + 0.1f;

                scoreBarPozX = scoreBarPozX + (int)(0.1f * scoreWidth);

                if (currentLen >= 2.4){
                    scoreBarPozX = 0;
                }
                //System.out.println(currentLen);
                //System.out.println(scoreBarLen);

            }else{
                enableResize = false;
                currentLen = scoreBarLen;

            }
        }
        //  SMANJENJE
        else {

            if (scoreBarLen >= -2.5){
                scoreBarLen -= 0.20;
                scoreBarPozX = scoreBarPozX - (int)(0.20f * scoreWidth);
            }
            else{
                scoreBarPozX = - (float)2.5 * scoreWidth;
                scoreBarLen = 0;
                currentLen = 0;
                enableResize = false;
            }
        }
    }

    public void recycleBitmap(){
        scoreBar.recycle();
        scoreFrame.recycle();
    }

    private boolean enableResize = false;

    @Override
    public void update() {
        if (enableResize){
            long elapsed = (System.nanoTime() - timeDelay) / 1000000;
            //System.out.println("Elapsed: " + elapsed);
            if (elapsed > 15){
                timeDelay = System.nanoTime();
                resizeAnim();
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(scoreBar, scoreBarPozX, 0, null);
        canvas.drawBitmap(scoreFrame, 0, 0, null);
    }

    public float getScoreWidth(){
      return  scoreBarLen;
    }


    public void recycle(){
        scoreBar.recycle();
        scoreFrame.recycle();
    }


}