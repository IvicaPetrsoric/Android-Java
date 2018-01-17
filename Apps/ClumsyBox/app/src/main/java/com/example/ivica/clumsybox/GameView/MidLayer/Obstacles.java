package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;


import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

import java.util.Random;


/**
 * Created by Ivica on 18.4.2017..
 */

public class Obstacles implements GameObject {

    private Bitmap obstacle;
    private Bitmap res;
    private int obstacleW;
    private int obstaclePozX;
    private int obstaclePozY;

    Random rnd = new Random();

    private boolean enableBomb;
    private long timeDelay;

    private enum BombPosition{
        UP,
        DOWN,
    }

    BombPosition bombPosition;

    private boolean rorateCW;

    public Obstacles(){
        obstacleW = Constants.objectFrameWidth;
    }

    public boolean showobstacle(){
       // if (rnd.nextInt(2) == 0){
            enableBomb = true;
            rorateCW = true;

            if (rnd.nextInt(2) == 0){
                rorateCW = false;
            }

        /*}else {
            enableBomb = false;
        }*/

        System.out.println("Stvori bombu: " + enableBomb);

        return enableBomb;
    }

    public boolean bombPozition(){
        if (rnd.nextInt(2) == 0){
            bombPosition = BombPosition.UP;
            return true;

        }else {
            bombPosition = BombPosition.DOWN;
            return false;
        }
    }

    public void prepareAnim(boolean showUp, int playerPozX){
        rndImg();
        rotateDegree = 0;
        obstaclePozX = playerPozX;
        enableUpdate = true;

        if (showUp){
            obstaclePozY = -obstacleW;
        }else{
            obstaclePozY = Constants.SCREEN_HEIGHT + obstacleW;
        }
    }

    private void rndImg() {
        int rndCloud = rnd.nextInt(3);

        switch (rndCloud) {

            case 0:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bomb1);
                break;

            case 1:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bomb2);
                break;

            case 2:
                res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bomb3);
                break;

            default:
                break;
        }

        obstacle = Bitmap.createScaledBitmap(res,obstacleW, obstacleW,false);
    }

    //  Animacija smanjenja igrača
    private void startAnim(){
        //  S gore na dole
        if (bombPosition.equals(BombPosition.UP)){
            obstaclePozY = obstaclePozY + (int)(obstacleW * 0.2f);

            if (obstaclePozY  > (int)(0.1f * Constants.SCREEN_HEIGHT)){
                enableUpdate = false;
            }
        }
        //  S dole na gore
        else{
            obstaclePozY = obstaclePozY - (int)(obstacleW * 0.2f);
            if (obstaclePozY  < (int)(0.8f * Constants.SCREEN_HEIGHT)){
                enableUpdate = false;
            }
        }
    }

    public boolean checkColision(float playerPozY){

        // bomba se nalazi gore
        if (bombPosition.equals(BombPosition.UP)){
            if (playerPozY < (obstaclePozY + obstacleW / 2)){
                return true;
            }
            else {
                return false;
            }
        }else{
            if ((playerPozY + obstacleW / 2) > (obstaclePozY)){
                return true;
            }else{
                return false;
            }
        }
    }

    private boolean enableUpdate = false;
    private int rotateDegree = 0;
    private long rotateTimer;

    @Override
    public void update() {
        if (enableUpdate){
            long elapsed = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed > 15){
                timeDelay = System.nanoTime();
                startAnim();
            }
        }

        long elapsed = (System.nanoTime() - rotateTimer) / 1000000;
        if (elapsed > 150){
            //System.out.println("AAAA");
            rotateTimer = System.nanoTime();
            obstacle = rotateImage(res,rotateDegree);

            if (rorateCW){
                rotateDegree += 1;
            }else{
                rotateDegree -= 1;
            }
        }
    }

    private Bitmap rotateImage(Bitmap src,int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createScaledBitmap(Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true),obstacleW, obstacleW,false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(obstacle, obstaclePozX, obstaclePozY, null);
    }

    public void recycle(){
        try {
            res.recycle();

            if (obstacle != null){
                obstacle.recycle();
            }
        }catch (Exception e){}

    }

}
