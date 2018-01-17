package com.example.ivica.letecakrafna;


import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Player extends GameObject{

    private Bitmap spritesheet;
    private int score;
    private double dya; // ubrzanje gore dole
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();  // PAZITI DA NE IMPORTA ANIMATIONS!!!!!! treba svoju klasu koristiti
    public long startTime;

    public Player(Bitmap res, int w, int h,int numFrames){

        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h ;
        width = w ;


        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, i * width  , 0, width , height );
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }

    public void setUp(boolean b){
        up = b;
    }

    public void update(){
        long elapsed = (System.nanoTime() - startTime)/ 1000000;
        if(elapsed>100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
            dy = (int)(dya -=0.5);
        }
        else{
            dy = (int)(dya +=0.5);
        }

        if(dy > 3) dy = 3;
        if(dy < -3) dy = -3;

        y += dy*2;
        dy = 0;

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public int getScore(){
        return  score;
    }
    public boolean getPlaying(){
        return playing;
    }
    public void setPlaying(boolean b){
        playing = b;
    }
    public void resetDYA(){
        dya = 0;
    }
    public void sesetScore(){
        score = 0;
    }










}