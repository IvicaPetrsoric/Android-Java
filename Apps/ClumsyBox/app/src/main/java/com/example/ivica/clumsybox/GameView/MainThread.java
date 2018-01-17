package com.example.ivica.clumsybox.GameView;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
klasa koja se brine za multithreading
 postavlja max FPS da ne ide iznad 30
 */
public class MainThread extends Thread{

    private int MAX_FPS = 60;
    public double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    private static Canvas canvas;

    public  MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder= surfaceHolder;
        this.gamePanel = gamePanel;
    }

    // pokreni multhitreading
    public void setRunning(boolean running){
        this.running = running;
    }

    // podešavanje na MAX FPS
    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // u slučaju erroa
            finally{
                if (canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            // podešavanje FPS-a
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {

                if (waitTime > 0){
                    this.sleep(waitTime);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;

                // ispiši srednju vrijednost FPS
                System.out.println("Average FPS: " +  (averageFPS));
            }
        }
    }



/*

    public void timeDelay(){

        //THREAD U POZADINI, tu ne stavljamo interface!!!   Preko handlera mjenjamo interface
        Runnable r = new Runnable() {
            @Override
            public void run() {

                long futureTime = System.currentTimeMillis() + 30;
                while( System.currentTimeMillis() < futureTime ){
                    synchronized (this) {
                        try{
                            wait(futureTime-System.currentTimeMillis());

                            resizePlayer();

                        }catch (Exception ignored){}
                    }
                }
            }
        };
        Thread mojThread = new Thread(r);
        mojThread.start();
    }

 */

}
