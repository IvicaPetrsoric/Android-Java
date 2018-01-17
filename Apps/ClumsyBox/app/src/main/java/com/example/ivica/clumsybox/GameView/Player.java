package com.example.ivica.clumsybox.GameView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;

import java.util.Random;

/**
 * Created by Ivica on 28.3.2017..
 */

public class Player implements GameObject {

    private Bitmap playerImage;
    private Bitmap res;
    private int playerX = 0;
    private float playerY = 0;
    private int playerW;

    // do nekih 30 je min
    // početno 200
    private float faktorPovećanja = 205;
    private float  velocity;

    // counter za resize player, ide do 0.5
    private float resizeCounter = 1;


    // timeri
    private long timeDelay;

    private float startPozY = 0.15f;

    Random rnd = new Random();

    private int rndPlayerX;

    private enum Velocity{
        UP,
        DOWN,
    }

    Velocity playerVelocity;


    public enum UpdateTask{
        POSITION,
        SIZE,
        NO_UPDATE,
    }

    UpdateTask updateTask = UpdateTask.NO_UPDATE;


    // kkroz konstruktor podešavanje pozadine
    public Player(){
        updateTask = UpdateTask.NO_UPDATE;
        res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player0);
        newPozX();
        createPlayer(rndPlayerX);
    }

    //  stvori igrača
    private void createPlayer(int rndPlayerPozX){

        updateTask = UpdateTask.POSITION;
        playerW = Constants.objectFrameWidth;
        playerX = rndPlayerPozX * (Constants.SCREEN_WIDTH_10);
        playerY = (startPozY * Constants.SCREEN_HEIGHT);
        playerImage = Bitmap.createScaledBitmap(res, playerW, playerW, false);
        playerVelocity = Velocity.DOWN;

        velocity = (Constants.SCREEN_HEIGHT / faktorPovećanja);

        updateCanvas = true;
    }

    public void prepareResize(){
        resizeCounter = 1;
        updateTask = UpdateTask.SIZE;
        resizePlayer();
    }

    //  Animacija smanjenja igrača
    private void resizePlayer(){

        playerW = (int)(playerW * resizeCounter);

        resizeCounter = (float) (resizeCounter - 0.05);

        if (playerW > 0){
            playerImage = Bitmap.createScaledBitmap(res, playerW, playerW, false);
        }

        playerX =  playerX + (int)(Constants.SCREEN_WIDTH_10 * 0.05) ;
        playerY =  playerY + (int)(Constants.SCREEN_WIDTH_10 * 0.05) ;

        if (resizeCounter == 0.5){

            updateCanvas = false;
        }
    }

    private boolean updateCanvas = true;

    // promjena pozicije igrača po X
    public void newPozX(){
        //playerRecycle();
        rndPlayerX = rnd.nextInt(8);

        createPlayer(rndPlayerX);
        updateCanvas = true;
    }

    public int getRndPozX(){
        return rndPlayerX;
    }

    public float getPozY(){
        return playerY;
    }

    public float getPozX(){
        return playerX;
    }

    // vraća dal se igrač kreće prema gore ili dole te ovisno o tome odreduje kada ce se bomba stvarati
    public boolean getMoveingDirection(){

        if (playerVelocity.equals(Velocity.DOWN)){
            return true;
        }else {
            return false;
        }
    }

    public void upgradeVelocity(boolean playerAlive){
        // ako je ziv smanjivati
        if (playerAlive){
            if (faktorPovećanja > 35){
                faktorPovećanja -= 15;
                velocity = (Constants.SCREEN_HEIGHT / faktorPovećanja);
            }

            System.out.println("Fakotr brzine: " + faktorPovećanja);

        }
        // ako nije ziv postaviti na početnu vrijednost
        else{
            faktorPovećanja = 205;
            velocity = (Constants.SCREEN_HEIGHT / faktorPovećanja);
        }
    }

    @Override
    public void update() {

        switch (updateTask){
            case POSITION:

                if (playerVelocity.equals(Velocity.DOWN)) {
                    playerY += velocity;
                }else{
                    playerY -= velocity;
                }


                // dolazi do doljnje točke
                if (playerY >= Constants.SCREEN_HEIGHT - playerW){
                    playerVelocity = Velocity.UP;
                    //    faktorPovećanja = faktorPovećanja + 20;
                }
                // dolazi do gornje točke
                else if (playerY <= 0){
                    playerVelocity = Velocity.DOWN;
                }

                break;

            case SIZE:
                long elapsed = (System.nanoTime() - timeDelay) / 1000000;
                //System.out.println("Elapsed: " + elapsed);
                if (elapsed > 30){
                    //  playerRecycle();
                    //  System.out.println("tu");
                    timeDelay = System.nanoTime();
                    resizePlayer();
                }

                break;

            case NO_UPDATE:
                break;

            default:
                break;
        }
    }

    @Override
    public void draw(Canvas canvas){
        // 0, 0 = x,y = gornji lijevi kut!
        if (updateCanvas){
            canvas.drawBitmap(playerImage, playerX, playerY, null);
            //canvas.drawBitmap(playerImage, null, new RectF(playerX, playerY, playerW, playerW), null);
        }
    }

    public void recycle(){
        playerImage.recycle();
        res.recycle();
    }

}
