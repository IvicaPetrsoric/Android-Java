package com.example.ivica.clumsybox.MainMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.Loading.Loading;
import com.example.ivica.clumsybox.Options.Options;

import com.example.ivica.clumsybox.Profile.Profile;
import com.example.ivica.clumsybox.R;
import com.example.ivica.clumsybox.Registration.Registration;
import com.example.ivica.clumsybox.Scores.Scores;

public class MainMenu extends Activity {

    //  samo jednom se moze kliknuti na dugme
    private boolean enableTocuh;

    private MenuGUI menuGUI;

    private Options options;
    private Profile profile;

    // asko je true onda rade dugmici od maina
    private boolean mainMenuActive;

    // TEST
    private Registration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hvatanje dimeznija ekrana
        getSceenDimension();

        menuGUI = new MenuGUI(this);

        // provjera pozicija ulaska
        provjeraDestinacije();

        // btn listeneri
        setUpBtnListeners();

        mainMenuActive = true;
    }

    public void getSceenDimension(){
        //  hvatanje dimenzija ekrana
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH_10 = Constants.SCREEN_WIDTH / 10;
        Constants.objectFrameWidth = Constants.SCREEN_WIDTH_10;
        Constants.objectFrameWidth_8 = Constants.SCREEN_WIDTH / 8;

        float yInches = dm.heightPixels/dm.ydpi;
        float xInches = dm.widthPixels/dm.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            System.out.println("TABLET");
            // 6.5inch device or bigger
        }else{
            System.out.println("PHONE");
            // smaller device
        }
    }

    // listeneri
    private void setUpBtnListeners(){
        menuGUI.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("btnPlay");
            }
        });

        menuGUI.btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnScore");

                Intent intent = new Intent(MainMenu.this, Scores.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);

                overridePendingTransition(R.anim.scores_slide_in, R.anim.alpha_out);
            }
        });

        menuGUI.btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOptionsView(true);
            }
        });
    }

    public void startRegistrationView(boolean show){
        if (show){
            mainMenuActive = false;
            registration = new Registration(this, menuGUI.menuView, false);

            registration.mainMenu = this;
        }else{

            registration = null;
            mainMenuActive = true;
        }
    }

    public void startOptionsView(boolean show){
        if (show){
            mainMenuActive = false;
            menuGUI.setVisibleBtnOptions(false);
            options = new Options(this);
            options.setRelativeLayout(menuGUI.menuView);
            options.mainMenu = this;
        }else{
            menuGUI.setVisibleBtnOptions(true);
            options = null;
            mainMenuActive = true;
            //menuGUI.btnOptions.setVisibility(View.VISIBLE);
        }
    }

    public void startProfileView(boolean show){
        if (show){
            mainMenuActive = false;
            menuGUI.setVisibleBtnProfile(false);
            profile = new Profile(this);
            profile.setRelativeLayout(menuGUI.menuView);
            profile.mainMenu = this;
        }else{
            menuGUI.setVisibleBtnProfile(true);
            profile = null;
            mainMenuActive = true;
            //menuGUI.btnOptions.setVisibility(View.VISIBLE);
        }
    }

    // provjera ako smo dolsli iz splasha ili iz igre
    private void provjeraDestinacije(){
        String destination = "";

        try{
            destination = getIntent().getExtras().getString("destination");


        }catch (Exception e){}

        if (destination == null){
            destination = "";
        }

        System.out.println("Game_destination: " + destination);

        if (destination == "menu" || destination.equals("menu")){

            enableTocuh = false;
            menuGUI.imgCircle.setVisibility(View.VISIBLE);
            menuGUI.menuAnimations.animateZoom(menuGUI.imgCircle, false, 1500);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuGUI.imgCircle.setVisibility(View.INVISIBLE);
                    enableTocuh = true;
                    //finish();
                }
            }, 1500);

        }else if (destination == ("static") || destination.equals("static")){
            menuGUI.imgCircle.setVisibility(View.INVISIBLE);
            enableTocuh = true;
        }

        else {
            Animation animation_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);
            Animation animation_7 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_menu_slide_in);

            animation_1.setDuration(1000);
            menuGUI.btnMultiplayer.startAnimation(animation_1);
            animation_2.setDuration(1500);
            menuGUI.btnPlay.startAnimation(animation_2);
            animation_3.setDuration(2000);
            menuGUI.btnPlayers.startAnimation(animation_3);
            animation_4.setDuration(2500);
            menuGUI.btnScore.startAnimation(animation_4);
            animation_5.setDuration(3000);
            menuGUI.btnAchievements.startAnimation(animation_5);
            animation_6.setDuration(3500);
            menuGUI.btnOptions.startAnimation(animation_6);
            animation_7.setDuration(4000);
            menuGUI.btnAds.startAnimation(animation_7);

            enableTocuh = true;
        }
    }

    private void startActivity(final String btn){
        if (mainMenuActive){
            int delayTime = 0;

            if (enableTocuh){
                enableTocuh = false;

                final String des = btn;

                switch (des) {
                    case "btnPlay":
                        delayTime = 2000;
                        menuGUI.imgCircle.setVisibility(View.VISIBLE);
                        menuGUI.menuAnimations.animateZoom(menuGUI.imgCircle, true, delayTime);

                        break;
                    case "btnOptions":
                        delayTime = 100;

                    default:
                        break;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent();


                        switch (des){
                            case "btnPlay":
                                intent = new Intent(MainMenu.this, Loading.class);
                                intent.putExtra("destination", "game");
                                menuGUI.blackScreen.setVisibility(View.VISIBLE);
                                //mainMenuView.setBackgroundColor(getResources().getColor(R.color.black));

                                startActivity(intent);
                                overridePendingTransition(0,0);

                                finish();

                                break;

                            case "btnOptions":
                                //intent = new Intent(MainMenu.this, OptionsTest.class);
                                //languageDialog();
                                break;

                            default:
                                break;
                        }

                    }
                }, delayTime);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (menuGUI.btnOptions.getVisibility() == View.INVISIBLE){
                options.closeOptionsView();
            }

            else if (menuGUI.btnProfile.getVisibility() == View.INVISIBLE){
                profile.closeProfileiew();
            }

            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

}

                    /*
                Intent intent = new Intent(MainMenu.this, Registration.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                */