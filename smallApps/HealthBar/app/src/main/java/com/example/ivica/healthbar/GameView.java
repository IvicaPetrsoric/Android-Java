package com.example.ivica.healthbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by Ivica on 16.4.2016..
 */
public class GameView extends SurfaceView{

    public Random generator = new Random(System.currentTimeMillis());
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                StopMainThread();
                MainActivity.homeButton = true;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                MakeAndStartMainThread();
                MainActivity.homeButton = false;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    public void MakeAndStartMainThread() {
        gameLoopThread = new GameLoopThread(this);
        gameLoopThread.start();
        gameLoopThread.setRunning(true);
    }
    public void StopMainThread (){
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
    public void RestartMainThread(int FPS){
        GameLoopThread.FPS = FPS;
        StopMainThread();
        MakeAndStartMainThread();
    }

    @SuppressLint({ "WrongCall", "DrawAllocation" })
    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 0, 255)); // White Smoke
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0, 0, (int) Var.DEVICEscreenWidth, (int) Var.DEVICEscreenHeight), paint);

        drawTitle(canvas, paint, "Health Bar");
        drawHealthBar(canvas, paint, Var.healthPercent);
        drawAttackButton(canvas, paint, "Attack!!");
        drawHealButton(canvas, paint, "Heal!");
    }

    public Point posTitle(){
        int X = (int)(Var.DEVICEscreenWidth * .5); // Center
        int Y = (int)(Var.DEVICEscreenHeight * .1); // 10 Percent from Top
        return new Point(X,Y);
    }
    public int sizeTitle(){
        int S = (int)(Var.DEVICEscreenWidth * .15);
        return S;
    }
    public void drawTitle(Canvas canvas, Paint paint, String string){
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(sizeTitle());
        canvas.drawText(string, posTitle().x, posTitle().y, paint);
    }

    public Point posTextHealthBar(){
        int difA_W = rectHealthBar().right - rectHealthBar().left;

        int X = (int)(rectHealthBar().left + (difA_W/2)); // Center
        int Y = (int)(rectHealthBar().bottom);
        return new Point(X,Y);
    }
    public Rect rectHealthBar(){
        int X = (int)(Var.DEVICEscreenWidth * .2);
        int Y = (int)(Var.DEVICEscreenHeight * .15);
        int W = (int)(Var.DEVICEscreenWidth - X);
        int H = (int)(Y + (Var.DEVICEscreenHeight * .1));

        return new Rect(X,Y,W,H);
    }
    public void drawHealthBar(Canvas canvas, Paint paint, double healthPercent){
        Rect rect = rectHealthBar();
        int health = (int)((rect.right - rect.left) * healthPercent);
        int difA_H = rectHealthBar().bottom - rectHealthBar().top;

        // Fills (FILL)
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.argb(0, 0, 0, 0)); // Red
        canvas.drawRect(rect, paint);

        if (Var.healBarSelect)
            paint.setColor(Color.argb(0,255, 255, 0)); // Green
        else if (Var.attackBarSelect)
            paint.setColor(Color.argb(255,204, 0, 255)); // Neon Purple
        else  paint.setColor(Color.argb(200, 0, 255, 0)); // Green
        canvas.drawRect(new Rect(rect.left, rect.top, rect.left + health, rect.bottom), paint);


        // Boarders (STROKE)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        if (Var.healBarSelect | Var.attackBarSelect)
            paint.setColor(Color.argb(255, 0, 0, 0)); // Black
        else paint.setColor(Color.argb(200, 0, 0, 0)); // Black
        canvas.drawRect(new Rect(rect.left, rect.top, rect.left + health, rect.bottom), paint);
        canvas.drawRect(rect, paint);

        // Text
        if (Var.healthAorHSelect) {
            paint.setStyle(Paint.Style.FILL);

            if (Var.healthAorH != 0) {
                if (Var.healBarSelect | Var.attackBarSelect)
                    paint.setColor(Color.rgb(0, 0, 0)); // Black
                else paint.setColor(Color.rgb(255, 255, 255)); // White
            } else
                paint.setColor(Color.rgb(0, 0, 255)); // Cyan

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((int) (difA_H * .7)); //177

            String string = String.valueOf(Var.healthAorH);

            Rect bounds = new Rect();
            paint.getTextBounds(string, 0, string.length(), bounds);
            int H = bounds.bottom - bounds.top; //135
            H = (Math.abs(difA_H - H)) / 2;

            canvas.drawText(string, posTextHealthBar().x, posTextHealthBar().y - H, paint);
        }
    }

    public Point posTextAttackButton(){
        int difA_W = rectAttackButton().right - rectAttackButton().left;

        int X = (int)(rectAttackButton().left + (difA_W/2)); // Center
        int Y = (int)(rectAttackButton().bottom); // 10 Percent from Top
        return new Point(X,Y);
    }
    public Rect rectAttackButton(){
        int difH_H = rectHealthBar().bottom - rectHealthBar().top;

        int X = (int)(Var.DEVICEscreenWidth * .3);
        int Y = (int)(rectHealthBar().bottom + (difH_H * .5));
        int W = (int)(Var.DEVICEscreenWidth - X);
        int H = (int)(Y + difH_H);

        return new Rect(X,Y,W,H);
    }
    public void drawAttackButton(Canvas canvas, Paint paint, String string){
        int difA_H = rectAttackButton().bottom - rectAttackButton().top;

        // Fills (FILL)
        paint.setStyle(Paint.Style.FILL);

        if(!Var.attackBarSelect)
            paint.setColor(Color.argb(200, 50, 0, 50)); // Dark Purple
        else paint.setColor(Color.rgb(150, 0, 150)); // Purple
        canvas.drawRect(rectAttackButton(), paint);

        // Boarders (STROKE)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        paint.setColor(Color.rgb(0, 0, 0)); // Black
        canvas.drawRect(rectAttackButton(), paint);

        // Text
        paint.setStyle(Paint.Style.FILL);
        if(!Var.attackBarSelect)
            paint.setColor(Color.argb(200,255, 255, 255)); // White
        else paint.setColor(Color.rgb(0, 0, 0)); // Black

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize((int) (difA_H * .5)); //177

        Rect bounds = new Rect();
        paint.getTextBounds(string, 0, string.length(), bounds);
        int H = bounds.bottom - bounds.top; //135
        H = (Math.abs(difA_H - H))/2;

        canvas.drawText(string, posTextAttackButton().x, posTextAttackButton().y - H, paint);
    }

    public Point posTextHealButton(){
        int difA_W = rectHealButton().right - rectHealButton().left;

        int X = (int)(rectHealButton().left + (difA_W/2)); // Center
        int Y = (int)(rectHealButton().bottom); // 10 Percent from Top
        return new Point(X,Y);
    }
    public Rect rectHealButton(){
        int difH_H = rectHealthBar().bottom - rectHealthBar().top;

        int X = (int)(Var.DEVICEscreenWidth * .3);
        int Y = (int)(rectAttackButton().bottom + (difH_H * .5));
        int W = (int)(Var.DEVICEscreenWidth - X);
        int H = (int)(Y + difH_H);

        return new Rect(X,Y,W,H);
    }
    public void drawHealButton(Canvas canvas, Paint paint, String string){
        int difA_H = rectHealButton().bottom - rectHealButton().top;

        // Fills (FILL)
        paint.setStyle(Paint.Style.FILL);

        if(!Var.healBarSelect)
            paint.setColor(Color.argb(200, 0, 50, 0)); // Darkish Green
        else paint.setColor(Color.rgb(0, 150, 0)); // Green
        canvas.drawRect(rectHealButton(), paint);

        // Boarders (STROKE)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        paint.setColor(Color.rgb(0, 0, 0)); // Black
        canvas.drawRect(rectHealButton(), paint);

        // Text
        paint.setStyle(Paint.Style.FILL);
        if(!Var.healBarSelect)
            paint.setColor(Color.argb(200, 255, 255, 255)); // White
        else paint.setColor(Color.rgb(0, 0, 0)); // Black
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize((int)(difA_H * .5)); //177

        Rect bounds = new Rect();
        paint.getTextBounds(string, 0, string.length(), bounds);
        int H = bounds.bottom - bounds.top; //135
        H = (Math.abs(difA_H - H))/2;

        canvas.drawText(string, posTextHealButton().x, posTextHealButton().y - H ,paint);
    }

    double randomGenerator(int X, int Y) {
        generator = new Random(System.currentTimeMillis());
        double A = (double)(generator.nextInt(Y) + X);
        Var.healthAorH = (int)A;
        A = (A * .01);
        return A;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                RestartMainThread(GameLoopThread.FPS);
                Var.mouseX = event.getX();
                Var.mouseY = event.getY();
                if (((Var.mouseX >= rectAttackButton().left) && (Var.mouseX <= rectAttackButton().right)) && ((Var.mouseY >= rectAttackButton().top) && (Var.mouseY <= rectAttackButton().bottom))) {
                    Var.attackBarSelect = true;
                    Var.healthAorHSelect = true;
                    Var.healthPercent -= randomGenerator(0, 10);
                    if(Var.healthPercent < 0)
                        Var.healthPercent = 0;
                }
                else if (((Var.mouseX >= rectHealButton().left) && (Var.mouseX <= rectHealButton().right)) && ((Var.mouseY >= rectHealButton().top) && (Var.mouseY <= rectHealButton().bottom))){
                    Var.healBarSelect = true;
                    Var.healthAorHSelect = true;
                    Var.healthPercent += randomGenerator(0, 10);
                    if(Var.healthPercent > 1)
                        Var.healthPercent = 1;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                // RestartMainThread(GameLoopThread.FPS);
                // Var.mouseX = event.getX();
                // Var.mouseY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                RestartMainThread(GameLoopThread.FPS);
                Var.mouseX = event.getX();
                Var.mouseY = event.getY();
                Var.attackBarSelect = false;
                Var.healBarSelect = false;
                Var.healthAorHSelect = false;

                break;
            default:
                return false;
        }
        return true;
    }
}

