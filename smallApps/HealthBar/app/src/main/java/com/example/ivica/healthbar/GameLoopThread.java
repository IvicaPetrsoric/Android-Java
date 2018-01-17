package com.example.ivica.healthbar;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

/**
 * Created by Ivica on 16.4.2016..
 */
public class GameLoopThread extends Thread{

    public static int FPS = 20;
    private GameView view;
    public boolean running = false;
    public boolean pause = false;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        if (FPS == 0)
            FPS = 1;
        long ticksPS = 1000 / FPS;
        long startTime = 0;
        long sleepTime = 0;

        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }

            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}
