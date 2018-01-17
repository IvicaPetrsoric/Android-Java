package com.example.ivica.letecakrafna;


import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;


    public GamePanel(Context context)
    {
        // poziva super klasu od surfaceView
        super(context);

        // dodamo callBacks, da mozemo presretat klikove, mis itd.. evente!
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // napraviti da gamePanel moze presretat evente
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int heigth) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //start gameLoop
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

    }



}
