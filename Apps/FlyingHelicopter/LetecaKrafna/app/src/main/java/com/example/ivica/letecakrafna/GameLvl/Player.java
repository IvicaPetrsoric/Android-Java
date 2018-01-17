package com.example.ivica.letecakrafna.GameLvl;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;


public class Player extends GameObject{

    public static final String TAG = "com.example.ivica.letecakrafna";

    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();  // PAZITI DA NE IMPORTA ANIMATIONS!!!!!! treba svoju klasu koristiti
    public long startTime;

    public Player(Bitmap res, int w, int h,int numFrames){
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;

        //Log.i(TAG,"model_player: "+ GamePanel.MODEL);
        if(GamePanel.MODEL.equals("SM-G900F")){
            //Log.i(TAG,"tu");
            width = w *3;
            height = h *2;
        }else{
            width = w;
            height = h;
        }

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
            dy -=1;
        }
        else{
            dy +=1;
        }

        if(dy>4)dy = 4;
        if(dy<-4)dy = -4;

        y += dy*2;
    }

    public void recycle(){
        animation.getImage().recycle();
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

    public void resetDY(){
        dy = 0;
    }

    public void resetScore(){
        score = 0;
    }


}
