package com.example.ivica.clumsybox.GameView.TopLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 18.4.2017..
 */

public class Coin implements GameObject{

    private Bitmap coins;
    private Bitmap animCoins;
    private int coinPozX;
    private int coinPozY;

    private int coinWidth;

    private boolean enableAnim = false;
    private long timeDelay;

    private int alphaValue = 0;

    public Coin(){
        coinWidth = Constants.objectFrameWidth;

        coinPozX = Constants.SCREEN_WIDTH - coinWidth - 1;
        coinPozY = coinWidth + coinWidth/2;

        Bitmap res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coins);
        coins = Bitmap.createScaledBitmap(res,coinWidth, coinWidth,false);
        animCoins = Bitmap.createScaledBitmap(res,coinWidth, coinWidth,false);
    }

    public void activateAnim(){
        timeDelay = System.nanoTime();
        coinPozY = coinWidth + coinWidth/2;
        enableAnim = true;
        alphaValue = 0;
        startAnim();
    }

    //  Animacija smanjenja igrača
    private void startAnim(){

        coinPozY = coinPozY - coinWidth/10;
        if (alphaValue < 225) {
            alphaValue += 25;
        }else {
            alphaValue = 255;
        }

        //System.out.println(alphaValue);

        if (coinPozY <= -5){
            enableAnim = false;
            enableAnim = false;
        }
    }

    @Override
    public void update() {
        if (enableAnim){
            long elapsed = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed > 15){
                timeDelay = System.nanoTime();
                startAnim();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(alphaValue);

        canvas.drawBitmap(coins, coinPozX, 0, null);

        if (enableAnim){
            canvas.drawBitmap(animCoins, coinPozX, coinPozY ,paint);
        }
    }

    public void recycle(){
        coins.recycle();
        animCoins.recycle();
    }
}
