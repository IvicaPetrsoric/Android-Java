package com.example.ivica.letecakrafna;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.nfc.Tag;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "com.example.ivica.letecakrafna";

    public static int WIDTH;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background bg;

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

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        //ponekad treba vise pokusaja da se zaustavi thread
        while (retry){

            // dok ovo ne uspije ici ce u krug
            try {
                thread.setRunning(false);
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }


    //  za pracenje dodira
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return  super.onTouchEvent(event);
    }

    public void update(){
        bg.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

       // final float scaleFactorX = getWidth()/(float)WIDTH;
       // final float scaleFactorY = getHeight()/(float)HEIGHT;

       // Log.i(TAG,"ScaleX: " + scaleFactorX);
       // Log.i(TAG,"ScaleY: " + scaleFactorY);



        if (canvas != null){
           // final int savedState = canvas.save();
            //canvas.scale(scaleFactorX,scaleFactorY);
            bg.draw(canvas);
            //  moramo svaki put vracati na normalno ili bi uvijek islo rescalle
           // canvas.restoreToCount(savedState);
        }

    }



}