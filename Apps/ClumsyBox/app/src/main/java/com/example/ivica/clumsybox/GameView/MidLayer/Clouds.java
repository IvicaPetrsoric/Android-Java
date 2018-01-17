package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

import java.util.Random;

/**
 * Created by Ivica on 18.4.2017..
 */

public class Clouds implements GameObject {

    private Bitmap obstacle;
    private Bitmap res;
    private int obstacleW;
    Random rnd = new Random();

    //  margine zida
    private float maxWallPozY = (float) 0.75;
    private float minWallPozY = (float) 0.15;

    private float obstaclePozY;
    private float obstaclePozX;

    private boolean sideStart;

    private long timeDelay;

    private int velocity;


    private float maxUpdate = (float) 0.35;
    private float minUpdate = (float) 0.25;
    private int rndTimeUpdate;

    public Clouds(){

        obstacleW = Constants.objectFrameWidth * 2;
        rndSidePoz();
        rndCloud();

    }

    private void rndCloud(){
        enableUpdate = false;
        //obstacle.recycle();

        rndPozY();

        rndVelocity();
        rndImg();
        enableUpdate = true;
    }

    private void rndImg(){
        int rndCloud = rnd.nextInt(7);

        switch (rndCloud){

            case 0:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud1);
                break;

            case 1:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud2);
                break;

            case 2:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud3);
                break;

            case 3:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud4);
                break;

            case 4:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud5);
                break;

            case 5:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud6);
                break;

            case 6:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud7);
                break;

            default:
                break;
        }

        obstacle = Bitmap.createScaledBitmap(res,obstacleW, obstacleW,false);
    }

    private void rndPozY(){
        obstaclePozY = rnd.nextFloat() * (maxWallPozY - minWallPozY) + minWallPozY;
        obstaclePozY = obstaclePozY * Constants.SCREEN_HEIGHT;
    }

    private void rndSidePoz(){
        int rndNumb = rnd.nextInt(2);

        if (rndNumb == 0){
            sideStart =  true;
            obstaclePozX = -obstacleW;
        }else {
            obstaclePozX = Constants.SCREEN_WIDTH + obstacleW;
            sideStart =  false;
        }
    }

    private void rndVelocity(){
        velocity = rnd.nextInt(2) + 1;
        //elocity = 1;
        rndTimeUpdate = (int)((rnd.nextFloat() * (maxUpdate - minUpdate) + minUpdate) * 100);
        //System.out.println("Rnd Update : " + rndTimeUpdate);
    }

    //  Animacija smanjenja igrača
    private void startAnim(){
        //  S lijeve na desnu
        if (sideStart){
            obstaclePozX += velocity;
        }
        //  S desne na lijevu
        else{
            obstaclePozX -= velocity;
        }

        if (obstaclePozX > Constants.SCREEN_WIDTH){
            rndCloud();
            sideStart =  false;
        }
        else if (obstaclePozX < -Constants.objectFrameWidth * 2){
            rndCloud();
            sideStart =  true;
        }
    }

    private boolean enableUpdate = true;

    @Override
    public void update() {

        if (enableUpdate){
            long elapsed = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed > rndTimeUpdate){
                timeDelay = System.nanoTime();
                startAnim();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (enableUpdate){
            canvas.drawBitmap(obstacle, obstaclePozX, obstaclePozY, null);
        }
    }

    public void recycle(){
        obstacle.recycle();
        res.recycle();
    }

}
