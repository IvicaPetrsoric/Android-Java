package com.example.ivica.letecakrafna.GameLvl;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ivica.letecakrafna.Menu.UlazniScreen;
import com.example.ivica.letecakrafna.R;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "com.example.ivica.letecakrafna";

    public static String  MODEL;
    public static int WIDTH;
    public static final int HEIGHT = 480;
    public static int MOVESPEED = -1;
    public static int globalSpeed;
    private long smokeStartTime;
    private long missilesStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile>  missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    // povecaj za smanjenje tezine
    private int progressDenom = 20;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;

    private Explosion explosion;
    private boolean pokreniExp;
    private boolean jednaExp;
    private boolean crtajBombu;
    private long startReset;

    private int bestScore;

    Bitmap backGround;

    //dugmad
    myButtons myButton;
    Missile rakete;

    Game game;

    private Context mContext;

    LinearLayout gameView;
    RelativeLayout buttonView;

    public GamePanel(Context context,String model) {
        // poziva super klasu od surfaceView
        super(context);

        MODEL = model;

        //za intente
        this.mContext = context;

        // dodamo callBacks, da mozemo presretat klikove, mis itd.. evente!
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // napraviti da gamePanel moze presretat evente
        setFocusable(true);

        backGround = BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1);

       // gameView =  (LinearLayout)findViewById(R.id.gameView);
       // buttonView = (RelativeLayout)findViewById(R.id.buttonView);
       // gameView.setVisibility(V);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int heigth) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game = new Game();

        pokreniExp = false;
        jednaExp = false;
        //rakete = new Missile(null,null,null,null,null,null,null);

        // postavljanje pozadine
        bg = new Background(Bitmap.createScaledBitmap(backGround,getWidth(),getHeight(),false));
        WIDTH = getWidth();

        // stvaranje igraca
        player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.helicopter),65,40,3);

        myButton = new myButtons(BitmapFactory.decodeResource(getResources(),R.drawable.pause),100,100,getWidth(),0);

        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();

        smokeStartTime = System.nanoTime();
        missilesStartTime = System.nanoTime();

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
                thread = null;
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //  za pracenje dodira
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int)event.getX();
        int y = (int)event.getY();

        if (!crtajBombu){
            if(x > getWidth()-100 && y < 100){
               // Log.i(TAG,"VECI!");
                //rakete.speed = 0;
               // game.povratakMain();
                //mContext = getContext();
                // TU SE IDE U MAIN!!!!!!!
                Intent intent = new Intent(mContext,UlazniScreen.class);
                mContext.startActivity(intent);

                bg.recycle();
                player.recycle();

                //recycle();
            }
            else {
                //MOVESPEED = -1;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!player.getPlaying()) {
                        player.setPlaying(true);
                        //player.setPlaying(r);
                    }
                    player.setUp(true);
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    player.setUp(false);
                    return true;
                }
            }
        }

        return  super.onTouchEvent(event);
    }

    public void update(){
        if(player.getPlaying()){
            bg.update();
            player.update();

            //calculate the threshold of height the border can have based on the score
            //max and min border heart are updated, and the border switched direction when either max or min is met.

            maxBorderHeight = 30+player.getScore()/progressDenom;
            // cap max border to 1/2 of screen
            if(maxBorderHeight > HEIGHT/4) maxBorderHeight = HEIGHT/4;
            minBorderHeight = 5+player.getScore()/progressDenom;

            /*
            //check bottom border collision
            for(int i = 0; i<botborder.size();i++){
                if(collision(botborder.get(i),player)){
                    player.setPlaying(false);
                }
            }
            */

            //check top border collision
            for(int i = 0; i<topborder.size();i++){
                if(collision(topborder.get(i),player)){
                    player.setPlaying(false);
                    jednaExp = true;
                }
            }

            if(player.getY() > getHeight() -50) {
                player.setPlaying(false);
                jednaExp = true;
            }

            //Log.i(TAG,"Visina"+player.getY());
            //Log.i(TAG,"Visina ukupna" + getHeight());

            //update top border
            updateTopBorder();
            //update bottom border
            //updateBottomBorder();

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

            // look trough every missile i trazi koliziju
            for(int i = 0; i<missiles.size(); i++){
                //  update projektil
                missiles.get(i).update();
                if(collision(missiles.get(i),player)){
                    missiles.remove(i);
                    player.setPlaying(false);
                    jednaExp = true;
                    break;
                }

                // makni projektil ako je van prozora
                if(missiles.get(i).getX()<-100){
                    missiles.remove(i);
                    break;
                }
            }

            // projektili
            long missilesElapsed = (System.nanoTime() - missilesStartTime)/1000000;
            // što je veći rezultat to je manji delaj
            if(missilesElapsed > (2000-player.getScore()/4)){

                // prvi projektil u sredini
                if(missiles.size() == 0){
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, HEIGHT/2,45,15,player.getScore(), 13));
                }
                else {
                    // missles unutar zidova
                    // missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                    //         WIDTH + 10, (int)(rand.nextDouble()*((HEIGHT - (maxBorderHeight *2))) + maxBorderHeight),45,15,player.getScore(), 13));

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, player.getY(),45,15,player.getScore(), 13));
                }

                // reset timer
                missilesStartTime = System.nanoTime();
            }
        }
        else {

            if (jednaExp) {
                crtajBombu = true;
                pokreniExp = true;
                startReset = System.nanoTime();
                jednaExp = false;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), player.getX(),
                        player.getY() - 30, 200, 200, 25);
            }

            if (crtajBombu && MODEL.equals("SM-G900F")){
                // tu se stvaraju problemi
                explosion.update();
            }

            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            newGameCreated = false;

            if(resetElapsed > 2500 && !newGameCreated) {
                crtajBombu = false;
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b){
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
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
            if(!crtajBombu){
                player.draw(canvas);
            }

            //  moramo svaki put vracati na normalno ili bi uvijek islo rescalle
            // canvas.restoreToCount(savedState);

            // crtaj dim
            for (Smokepuff sp: smoke){
                sp.draw(canvas);
            }

            // crtaj projektile
            for(Missile m: missiles){
                m.draw(canvas);
            }

            //draw topborder
            for(TopBorder tb:topborder){
                tb.draw(canvas);
            }

            /*
            for(BotBorder bb:botborder){
                bb.draw(canvas);
            }*/

            // crtaj exploziju
            if(pokreniExp){
                explosion.draw(canvas);
            }

            //crtaj dugme
            myButton.draw(canvas);

            drawText(canvas);
        }
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + (player.getScore()/10 ) +"m", 10, getHeight() - 10, paint);
        canvas.drawText("BEST: " + bestScore +"m", 10, getHeight() - 50, paint);
    }

    public void updateTopBorder(){
        //every 50 points, inest randomly placed top blocks that break the pattern
        if(player.getScore()%50 == 0){
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                    R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20, 0, (int)((rand.nextDouble() * (maxBorderHeight)) +1)));
        }

        for(int i = 0; i<topborder.size(); i++){
            topborder.get(i).update();
            if(topborder.get(i).getX() < -20){
                topborder.remove(i);

                // remove element i zamijeni s novim
                if(topborder.get(topborder.size()-1).getHeigth() >= maxBorderHeight){
                    topDown = false;
                }
                // zadnji stvoren
                if(topborder.get(topborder.size()-1).getHeigth() <= minBorderHeight){
                    topDown = true;
                }

                // dodavanjem novog ce biti vecih dimenzija
                if(topDown){
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            topborder.get(topborder.size()-1).getX()+20, 0, topborder.get(topborder.size()-1).getHeigth()+1));
                }else {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            topborder.get(topborder.size()-1).getX()+20, 0, topborder.get(topborder.size()-1).getHeigth()-1));// na kraju se visina dodaje ili oduzima
                }
            }
        }
    }

    public void updateBottomBorder(){
        //every 40 points, inest randomly placed top blocks that break the pattern
        if(player.getScore()%40 == 0){
            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),
                    R.drawable.brick), botborder.get(botborder.size() - 1).getX() + 20, (int)((rand.nextDouble() * maxBorderHeight) + (HEIGHT-maxBorderHeight))));
        }

        //update bottom border
        for(int i = 0; i<botborder.size(); i++) {
            botborder.get(i).update();

            // ako je border izvan ekrana makni ga
            if (botborder.get(i).getX() < -20) {
                botborder.remove(i);


                // remove element i zamijeni s novim
                if (botborder.get(botborder.size() - 1).getY() <= HEIGHT - maxBorderHeight) {
                    botDown = true;
                }
                // zadnji stvoren
                if (botborder.get(botborder.size() - 1).getY() >= HEIGHT - minBorderHeight) {
                    botDown = false;
                }

                if (botDown) {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() + 1));
                } else {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() - 1));
                }
            }
        }
    }

    //svaki put kada igrtac umre
    public void newGame(){
        // Log.i(TAG,"TU");
        botborder.clear();
        topborder.clear();
        missiles.clear();
        smoke.clear();

        pokreniExp = false;

        minBorderHeight = 5;
        maxBorderHeight = 30;

        if(player.getScore()/10 > bestScore){
            bestScore = player.getScore()/10;
        }

        player.resetScore();
        player.resetDY();
        player.setY(HEIGHT/2);

        //create initial borders
        // initial top border
        for(int i = 0; i*20 < WIDTH+40;i++){
            // prvi border
            if(i == 0){
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20,0,10));
            }else{
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20,0,topborder.get(i-1).getHeigth()+1));
            }
        }

        // initial bottom border
        for(int i = 0; i*20<WIDTH+40;i++){
            // prvi
            if(i == 0){
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, HEIGHT - minBorderHeight));
            }else{
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, botborder.get(i-1).getY()-1));
            }
        }
        newGameCreated = true;
    }

}
