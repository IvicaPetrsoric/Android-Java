package com.example.ivica.clumsybox.GameView.TopLayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;

/**
 * Created by Ivica on 18.4.2017..
 */

public class ScoreLbl implements GameObject {

    private int score = 0;

    public ScoreLbl(){
        String customFont = "Carrington.ttf";
    }


    public void updateScore(){
        score++;
    }

    public void setScore(){
        score = 0;
    }

    public int getScore(){
        return score;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(Constants.objectFrameWidth * 0.6f);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.isFakeBoldText();
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("" + score, Constants.SCREEN_WIDTH -  1.05f *Constants.objectFrameWidth, 0 + paint.descent() - paint.ascent(), paint );
    }

}
