package com.example.ivica.surfaceview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

//public class MainActivity extends Activity implements View.OnTouchListener {
public class MainActivity extends Activity {

   // OurView v;
   // OurView b;
    Bitmap ball;
    float x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // v = new OurView(this);  // novi objekt
        x = y = 0;
       // setContentView(v);

        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

    }



    @Override
    protected void onPause() {
        super.onPause();
        v.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        v.resume();
    }

   /* @Override
    public boolean onTouch(View v, MotionEvent event) {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getX();   // vraca float
                y = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x = event.getX();   // vraca float
                y = event.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                x = event.getX();   // vraca float
                y = event.getY();

                break;
        }

        return true;    // stalno ce pratiti reakcije, ne smije biti false jer ce izaci iz ovoga
    }*/

    public class OurView extends SurfaceView implements Runnable{

        Thread t = null;
        SurfaceHolder   holder;    //dopusta promjenu prikaza, pixela itd, manage ove clase
        boolean isItOk = false;

        public OurView(Context context){
            super(context);
            holder = getHolder();   // set content iz glavne clase, surface
            setWillNotDraw(false);

        }

        @Override
        public void run() {
            while (isItOk == true){ // invalidate method
                // perform canvas drawing
                if (!holder.getSurface().isValid()){ // provjera surface i ako nije dostupan
                    continue;
                }
                Canvas c = holder.lockCanvas();
                //  c.restore();
                //c.drawARGB(255,0,0,0);
                // c.getDrawFilter();
                c.drawColor(Color.BLACK);
                // invalidate();
                c.drawBitmap(ball,x - (ball.getWidth()/2) ,y - (ball.getHeight()/2),null);  // centriranje
                holder.unlockCanvasAndPost(c);
                // invalidate();

                x++;
                y++;
            }
        }

        public void pause(){
            isItOk = false;
            while(true){
                try {
                    t.join();   // prekida thread
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        public void resume(){
            isItOk = true;
            t = new Thread(this);   // ova metoda run, jer smo implementirali vec runnable , trazin run metodu!
            t.start();
        }
    }
}
