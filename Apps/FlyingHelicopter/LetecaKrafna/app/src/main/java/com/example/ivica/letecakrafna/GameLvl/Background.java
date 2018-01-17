package com.example.ivica.letecakrafna.GameLvl;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    public static final String TAG = "com.example.ivica.letecakrafna";
    private Bitmap image;
    private int x,y, dx;

    public Background(Bitmap res){

        this.image = res;
        dx = GamePanel.MOVESPEED;

    }

    public void update(){
        x += dx;

        // ako izade is screena
        if(x < -GamePanel.WIDTH){
            x = 0;
        }
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image,x,y,null);
        // ako ode slika treba zamijeniti, ide jedna za drugom
        if(x<0){
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y,null);
        }
    }

    public void recycle(){
        image.recycle();
    }

    /*
    public void setVector(int dx){
        this.dx = dx;
    }*/

}
