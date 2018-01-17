package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 17.4.2017..
 */

public class Wall implements GameObject {

    private Bitmap wallBox;
    private int wallBoxPozX = 0;
    private float wallBoxPozY;
    private int wallBoxW;
    private int alphaValue = 255;
    private float grayBoxHeight = 1f;
    private float grayBoxPozY;
    private float minGrayBoxHeight = 0.5f;
    private float rndWallPozY;

    public Wall(int currentBox, float rndWallPozY, boolean wallAndBoxLined){
        this.rndWallPozY = rndWallPozY;
        this.wallBoxW = (int) (Constants.objectFrameWidth * grayBoxHeight);

        // postavljanje  pozicije zida po Y
        wallBoxPozY =  rndWallPozY * Constants.SCREEN_HEIGHT;

        grayBoxPozY = wallBoxPozY;

        // postavljnje X pozicije kutija unutar zida
        wallBoxPozX = currentBox * (Constants.SCREEN_WIDTH_10);

        Bitmap res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player0);
        this.wallBox = Bitmap.createScaledBitmap(res, wallBoxW, wallBoxW, false);

        //rndPozY();

        changeAlpha(wallAndBoxLined);
    }

    public void newWallPozX(int currentBox, float rndWallPozY, int playerPozX){
        this.rndWallPozY = rndWallPozY;
        // treba osloboditi poziciju di se krece box
        if (playerPozX == currentBox){
            //currentBox++;
            changeAlpha(true);
        }
        else{
            changeAlpha(false);
        }
        // postavljanje  pozicije zida po Y
        wallBoxPozY =  rndWallPozY * Constants.SCREEN_HEIGHT;

        // postavljnje X pozicije kutija unutar zida
        wallBoxPozX = currentBox * (Constants.SCREEN_WIDTH_10);
    }

    public void updateGrayBoxHeight(){
        grayBoxHeight -= 0.05;

        recycle();
        // new Box
        this.wallBoxW = (int) (Constants.objectFrameWidth * grayBoxHeight);
        Bitmap res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player0);
        this.wallBox = Bitmap.createScaledBitmap(res, wallBoxW, wallBoxW, false);

        wallBoxPozY =  rndWallPozY * Constants.SCREEN_HEIGHT + wallBoxW / 2;

        grayBoxPozY = wallBoxPozY;
    }

    public float getGrayBoxPozY(){
        return grayBoxPozY;
    }

    public float getGrayBoxHeight(){
        return grayBoxHeight;
    }

    public float getMingGrayBoxHeight(){
        return minGrayBoxHeight;
    }

    private void changeAlpha(boolean fade){
        alphaValue = 255;
        if (fade) {
            alphaValue = 255/2;
        }
    }

    public float getWallPozY(){
        return wallBoxPozY;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(alphaValue);
        canvas.drawBitmap(wallBox, wallBoxPozX, wallBoxPozY, paint);
    }

    @Override
    public void update() {
    }

    public void recycle(){
        wallBox.recycle();
    }
}
