package com.example.ivica.clumsybox.GameView.TopLayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;

/**
 * Created by Ivica on 17.8.2017..
 */

public class Level implements GameObject {

    private int level = 1;
    private float padding;

    public Level(){
        padding = (float)0.75 * Constants.objectFrameWidth;
    }

    public void updateScore(){
        level++;
    }

    public void setScore(){
        level = 1;
    }

    public int getScore(){
        return level;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

        /*
        Paint paint = new Paint();
        paint.setTextSize(Constants.objectFrameWidth * 0.5f);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.isFakeBoldText();
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Level: " + level, 3, paint.descent() - paint.ascent() + padding, paint);
        */
    }
}
