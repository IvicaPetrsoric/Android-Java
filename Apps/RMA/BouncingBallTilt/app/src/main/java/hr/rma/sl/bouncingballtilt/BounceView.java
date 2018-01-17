package hr.rma.sl.bouncingballtilt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class BounceView extends View {

    int availableWidth;
    int availableHeight;
    float positionX;
    float positionY;
    int speedX;
    int speedY;
    float ballRadius;
    private collisionListener myCollisionListener;
    Paint myPaint;
    int currentBallColor;
    Bitmap ballBitmap, addLifeBitmap;

    Random r;
    ArrayList myObstacles;
    ArrayList myObstacleExpireTimes;
    long currentTime = 0;
    long LIFE_UP_EXPIRATION_TIME = 6000;
    boolean firstDraw = true;



    public interface collisionListener {
        void onCollision(String cause);
    }

    public void setCollisionListener(collisionListener inputListener){
        this.myCollisionListener = inputListener;
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
        addLifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        r = new Random();
        myObstacles = new ArrayList();
        myObstacleExpireTimes = new ArrayList();
    }


    public BounceView(Context context, AttributeSet attrs){
        super(context, attrs);
        positionX =0;
        positionY =0;
        speedX = 0;
        speedY = 0;
        //ballRadius = 30f;
        myPaint = new Paint();
        setWillNotDraw(false);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ballv2);
        addLifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        r = new Random();
        myObstacles = new ArrayList();
        myObstacleExpireTimes = new ArrayList();
        //ballBitmap = Bitmap.createScaledBitmap(ballBitmap, 60, 60, false);
    }


    public void reset_upLife_Moments() {
        // clear all regarding existing obstacles:
        myObstacles.clear();
        myObstacleExpireTimes.clear();

        // Let's generate times for life-up opportunities.
        // Let's assume 10 such opportunities in random ranges:
        // [0-10]s, [11-20]s, [21-30]s, ..., [91-100]s
        Random r = new Random();
        for (int i=0; i<10; i++) {
            int timeToShow = r.nextInt(10) + i*10 + 1;
            timeToShow = 1000 * timeToShow;
            System.out.println(timeToShow);
            add_obstacle((long)timeToShow);
        }
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


    public void setFirstDraw(boolean firstdraw){
        this.firstDraw = firstdraw;
    }


    public void setBallPosition(int x, int y){
        this.positionX = x;
        this.positionY = y;
    }


    public void setBallRadius(float radius){
        this.ballRadius = radius;
        int d = Math.round(radius * 2);
        ballBitmap = Bitmap.createScaledBitmap(ballBitmap, d, d, false);

        addLifeBitmap = Bitmap.createScaledBitmap(addLifeBitmap, d * 2, d * 2, false);
    }


    public void setBallInMiddle(){
        this.positionX = this.availableWidth / 2;
        this.positionY = this.availableHeight / 2;
    }


    public void setBallColor(int inputColor){
        this.currentBallColor = inputColor;
    }


    public void MoveBall(long currentTime){
        this.currentTime = currentTime;

        positionX = positionX + speedX;
        positionY = positionY + speedY;

        if (positionX + ballRadius > availableWidth) {
            positionX = availableWidth - ballRadius;
            speedX = -speedX;

            if (myCollisionListener !=null)
                myCollisionListener.onCollision("wall");
        }

        if (positionX - ballRadius < 0) {
            positionX = 0 + ballRadius;
            speedX = -speedX;

            if (myCollisionListener !=null)
                myCollisionListener.onCollision("wall");
        }

        if (positionY + ballRadius > availableHeight) {
            positionY = availableHeight - ballRadius;
            speedY = -speedY;

            if (myCollisionListener !=null)
                myCollisionListener.onCollision("wall");
        }

        if (positionY - ballRadius < 0) {
            positionY = 0 + ballRadius;
            speedY = -speedY;

            if (myCollisionListener !=null)
                myCollisionListener.onCollision("wall");
        }


        int obstacleIndex = isBallInObstacle();
        if (obstacleIndex != -1){
            // we found obstacle that has been hit within regular time,
            // we can remove it from canvas, but we have earned life-up also!
            remove_obstacle(obstacleIndex);

            // raise event:
            if (myCollisionListener !=null)
                myCollisionListener.onCollision("obstacle");
        }

    }


    public int isBallInObstacle(){
        if (myObstacles.size() <=0)
            return -1;

        // Here we should have a collection with valid obstacles only.
        // Apatr position, we must be aware of times also!
        for(int i = 0; i < myObstacles.size(); i++)
        {
            long beginTime = (long)myObstacleExpireTimes.get(i);
            long endTime = beginTime + LIFE_UP_EXPIRATION_TIME;
            if (!((this.currentTime >= beginTime) &&
                    (this.currentTime <= endTime))) {
                continue;
            }

            PointF iObstacle = (PointF)myObstacles.get(i);
            float leftBorder = iObstacle.x - ballRadius * 2;
            float rightBorder = iObstacle.x + ballRadius * 2;
            float upperBorder = iObstacle.y - ballRadius * 2;
            float bottomBorder = iObstacle.y + ballRadius * 2;

            if ((positionX + ballRadius >= leftBorder) &&
                    (positionX + ballRadius <= rightBorder) &&
                    (positionY + ballRadius >= upperBorder) &&
                    (positionY + ballRadius <= bottomBorder))
            {
                return i;
            }

            if ((positionX - ballRadius >= leftBorder) &&
                    (positionX - ballRadius <= rightBorder) &&
                    (positionY - ballRadius >= upperBorder) &&
                    (positionY - ballRadius <= bottomBorder))
            {
                return i;
            }

            if ((positionX - ballRadius >= leftBorder) &&
                    (positionX - ballRadius <= rightBorder) &&
                    (positionY + ballRadius >= upperBorder) &&
                    (positionY + ballRadius <= bottomBorder))
            {
                return i;
            }

            if ((positionX + ballRadius >= leftBorder) &&
                    (positionX + ballRadius <= rightBorder) &&
                    (positionY - ballRadius >= upperBorder) &&
                    (positionY - ballRadius <= bottomBorder))
            {
                return i;
            }
        }

        return -1;
    }


    public void add_obstacle(long momentToShow){
        float minX = ballRadius * 2;
        float maxX = (float)availableWidth - ballRadius * 2;
        float minY = ballRadius * 2;
        float maxY = (float)availableHeight - ballRadius * 2;

        float random_x = r.nextFloat() * (maxX - minX) + minX;
        float random_y = r.nextFloat() * (maxY - minY) + minY;

        PointF newPoint = new PointF(random_x, random_y);
        myObstacles.add(newPoint);
        myObstacleExpireTimes.add(momentToShow);
    }


    public void remove_obstacle(int obstacleToRemove){
        // Index-based removal if obstacle collision in drawing thread is detected
        if (myObstacles.size() > 0){
            myObstacles.remove(obstacleToRemove);
            myObstacleExpireTimes.remove(obstacleToRemove);
            //this.invalidate();
        }
    }


    public void invokeCommand(int command){
        if (command==1) {
            if (speedX <0) return;
            speedX = -speedX;
        }

        if (command==2) {
            if (speedY <0) return;
            speedY = -speedY;
        }

        if (command==3) {
            if (speedY >0) return;
            speedY = -speedY;
        }

        if (command==4) {
            if (speedX >0) return;
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

        // Here we should have valid view dimensions (availableWidth, availableHeight).
        // So, we can calculate obstacles in space and time here.
        // This calculation should be applied only at the game beginning.
        if (firstDraw){
            reset_upLife_Moments();
            firstDraw = false;
        }

        // prepare canvas and paint:
        myPaint.setColor(Color.WHITE);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        // bouncing box:
        myPaint.setStrokeWidth(5f);
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);


        // obstacles:
        int obstacleToDeleteDueTimeExpire = -1;
        for(int i = 0; i < myObstacleExpireTimes.size(); i++){
            long beginTime = (long)myObstacleExpireTimes.get(i);
            long endTime = beginTime + LIFE_UP_EXPIRATION_TIME;
            if ((this.currentTime >= beginTime) &&
                (this.currentTime <= endTime)) {
                // it's time to show specific obstacle:
                PointF obstaclePoint = (PointF) myObstacles.get(i);
                canvas.drawBitmap(addLifeBitmap,
                        obstaclePoint.x - ballRadius * 2,
                        obstaclePoint.y - ballRadius * 2,
                        myPaint);
            } else if (this.currentTime >= endTime) {
                // available time has passed for this obstacle,
                // so we will forget about it now
                obstacleToDeleteDueTimeExpire = i;
                break;
            }
        }
        if (obstacleToDeleteDueTimeExpire != -1){
            myObstacles.remove(obstacleToDeleteDueTimeExpire);
            myObstacleExpireTimes.remove(obstacleToDeleteDueTimeExpire);
        }

        // ball:
        myPaint.setColor(this.currentBallColor);
        myPaint.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(positionX, positionY, ballRadius, myPaint);
        canvas.drawBitmap(ballBitmap, positionX - ballRadius, positionY - ballRadius, myPaint);
    }


}
