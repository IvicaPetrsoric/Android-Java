package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 28.3.2017..
 */

public class Background implements GameObject {

    private Bitmap backgroundImage;

    public Background(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;

        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap res = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.game_view, null);
        this.backgroundImage = Bitmap.createScaledBitmap(res, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);
    }

    @Override
    public void draw(Canvas canvas){
        // 0, 0 = x,y = gornji lijevi kut!
        if (!backgroundImage.isRecycled()){
            canvas.drawBitmap(backgroundImage, 0, 0, null);
        }
    }

    @Override
    public void update() {
    }

    // čišćenje rama
    public void backgroundRecyle(){
        backgroundImage.recycle();
    }
}
