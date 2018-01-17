package com.example.ivica.ClumsyBox.SplashScreen;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ivica.ClumsyBox.MainMenu.Main_menu;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.ScoresOnlineView;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.UUID;

public class SplashScreen extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    Handler taskHandler;
    int FRAME_RATE = 30;
    int screenHeight,screenWidth;
    int randPozX1, randPozY1,randPozX2, randPozY2,randPozX3, randPozY3, brojacPozadine;
    boolean prekiniTask, omoguciIntro;
    Splash splashController;
    MediaPlayer player;
    LinearLayout splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        prekiniTask = false;
        brojacPozadine = 0;
        loadDataSharedPref();

        DisplayMetrics myDisplaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myDisplaymetrics);

        screenHeight = myDisplaymetrics.heightPixels;
        screenWidth = myDisplaymetrics.widthPixels;

        Random rand = new Random();
        randPozY1 = rand.nextInt(screenHeight/2) + screenHeight/4;
        randPozX1 = rand.nextInt(screenWidth/2) + screenWidth/4;
        randPozY2 = rand.nextInt(screenHeight/2) + screenHeight/4;
        randPozX2 = rand.nextInt(screenWidth/2) + screenWidth/4;
        randPozY3 = rand.nextInt(screenHeight/2) + screenHeight/4;
        randPozX3 = rand.nextInt(screenWidth/2) + screenWidth/4;

        splashScreen = (LinearLayout)findViewById(R.id.splasScreen);

        splashController = new Splash(this);
        taskHandler = new Handler();
        splashScreen.addView(splashController);
        taskHandler.postDelayed(myTask, FRAME_RATE);

        // intro muzika
        player = MediaPlayer.create(this, R.raw.intro);
        player.setLooping(false); // Set looping
        player.setVolume(1,1);

        brojacIspisaImena();
        brojacMaina();
    }

    public void loadDataSharedPref() {
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        omoguciIntro = sharedPref.getBoolean("introMuzika", false);
    }

    public void playIntro(){
        if (!omoguciIntro){
            player.start();
        }
    }

    public void brojacIspisaImena(){
        Runnable r = new Runnable() {
            @Override
            public void run() {

                long futureTime_0 = System.currentTimeMillis()+5400;
                long futureTime_1 = System.currentTimeMillis()+4100;
                long futureTime_2 = System.currentTimeMillis()+3400;
                long futureTime_3 = System.currentTimeMillis()+2700;
                long futureTime_start = System.currentTimeMillis()+2000;

                playIntro();

                while(System.currentTimeMillis() < futureTime_0){
                    synchronized (this) {
                        if (System.currentTimeMillis() < futureTime_start){

                        }

                        else if(System.currentTimeMillis() < futureTime_3){
                            if (!splashController.prvaExp){
                                asyncPromjenaPozadine();
                            }
                            splashController.omoguciCrtanje = true;
                            splashController.rma_tekst = "R";
                            splashController.prvaExp = true;
                        }
                        else if(System.currentTimeMillis()  < futureTime_2){
                            if (!splashController.drugaExp){
                                brojacPozadine++;
                                asyncPromjenaPozadine();
                            }
                            splashController.rma_tekst = "RM";
                            splashController.drugaExp = true;
                        }

                        else if (System.currentTimeMillis() < futureTime_1){
                            if (!splashController.trecaExp){
                                brojacPozadine++;
                                asyncPromjenaPozadine();
                            }
                            splashController.rma_tekst = "RMA";
                            splashController.trecaExp = true;
                        }
                        else if (System.currentTimeMillis() < futureTime_0){
                            splashController.svaSlova = true;
                        }
                    }
                }
            }
        };
        Thread mojThread = new Thread(r);
        mojThread.start();
    }

    public void asyncPromjenaPozadine(){
        novaPozadina task = new novaPozadina();
        task.execute();
    }

    public class novaPozadina extends AsyncTask<Void, Void,Void> {
        @Override
        protected Void doInBackground(Void... params ){
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

                switch (brojacPozadine){

                    case 0:
                        splashScreen.setBackgroundResource(R.drawable.munje_5a);
                        break;

                    case 1:
                        splashScreen.setBackgroundResource(R.drawable.munje_5b);
                        break;

                    case 2:
                        splashScreen.setBackgroundResource(R.drawable.munje_5);
                        break;
                }
        }
    }

    private Runnable myTask = new Runnable(){
        @Override
        public void run() {
            taskHandler.postDelayed(myTask, FRAME_RATE);    // brzina poziva
            splashController.invalidate();
            splashController.pomakeEfektaEksplozije();

            if (prekiniTask){
                taskHandler.removeCallbacks(myTask);
            }
        }

    };

    public void brojacMaina(){
        Runnable r = new Runnable() {   //THREAD U POZADINI, tu ne stavljamo interface!!!   Preko handlera mjenjamo interface
            @Override
            public void run() {

                long futureTime = System.currentTimeMillis()+10000;
                while(System.currentTimeMillis() <futureTime){
                    synchronized (this) { // kdo vise thredova da ne skacu jedan dgugome
                        try{
                            wait(futureTime-System.currentTimeMillis());
                        }catch (Exception ignored){}
                    }
                }
                pokreniMenu();

            }
        };
        Thread mojThread = new Thread(r);
        mojThread.start();
    }

    public void pokreniMenu(){
        prekiniTask = true;
        Intent i = new Intent(this,Main_menu.class);
        startActivity(i);
        splashController.omoguciCrtanje = false;
        splashController.recycleRam();
        player.release();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    public class Splash extends View {
        Bitmap eksplozija1, eksplozija2, eksplozija3;
        float pozEkspY1, pozEkspY2, pozEkspY3, pozEkspX1, pozEkspX2, pozEkspX3;
        float pozEkspY4, pozEkspY5, pozEkspY6, pozEkspX4, pozEkspX5, pozEkspX6;
        float pozEkspY7, pozEkspY8, pozEkspY9, pozEkspX7, pozEkspX8, pozEkspX9;
        float speedEksp;
        int fadeSlova;
        boolean omoguciCrtanje,svaSlova;
        boolean prvaExp, drugaExp, trecaExp;
        String rma_tekst, rma_studio;
        Paint paint = new Paint();

        public Splash(Context context) {
            super(context);
            //  y
            pozEkspY1 = pozEkspY2 = pozEkspY3 = randPozY1 ;
            //  x
            pozEkspX1 = pozEkspX2 = pozEkspX3 = randPozX1 ;

            //  y
            pozEkspY4 = pozEkspY5 = pozEkspY6 = randPozY2 ;
            //  x
            pozEkspX4 = pozEkspX5 = pozEkspX6 = randPozX2 ;

            //  y
            pozEkspY7 = pozEkspY8 = pozEkspY9 = randPozY3 ;
            //  x
            pozEkspX7 = pozEkspX8 = pozEkspX9 = randPozX3 ;

            omoguciCrtanje = false;
            svaSlova = false;
            prvaExp = drugaExp = trecaExp = false;

            speedEksp = screenWidth/500;
            if (speedEksp < 0.5){
                speedEksp = 1;
            }
            fadeSlova = 0;
            rma_tekst = "";
            rma_studio = "STUDIO";

            eksplozija1 = BitmapFactory.decodeResource(getResources(), R.drawable.eksplozija_c);
            eksplozija2 = BitmapFactory.decodeResource(getResources(), R.drawable.eksplozija_p);
            eksplozija3 = BitmapFactory.decodeResource(getResources(), R.drawable.eksplozija_z);

        }

        public void recycleRam(){
            eksplozija1.recycle();
            eksplozija2.recycle();
            eksplozija3.recycle();
        }

        public void pomakeEfektaEksplozije() {

            if (prvaExp) {

                pozEkspY1 = pozEkspY1 + speedEksp;
                pozEkspY2 = pozEkspY2 - speedEksp;
                pozEkspX3 = pozEkspX3 + speedEksp;
                pozEkspX2 = pozEkspX2 - speedEksp;
            }
            if (drugaExp) {

                pozEkspY4 = pozEkspY4 + speedEksp;
                pozEkspY5 = pozEkspY5 - speedEksp;
                pozEkspX6 = pozEkspX6 + speedEksp;
                pozEkspX5 = pozEkspX5 - speedEksp;
            }
            if (trecaExp) {

                pozEkspY7 = pozEkspY7 + speedEksp;
                pozEkspY8 = pozEkspY8 - speedEksp;
                pozEkspX9 = pozEkspX9 + speedEksp;
                pozEkspX8 = pozEkspX8 - speedEksp;
            }
        }

        public void fadeSlova(){
            fadeSlova = fadeSlova + 2;

            if (fadeSlova > 255) {
                fadeSlova = 255;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (omoguciCrtanje) {

                paint.setColor(Color.WHITE);
                paint.setTextSize(screenWidth / 10);
                paint.setFakeBoldText(true);

                canvas.drawText(rma_tekst, screenWidth * 0.4f, screenHeight * 0.45f, paint);

                if (svaSlova){

                    paint.setARGB(fadeSlova, 255, 255, 255);
                    fadeSlova();

                    canvas.drawText(rma_studio, screenWidth * 0.35f, screenHeight * 0.52f, paint);
                }

                if (prvaExp) {
                    // eksplozija
                    //  +y
                    canvas.drawBitmap(eksplozija1, pozEkspX1, pozEkspY1, null);
                    //   -y
                    canvas.drawBitmap(eksplozija1, pozEkspX1, pozEkspY2, null);
                    //   +x
                    canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY3, null);
                    //   -x
                    canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY3, null);
                    //   X|Y
                    canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY2, null);
                    //   -X|Y
                    canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY2, null);
                    //   -X|-Y
                    canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY1, null);
                    //   X|-Y
                    canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY1, null);
                }

                if (drugaExp) {
                    // eksplozija
                    //  +y
                    canvas.drawBitmap(eksplozija2, pozEkspX4, pozEkspY4, null);
                    //   -y
                    canvas.drawBitmap(eksplozija2, pozEkspX4, pozEkspY5, null);
                    //   +x
                    canvas.drawBitmap(eksplozija2, pozEkspX5, pozEkspY6, null);
                    //   -x
                    canvas.drawBitmap(eksplozija2, pozEkspX6, pozEkspY6, null);
                    //   X|Y
                    canvas.drawBitmap(eksplozija2, pozEkspX5, pozEkspY5, null);
                    //   -X|Y
                    canvas.drawBitmap(eksplozija2, pozEkspX6, pozEkspY5, null);
                    //   -X|-Y
                    canvas.drawBitmap(eksplozija2, pozEkspX5, pozEkspY4, null);
                    //   X|-Y
                    canvas.drawBitmap(eksplozija2, pozEkspX6, pozEkspY4, null);
                }

                if (trecaExp) {
                    // eksplozija
                    //  +y
                    canvas.drawBitmap(eksplozija3, pozEkspX7, pozEkspY7, null);
                    //   -y
                    canvas.drawBitmap(eksplozija3, pozEkspX7, pozEkspY8, null);
                    //   +x
                    canvas.drawBitmap(eksplozija3, pozEkspX8, pozEkspY9, null);
                    //   -x
                    canvas.drawBitmap(eksplozija3, pozEkspX9, pozEkspY9, null);
                    //   X|Y
                    canvas.drawBitmap(eksplozija3, pozEkspX8, pozEkspY8, null);
                    //   -X|Y
                    canvas.drawBitmap(eksplozija3, pozEkspX9, pozEkspY8, null);
                    //   -X|-Y
                    canvas.drawBitmap(eksplozija3, pozEkspX8, pozEkspY7, null);
                    //   X|-Y
                    canvas.drawBitmap(eksplozija3, pozEkspX9, pozEkspY7, null);
                }
            }
        }
    }
}
