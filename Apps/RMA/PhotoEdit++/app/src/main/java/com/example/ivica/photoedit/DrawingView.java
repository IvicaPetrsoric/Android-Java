package com.example.ivica.photoedit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ivica on 12.4.2016..
 */
class DrawingView extends View{

    static Paint mPaint;
    Bitmap mBitmap;
    Canvas mCanvas;
    static ArrayList<Canvas>mCanvasSlika;
    Path mPath;
    Paint mBitmapPaint;

    public DrawingView(Context context) {
        super(context);
        mBitmapPaint = new Paint();

        // paint for overlay canvas:
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        mPath = new Path();

        /*Bundle extras = getIntent().getExtras();
        String value = extras.getString("Boja");*/

       // Boja =  intent.getIntExtra
        Intent intent = ((Activity) context).getIntent();
    }

    static void PromjenaBoje(int boja) {
        switch(boja) {
            case 0:
                mPaint.setColor(0xFFFF0000);
                break;

            case 1:
                mPaint.setColor(0xFF00FF00);
                break;

            case 2:
                mPaint.setColor(0xFF0000FF);
                break;
            case 3:
                mPaint.setColor(0xFFFFFFFF);

        }

       // Toast.makeText(DrawingView.this,"Birana je crvena boja", Toast.LENGTH_LONG).show();
    }
    static void PromjenaDebljine(int velicina) {
        mPaint.setStrokeWidth(velicina);

    //    System.out.println("Količina elemenata: "+mCanvasSlika.size());
       /* for(int i = 0; i<mCanvasSlika.size();i++)
        {
            System.out.println(i);
        }*/
    }

   /* static void UndoCrtanja(int pomak)
    {
        for(int i = 0; i<mCanvasSlika.size();i++)
        {
            System.out.println(i);
        }
    }
        */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        //  background photo:
        super.draw(canvas);

        //on- top drawing canvas:
        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
        canvas.drawPath(mPath,mPaint);
    }

    private float mX,mY;
    private static final float TOUCH_TOLERANCE = 4;

    private  void touch_start(float x, float y) {
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mX);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX,mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
       // int BROJ;
       // System.out.println(mCanvasSlika);
        mPath.lineTo(mX,mY);
        //  commit the path to our offscreen

        mCanvas.drawPath(mPath,mPaint);
        //mCanvasSlika.add(BROJ);
       // mCanvasSlika.add
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

}
