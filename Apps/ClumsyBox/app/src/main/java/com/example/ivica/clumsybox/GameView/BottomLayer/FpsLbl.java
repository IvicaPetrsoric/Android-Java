package com.example.ivica.clumsybox.GameView.BottomLayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;

/**
 * Created by Ivica on 26.4.2017..
 */

public class FpsLbl implements GameObject {

    public double fps;

    public void setFps(double fps){
        this.fps = fps;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(Constants.objectFrameWidth * 0.6f);
        paint.setColor(Color.GRAY);
        paint.setFakeBoldText(true);
        paint.isFakeBoldText();
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("FPS: " + (int)fps, Constants.objectFrameWidth / 10, 0 + paint.descent() - paint.ascent() + Constants.SCREEN_HEIGHT - Constants.objectFrameWidth , paint );
    }
}
