package com.example.ivica.clumsybox.GameView.Animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 21.4.2017..
 */

public class Explosion implements GameObject {

    private int x;
    private int y;
    private int cuttedWidth;
    private int cuttedHeight;
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    Bitmap[] image = new Bitmap[16];

    public Explosion(){
        spritesheet =  BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.explosion);

        spritesheet = Bitmap.createScaledBitmap(spritesheet, 5 * Constants.objectFrameWidth, 5 * Constants.objectFrameWidth, false);

        cuttedWidth =  spritesheet.getWidth() / 4;
        cuttedHeight = spritesheet.getHeight() / 4;

        for(int i = 0; i<image.length; i++)
        {
            if(i%4==0 && i>0)
                row++;

            //System.out.println("X: " + (i-(5*row)) * cuttedWidth);
            //System.out.println("Y: " + row * cuttedWidth);
            image[i] = Bitmap.createBitmap(spritesheet, (i-(4*row)) * cuttedWidth, row * cuttedHeight, cuttedWidth, cuttedHeight);

        }

        animation.setFrames(image);
    }

    public void setExplosion( int x, int y) {
        this.x = x;
        this.y = y;

        animation.playedOnce = false;

        animation.setDelay(10);
        animation.setFrame(0);
    }

    public void draw(Canvas canvas) {
        if(!animation.playedOnce())
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }
    }

    public void update() {
        if(!animation.playedOnce())
        {
            animation.update();
        }
    }

    public void recycle(){
        for(int i = 0; i<image.length; i++){
            image[i].recycle();
        }
        animation.recycle();
    }
}
