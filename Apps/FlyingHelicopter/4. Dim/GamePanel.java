package com.example.ivica.letecakrafna;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "com.example.ivica.letecakrafna";

    public static int WIDTH;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -1;
    private long smokeStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;

    Bitmap backGround;


    public GamePanel(Context context)
    {
        // poziva super klasu od surfaceView
        super(context);

        // dodamo callBacks, da mozemo presretat klikove, mis itd.. evente!
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // napraviti da gamePanel moze presretat evente
        setFocusable(true);

        backGround = BitmapFactory.decodeResource(getResources(),R.drawable.grassbg1);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int heigth) {

    }

    //okvirBara = Bitmap.createScaledBitmap(okvirBara, d * 3  , d, false);

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // postavljanje pozadine
        bg = new Background(Bitmap.createScaledBitmap(backGround,getWidth(),getHeight(),false));
        WIDTH = getWidth();

        // stvaranje igraca
        player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.helicopter),65,40,3);

        smoke = new ArrayList<Smokepuff>();

        smokeStartTime = System.nanoTime();

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;

        //ponekad treba vise pokusaja da se zaustavi thread
        while (retry && counter<1000){

            counter++;
            // dok ovo ne uspije ici ce u krug
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }
    }


    //  za pracenje dodira
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!player.getPlaying()) {
                player.setPlaying(true);
            }
            player.setUp(true);
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }

        return  super.onTouchEvent(event);
    }



    public void update(){
        if(player.getPlaying()){
            bg.update();
            player.update();



            //dim
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;  //ms
            if (elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));
                smokeStartTime = System.nanoTime();
            }

            // pražnjenje resursa
            for (int i = 0; i<smoke.size(); i++){
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10){
                    smoke.remove(i);
                }
            }



        }

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

       // final float scaleFactorX = getWidth()/(float)WIDTH;
       // final float scaleFactorY = getHeight()/(float)HEIGHT;

        if (canvas != null){
           // final int savedState = canvas.save();
            //canvas.scale(scaleFactorX,scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            //  moramo svaki put vracati na normalno ili bi uvijek islo rescalle
           // canvas.restoreToCount(savedState);

            for (Smokepuff sp: smoke){
                sp.draw(canvas);
            }
        }

    }



}
