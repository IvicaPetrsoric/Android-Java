package com.example.ivica.clumsybox.GameView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 21.4.2017..
 */

public class GameEnd implements GameObject{

    private Bitmap homeBtn;
    private int homeBtnX;
    private int homeBtnY;

    private Bitmap restartBtn;
    private int restartBtnX;
    private int restartBtnY;

    private Bitmap pauseBtn;
    private int pauseBtnX;
    private int pauseBtnY;

    private Bitmap playBtn;
    private int playBtnX;
    private int playBtnY;

    private float btnSize = 1.5f;
    private int btnWidth;

    private boolean enableAnim;
    private int alphaBack;
    private int alphaBtns;
    private long timeDelay;

    private boolean showBtns;
    private boolean btnsActive;

    private boolean playerDeath;

    private int playerScore;

    public GameEnd(){
        enableAnim = false;
        showBtns = false;
        btnsActive = false;
        playerDeath = false;

        playerScore = 0;

        alphaBack = 0;
        alphaBtns = 0;

        btnWidth =(int)(Constants.objectFrameWidth * btnSize);

        homeBtnX = Constants.SCREEN_WIDTH / 2 - Constants.SCREEN_WIDTH / 4 - btnWidth/2;
        homeBtnY = Constants.SCREEN_HEIGHT / 2 - btnWidth;

        restartBtnX = Constants.SCREEN_WIDTH / 2 + Constants.SCREEN_WIDTH / 4 - btnWidth/2;
        restartBtnY = homeBtnY;

        playBtnX = Constants.SCREEN_WIDTH / 2 - btnWidth/2;
        playBtnY = Constants.SCREEN_HEIGHT / 2;

        pauseBtnX = 3;
        pauseBtnY = (int)(Constants.objectFrameWidth * 1.5);

        Bitmap res_home = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.btn_home);
        Bitmap res_restart = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.btn_restart);
        Bitmap res_pause = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.btn_pause);
        Bitmap res_play = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.btn_play);


        homeBtn = Bitmap.createScaledBitmap(res_home, btnWidth, btnWidth, false);
        restartBtn = Bitmap.createScaledBitmap(res_restart, btnWidth, btnWidth, false);
        pauseBtn = Bitmap.createScaledBitmap(res_pause, (int)(btnWidth * 0.75), (int)(btnWidth * 0.75), false);
        playBtn = Bitmap.createScaledBitmap(res_play, btnWidth, btnWidth, false);
    }

    // provjera pozicije korisnikovog klika
    public String checkBtnPressed(float pozX, float pozY){
        String status = "";

        if (checkPositionY(pozY, pozX).equals(checkPositionX(pozX))){

            String statusPoz = checkPositionY(pozY, pozX);

            switch (statusPoz){

                case "PAUSE":
                    if (!showBtns){
                        showGameEndBtns(true);
                    }
                    break;

                case "PLAY":
                    if (showBtns && !playerDeath){
                        showGameEndBtns(false);
                    }
                    break;

                case "HOME":
                    status = statusPoz;
                    break;

                case "RESTART":
                    status = statusPoz;
                    showGameEndBtns(false);
                    break;
            }

            System.out.println("Dugme: " + statusPoz);
        }

        return status;
    }

    public void playerDead(int score){
        playerScore = score;
        playerDeath = true;
        showGameEndBtns(true);
    }

    public void showGameEndBtns(boolean activetBtns){
        if (activetBtns){
            alphaBack = 0;
            alphaBtns = 0;
            timeDelay = System.nanoTime();
            showBtns = true;
            btnsActive = true;
            enableAnim = true;
        }
        else {
            timeDelay = System.nanoTime();
            enableAnim = true;
            showBtns = false;
        }
    }

    private String checkPositionX(float pozX){
        // provjera na poziciji botuna pause
        if (pozX > pauseBtnX && pozX < pauseBtnX + btnWidth){
            return "PAUSE";
        }

        // provjera ako je na poziciji home
        else if (pozX > homeBtnX && pozX < homeBtnX + btnWidth){
            return "HOME";

        }

        // provjera ako je na poziciji dugme restart
        else if (pozX > restartBtnX && pozX < restartBtnX + btnWidth){
            return "RESTART";
        }

        // provjera ako je na poziciji play
        else if (pozX > playBtnX && pozX < playBtnX + btnWidth){
            return "PLAY";
        }

        return "";
    }

    private String checkPositionY(float pozY, float pozX){
        // provjera na poziciji botuna pause
        if (pozY > pauseBtnY && pozY < pauseBtnY + btnWidth){
            return "PAUSE";
        }

        // provjera ako je na poziciji home
        else if (pozY > homeBtnY && pozY < homeBtnY + btnWidth){
            if(checkPositionX(pozX).equals("HOME")) {
                return "HOME";
            }else if (checkPositionX(pozY).equals("RESTART")){
                return "RESTART";
            }
        }

        /*
            ne treba jer home ja na istom X-u pa treba kod njega provjeriti y
        */

        // provjera ako je na poziciji play
        else if (pozY > playBtnY && pozY < playBtnY + btnWidth){
            return "PLAY";
        }

        return "";
    }

    // animiraj alphu dugmica
    private void animAlpha(boolean show){
        // prikazi dugmice
        if (show){

            if (alphaBack < 127){
                alphaBack +=3;
            }

            if (alphaBtns < 255){
                alphaBtns += 3;
            }

            if (alphaBtns >= 255) enableAnim = false;
        }

        // makni dugmice
        else{
            if (alphaBack > 5){
                alphaBack -=3;
            }

            if (alphaBtns > 9){
                alphaBtns -=3;
            }

            if (alphaBtns <= 10) {
                enableAnim = false;
                btnsActive = false;
                playerDeath = false;
            }
        }
    }

    // vraca status botunua, ako su aktivni treba onemoguciti samo respond klikanja po njima
    public boolean btnStatus(){
        return btnsActive;
    }


    @Override
    public void update() {

        if (enableAnim){
            long elapsed_2 = (System.nanoTime() - timeDelay) / 1000000;
            if (elapsed_2 > 5){
                //System.out.println("DA");

                animAlpha(showBtns);
                timeDelay = System.nanoTime();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // pause btn
        if (!playerDeath){
            canvas.drawBitmap(pauseBtn, pauseBtnX, pauseBtnY, null);
        }

        if (btnsActive){

            // pozadina kada se aktiviraju dugmici
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAlpha(alphaBack);

            canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, paint);


            Paint paint_2 = new Paint();
            paint_2.setColor(Color.BLACK);
            paint_2.setAlpha(alphaBtns);

            canvas.drawBitmap(homeBtn, homeBtnX, homeBtnY, paint_2);
            canvas.drawBitmap(restartBtn, restartBtnX, restartBtnY, paint_2);

            if (playerDeath){
                Paint paint_3 = new Paint();
                paint_3.setTextSize(Constants.objectFrameWidth * 1f);
                paint_3.setColor(Color.WHITE);
                paint_3.setAlpha(alphaBtns);
                paint_3.setFakeBoldText(true);
                paint_3.isFakeBoldText();
                paint_3.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Score: " + playerScore , Constants.SCREEN_WIDTH / 2, (Constants.SCREEN_HEIGHT / 4)  + paint.descent() - paint.ascent(), paint_3);
            }
            else{
                canvas.drawBitmap(playBtn, playBtnX, playBtnY,  paint_2);
            }
        }
    }

    public void recycle(){
        homeBtn.recycle();
        restartBtn.recycle();
        pauseBtn.recycle();
        playBtn.recycle();
    }

}
