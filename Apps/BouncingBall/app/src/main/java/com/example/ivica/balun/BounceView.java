package com.example.ivica.balun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by Ivica on 14.3.2016..
 */
public class BounceView extends View {

    int availableWidth;
    int availableHeight;
    float positionX;
    float positionY;
    int speedX;
    int speedY;
    float ballRadius;
    private ballInWallListener myBallInWallListener;
    Paint myPaint;
    int currentBallColor;
    Bitmap ballBitmap;

    public interface ballInWallListener {
        void onBallInWall();
    }

    public void setBallInWallListener(ballInWallListener inputListener){
        this.myBallInWallListener = inputListener;
    }

    public BounceView(Context context) {
        super(context);
        positionX =0;
        positionY =0;
        speedX = 0;
        speedY = 0;
        //ballRadius = 30f;
        myPaint = new Paint();
        setWillNotDraw(false);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ballv2);
    }

    public void setSpeed(int x, int y, boolean gameStart){
        if (gameStart){
            this.speedX = x;
            this.speedY = y;
            return;
        }

        if (this.speedX < 0) {
            this.speedX = -x;
        } else {
            this.speedX = x;
        }
        if (this.speedY < 0) {
            this.speedY = -y;
        } else {
            this.speedY = y;
        }
    }

    public void setStartVector() {
        if (this.speedX < 0) {
            this.speedX = -this.speedX;
        }
        if (this.speedY < 0) {
            this.speedY = -this.speedY;
        }
    }

    public void setBallPosition(int x, int y){
        this.positionX = x;
        this.positionY = y;
    }

    public void setBallRadius(float radius){
        this.ballRadius = radius;
        int d = Math.round(radius * 20);
        ballBitmap = Bitmap.createScaledBitmap(ballBitmap, d, d, false);
    }


    public void setBallInMiddle(){
        this.positionX = this.availableWidth / 2;
        this.positionY = this.availableHeight / 2;
    }

    public void setBallColor(int inputColor){
        this.currentBallColor = inputColor;
    }

    public void MoveBall(){
        positionX = positionX + speedX;
        positionY = positionY + speedY;

        if (positionX + ballRadius > availableWidth) {
            positionX = availableWidth - ballRadius;
            speedX = -speedX;

            if (myBallInWallListener!=null)
                myBallInWallListener.onBallInWall();
        }

        if (positionX - ballRadius < 0) {
            positionX = 0 + ballRadius;
            speedX = -speedX;

            if (myBallInWallListener!=null)
                myBallInWallListener.onBallInWall();
        }

        if (positionY + ballRadius > availableHeight) {
            positionY = availableHeight - ballRadius;
            speedY = -speedY;

            if (myBallInWallListener!=null)
                myBallInWallListener.onBallInWall();
        }

        if (positionY - ballRadius < 0) {
            positionY = 0 + ballRadius;
            speedY = -speedY;

            if (myBallInWallListener!=null)
                myBallInWallListener.onBallInWall();
        }
    }

    public void invokeCommand(int command) {
        if (command == 1) {
            if (speedX < 0) return;
            speedX = -speedX;
        }

        if (command == 2) {
            if (speedY < 0) return;
            speedY = -speedY;
        }

        if (command == 3) {
            if (speedY > 0) return;
            speedY = -speedY;
        }

        if (command == 4) {
            if (speedX > 0) return;
            speedX = -speedX;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.availableWidth = w;
        this.availableHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        myPaint.setColor(Color.WHITE);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        // bouncing box:
        myPaint.setStrokeWidth(5f);
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        // ball:
        myPaint.setColor(this.currentBallColor);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //canvas.drawCircle(positionX, positionY, ballRadius, myPaint);
        canvas.drawBitmap(ballBitmap, positionX - ballRadius, positionY - ballRadius, myPaint);
    }

}
