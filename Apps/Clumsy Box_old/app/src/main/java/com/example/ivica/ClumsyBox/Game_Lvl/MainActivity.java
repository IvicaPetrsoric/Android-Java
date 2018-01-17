package com.example.ivica.ClumsyBox.Game_Lvl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;
import com.example.ivica.ClumsyBox.End_Lvl.EndLvl;
import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Scores.DB.MyDBHandler;
import com.example.ivica.ClumsyBox.R;

import java.util.Random;

public class MainActivity extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";
    int FRAME_RATE = 24;
    int radius, screenHeight, screenWidth ,gameViewHeight, duzinaReklame;
    int musicDestroyed, musicBounce, jacinaSFX;
    int brzinaIgraca;
    int brojPovecanjaBrzine;
    int trenutniHighScoreDB;
    float trenutnaBrzinaKocke;
    boolean noviActivity,noviScore, prviTouch, omoguciOlbake;
    GameView myGameView;

    Handler taskHandler;

    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    Button reklama;
    TranslateAnimation animation;
    LinearLayout mainGameView;
    String odabraniSvijet;
    SoundPool soundPool;
    MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noviActivity = false;
        noviScore = false;
        prviTouch = true;
        omoguciOlbake = true;

        ProvjeraSvijeta();
        randomPozadina();

        myDB = new MyDBHandler(this,null);
        trenutniHighScoreDB = myDB.highestDbScore();

        //  GameView veličina
        DisplayMetrics myDisplaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myDisplaymetrics);
        mainGameView = (LinearLayout)findViewById(R.id.GameView);
        reklama = (Button) findViewById(R.id.dugmeReklama);

        if (globalVar.get_noAds()){
            reklama.setText("");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            mainGameView.setLayoutParams(param);
        }

        mainGameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zaustaviGameView();
                return false;
            }
        });

        screenHeight = myDisplaymetrics.heightPixels;
        screenWidth = myDisplaymetrics.widthPixels;

        globalVar.set_screenH(screenHeight);
        globalVar.set_screenW(screenWidth);

        this.radius = screenWidth / 10;

        myGameView = new GameView(this);
        taskHandler = new Handler();

        // pokretanje igre
        myGameView.setWorldPlayng(odabraniSvijet);
        myGameView.setStartPosition(radius *2 , radius *2);
        myGameView.setCubeRadius((float) radius);
        myGameView.randIgrac();
        brojPovecanjaBrzine = 0;
        mainGameView.addView(myGameView);
        myGameView.postaviDuzinuScoreBara(false);
        trenutnaBrzinaKocke = 0;

        //  muzika
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        musicDestroyed = soundPool.load(this, R.raw.destroy, 1);
        musicBounce = soundPool.load(this, R.raw.bounce, 1);
    }

    public void noviLocalScore(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long futureTime_3 = System.currentTimeMillis()+2000;
                while (System.currentTimeMillis() < futureTime_3){

                }
                myGameView.omoguciNewLocalScore = false;
            }
        };

        Thread mojThread = new Thread(r);
        mojThread.start();
    }

    public void randomPozadina(){
        LinearLayout mainGameView = (LinearLayout)findViewById(R.id.GameView);

        Random rand = new Random();
        int ranndPozadina = rand.nextInt(2);

        if (ranndPozadina == 1){
            mainGameView.setBackgroundResource(R.drawable.pozadina_2);
        }
        else{
            mainGameView.setBackgroundResource(R.drawable.pozadina_1);
        }

    }

    // za hvatanje dimenzija gamveView
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout mainGameView = (LinearLayout)findViewById(R.id.GameView);
        gameViewHeight =  mainGameView.getHeight();
        globalVar.set_gameViewH(gameViewHeight);
        myGameView.gameViewHeigth = globalVar.get_gameViewH();

        brzinaIgraca = gameViewHeight/200;
        myGameView.setSpeed(brzinaIgraca);

        reklama = (Button)findViewById(R.id.dugmeReklama);

        if (!noviActivity){
            if (!globalVar.get_noAds()){
                reklama.setText(globalVar.get_textReklama());
                duzinaReklame = reklama.getWidth();
                kretanjeReklame();
            }
        }
    }

    public void zaustaviGameView(){
        if (prviTouch){
            myGameView.brojacPrijeIgre = false;
            taskHandler.postDelayed(myTask, FRAME_RATE);
            myGameView.setStartPosition(radius  , radius *3);
            prviTouch = false;
            myGameView.pokretanjeIgre.recycle();
        }

        else if  (!myGameView.brojacPrijeIgre && !myGameView.omoguciLoading) {
            String stanje = myGameView.provjeraPozicijeIgraca();

            switch (stanje) {

                case "VAN ZIDA" :
                    novaBrzinaKocke();
                    playMusicDestroy();
                    break;

                case "PORAVNATI":
                    myGameView.postaviDuzinuScoreBara(true);
                    reset();
                    playMusicBounce();
                    break;

                case "UNUTAR":
                    playMusicBounce();
                    novaBrzinaKocke();
                    break;

                default:
                    break;
            }
        }
    }

    public void playMusicBounce(){
        if (globalVar.get_stanjeSFXmuzike()){
            soundPool.play(musicBounce, (float)jacinaSFX/100, (float)jacinaSFX/100, 1, 0, 1f);
        }
    }

    public void playMusicDestroy(){
        if (globalVar.get_stanjeSFXmuzike()){
            soundPool.play(musicDestroyed, (float)jacinaSFX/100, (float)jacinaSFX/100, 1, 0, 1f);
        }
    }

    public void pokreniLoading(){
        noviActivity = true;
        globalVar.set_ostvareniBodovi(myGameView.rezultat);
        globalVar.set_obavljenoSpremanjeULocalScore(false);
        globalVar.set_obavljenoSpremanjeNaWeb(false);
        myGameView.ocistiMemoriju();
        Intent pokreniEndLvl = new Intent(this,EndLvl.class);
        startActivity(pokreniEndLvl);
        finish();
    }

    public void novaBrzinaKocke(){
        if (brojPovecanjaBrzine < 6 && odabraniSvijet.equals("1") ){
            myGameView.cubeSpeedY  = Math.abs( myGameView.cubeSpeedY) + brzinaIgraca; //max 55
            brojPovecanjaBrzine++;

        }
        else if(brojPovecanjaBrzine < 6 && odabraniSvijet.equals("2")){
            myGameView.cubeSpeedY  = Math.abs( myGameView.cubeSpeedY) + brzinaIgraca;
            brojPovecanjaBrzine++;
        }

        myGameView.speedEksp = myGameView.speedEksp + 0.2f;
        myGameView.miciEfektEksp = myGameView.miciEfektEksp + 0.02f;

        myGameView.setStartPosition(radius , radius * 2);
        myGameView.randIgrac();
    }

    public void reset(){
        trenutnaBrzinaKocke = Math.abs (myGameView.cubeSpeedY);
        myGameView.cubeSpeedY = 0;

        Runnable r = new Runnable() {   //THREAD U POZADINI, tu ne stavljamo interface!!!   Preko handlera mjenjamo interface
            @Override
            public void run() {

                long futureTime = System.currentTimeMillis()+200;
                while(System.currentTimeMillis() <futureTime){
                    synchronized (this) {
                        try{
                            wait(futureTime-System.currentTimeMillis());
                        }catch (Exception ignored){}
                    }
                }
                myGameView.omoguciScoredLiniju = false;
                myGameView.cubeSpeedY = trenutnaBrzinaKocke;
                novaBrzinaKocke();
            }
        };
        Thread mojThread = new Thread(r);
        mojThread.start();
    }

    private Runnable myTask = new Runnable(){
        @Override
        public void run() {
            taskHandler.postDelayed(myTask, FRAME_RATE);
            myGameView.invalidate();
            myGameView.movePlayer();

            if (myGameView.udarBombe &&  !myGameView.omoguciLoading){
                myGameView.udarBombe = false;
                playMusicDestroy();
                novaBrzinaKocke();
            }

            if (myGameView.rezultat > trenutniHighScoreDB && !noviScore){
                noviScore = true;
                myGameView.omoguciNewLocalScore = true;
                noviLocalScore();
            }

            if(myGameView.stanjeZivota == 0){
                myGameView.loadanjeVelikeSkrinje();
                myGameView.omoguciLoading =  true;
                if (!globalVar.get_noAds()){
                    animation.cancel();
                    reklama.setText("");
                }
            }

            if (myGameView.pomakPozadine == 0){
                save_LoadDataSharedPref();
                taskHandler.removeCallbacks(myTask);
                pokreniLoading();
            }

            if (myGameView.omoguciEksplozijuIgraca){
                myGameView.pomakeEfektaEksplozije();
            }
        }
    };

    //  provjera stanja zlatnika
    public void save_LoadDataSharedPref(){
        int rezZaSpremanje = myGameView.rezultat;
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);

        //Loadanje
        int ukupnoZlatnika = sharedPref.getInt("ukupnoCoina",0);
        if (ukupnoZlatnika == -1){
            rezZaSpremanje = 0;
        }
        else
        {
            rezZaSpremanje = rezZaSpremanje + ukupnoZlatnika;
        }

        // spremanje
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("ukupnoCoina",rezZaSpremanje);
        editor.apply();
    }

    public void ProvjeraSvijeta(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        jacinaSFX = sharedPref.getInt("seekBarSFX",50);
        odabraniSvijet = sharedPref.getString("odabraniSvijet","1");
    }

    // animacija reklame
    public void kretanjeReklame(){

        animation = new TranslateAnimation(0.0f, screenWidth -   screenWidth * 0.3f,0.0f, 0.0f);
        animation.setDuration(5000);  // svaka iteracija
        animation.setRepeatCount(10000000);  // broj ponavljanja
        animation.setRepeatMode(2);   // lijevo - desno - desno
//        animation.setFillAfter(true);
        reklama.startAnimation(animation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (globalVar.get_stanjeMuzike()){
            pokreniServiceNakonResume();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (globalVar.get_stanjeMuzike() && (!noviActivity)){
            pauzirajService();
        }
    }

    public void pauzirajService(){
        String stanjeMuzikeInt;
        Intent service = new Intent(this, BackgroundSoundService.class);
        stanjeMuzikeInt = "2"; // pauza muzike
        service.putExtra("stanjeMuzike", stanjeMuzikeInt);
        startService(service);
    }

    public void pokreniServiceNakonResume(){
        String stanjeMuzikeInt;
        Intent service = new Intent(this, BackgroundSoundService.class);
        stanjeMuzikeInt = "1"; // pauza muzike
        service.putExtra("stanjeMuzike", stanjeMuzikeInt);
        startService(service);
    }
}
