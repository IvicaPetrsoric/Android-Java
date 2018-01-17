package com.example.ivica.letecakrafna.GameLvl;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class myButtons {

    private Bitmap image;
    private int x;
    private int y;
    private int width;
    private int height;

    public myButtons(Bitmap res,int x,int y, int w, int h){
        this.image = res;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        image = Bitmap.createScaledBitmap(this.image,x,y,false);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image,width -x ,height ,null);
    }

}
