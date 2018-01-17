package com.example.ivica.clumsybox.GameView.MidLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.GameObject;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 17.4.2017..
 */

public class ScoreLine implements GameObject {

    private Bitmap scoreLine;

    private float scoreLinePozy;

    public ScoreLine(float scoreLinePozy){

        this.scoreLinePozy = scoreLinePozy;

        scoreLine = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.score_line);
        scoreLine = Bitmap.createScaledBitmap(scoreLine,Constants.SCREEN_WIDTH, Constants.objectFrameWidth,false);
    }

    public void updateScoreLinePozY(float scoreLinePozy){
        this.scoreLinePozy = scoreLinePozy * Constants.SCREEN_HEIGHT;
    }

    public void recycleBitmap(){
        scoreLine.recycle();
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(scoreLine, 0, scoreLinePozy, null);
    }

    public void recycle(){
        scoreLine.recycle();
    }
}
