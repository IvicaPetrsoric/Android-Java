package com.example.ivica.clumsybox.MainMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 24.4.2017..
 */

public class MenuGUI extends View {

    // view
    public RelativeLayout menuView;

    // botuni
    public ImageButton btnPlay, btnScore, btnOptions, btnProfile, btnPlayers, btnAchievements, btnMultiplayer, btnShopCoins, btnShopDiamonds;
    public Button btnAds;

    public ImageView imgCircle, blackScreen;
    public TextView coinsText,diamomndsText;

    private ImageView imgClouds, imgBox;

    private double btnSize;

    Activity myActivity;

    // kontrola animacija
    MenuAnimations menuAnimations;

    public MenuGUI(Context context) {
        super(context);

        myActivity = (Activity) context;
        menuAnimations = new MenuAnimations();

        btnSize = Constants.SCREEN_WIDTH * 0.2;

        createGUI();
        createBlackScreen();
    }

    public void createCoinsDiamonds(){
        // coins
        ImageView coinsBackground = new ImageView(myActivity);
        coinsBackground.setBackgroundColor(Color.rgb(1,182,212));
        menuView.addView(coinsBackground);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) coinsBackground.getLayoutParams();
        params.height = (int)(btnSize * 0.75 / 2);
        params.width = (int)(btnSize * 1.75);
        params.rightMargin = (int)(btnSize * 0.75/ 2);
        params.topMargin = (int)(btnSize * 0.75/ 4)  ;
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        coinsBackground.setLayoutParams(params);

        ImageView coinsImg = new ImageView(myActivity);
        coinsImg.setImageResource(R.drawable.coins);
        menuView.addView(coinsImg);

        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) coinsImg.getLayoutParams();
        params_1.height = (int)(btnSize * 0.75);
        params_1.width = (int)(btnSize * 0.75);
        params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        coinsImg.setLayoutParams(params_1);

        coinsText = new TextView(myActivity);
        coinsText.setTextSize(15);
        coinsText.setText("1000");
        coinsText.setTextColor(Color.WHITE);
        coinsText.setGravity(Gravity.RIGHT);
        menuView.addView(coinsText);

        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) coinsText.getLayoutParams();
        params_2.height = (int)(btnSize * 0.75 / 2);
        params_2.width = (int)(btnSize * 1.38);
        params_2.rightMargin = (int)(btnSize * 0.71);
        params_2.topMargin = (int)(btnSize * 0.75/ 4)  ;
        params_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        coinsText.setLayoutParams(params_2);

        btnShopCoins = new ImageButton(myActivity);
        btnShopCoins.setBackground(getResources().getDrawable(R.drawable.btn_shop));
        menuView.addView(btnShopCoins);

        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) btnShopCoins.getLayoutParams();
        params_3.height = (int)(btnSize * 0.5);
        params_3.width = (int)(btnSize * 0.5);
        params_3.rightMargin = (int)(btnSize * 0.71) + (int)(btnSize * 1.38) ;
        params_3.topMargin = (int)(btnSize * 0.125);
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnShopCoins.setLayoutParams(params_3);

        // DIAMONDS
        btnShopDiamonds = new ImageButton(myActivity);
        btnShopDiamonds.setBackground(getResources().getDrawable(R.drawable.btn_shop));
        menuView.addView(btnShopDiamonds);

        RelativeLayout.LayoutParams params_7 = (RelativeLayout.LayoutParams) btnShopDiamonds.getLayoutParams();
        params_7.height = (int)(btnSize * 0.5);
        params_7.width = (int)(btnSize * 0.5);
        params_7.leftMargin = (int)(btnSize * 0.05);
        params_7.topMargin = (int)(btnSize * 0.125);
        params_7.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnShopDiamonds.setLayoutParams(params_7);

        ImageView diamondsBackground = new ImageView(myActivity);
        diamondsBackground.setBackgroundColor(Color.rgb(1,182,212));
        menuView.addView(diamondsBackground);

        RelativeLayout.LayoutParams params_4 = (RelativeLayout.LayoutParams) diamondsBackground.getLayoutParams();
        params_4.height = (int)(btnSize * 0.75 / 2);
        params_4.width = (int)(btnSize * 1.3);
        params_4.leftMargin = (int)(btnSize * 0.55);
        params_4.topMargin = (int)(btnSize * 0.75/ 4)  ;
        params_4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        diamondsBackground.setLayoutParams(params_4);

        diamomndsText = new TextView(myActivity);
//        diamomndsText.setBackgroundColor(Color.RED)/
        diamomndsText.setTextSize(15);
        diamomndsText.setText("1000");
        diamomndsText.setTextColor(Color.WHITE);
        diamomndsText.setGravity(Gravity.RIGHT);
        menuView.addView(diamomndsText);

        RelativeLayout.LayoutParams params_6 = (RelativeLayout.LayoutParams) diamomndsText.getLayoutParams();
        params_6.height = (int)(btnSize * 0.75 / 2);
        params_6.width = (int)(btnSize * 1.075);
        params_6.leftMargin = (int)(btnSize * 0.57);
        params_6.topMargin = (int)(btnSize * 0.75/ 4)  ;
        params_6.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        diamomndsText.setLayoutParams(params_6);

        ImageView diamondsImg = new ImageView(myActivity);
        diamondsImg.setImageResource(R.drawable.diamonds);
        menuView.addView(diamondsImg);

        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) diamondsImg.getLayoutParams();
        params_5.height = (int)(btnSize * 0.75);
        params_5.width = (int)(btnSize * 0.75);
        params_5.leftMargin = (int)(btnSize * 0.5) + (int)(btnSize * 1.1);
        params_5.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        diamondsImg.setLayoutParams(params_5);
    }

    public void createGUI(){
        // pozadina
        menuView = new RelativeLayout(myActivity);
        menuView.layout(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        menuView.setBackground(getResources().getDrawable(R.drawable.home_view));

        // pozadinska kocka koja se krece gore-dole
        createMovingBox();

        // pozadinski oblaci
        createClouds();

        createCoinsDiamonds();

        // botun play
        btnPlay = new ImageButton(myActivity);
        btnPlay.setBackground(getResources().getDrawable(R.drawable.btn_play));
        menuView.addView(btnPlay);

        // parametri botuna play
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnPlay.getLayoutParams();
        params.height = (int) (Constants.SCREEN_WIDTH * 0.3);
        params.width = (int) (Constants.SCREEN_WIDTH * 0.3);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        btnPlay.setLayoutParams(params);

        // botun Score
        btnScore = new ImageButton(myActivity);
        btnScore.setBackground(getResources().getDrawable(R.drawable.btn_scores));
        menuView.addView(btnScore);

        // parametri botuna score
        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnScore.getLayoutParams();
        params_1.height = (int) btnSize;
        params_1.width = (int) btnSize;
        params_1.topMargin = (int)(7f * Constants.SCREEN_HEIGHT / 10);
        params_1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnScore.setLayoutParams(params_1);

        // botun players
        btnPlayers = new ImageButton(myActivity);
        btnPlayers.setBackground(getResources().getDrawable(R.drawable.btn_players));
        menuView.addView(btnPlayers);

        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) btnPlayers.getLayoutParams();
        params_2.height = (int) btnSize;
        params_2.width = (int) btnSize;
        params_2.leftMargin = Constants.SCREEN_WIDTH / 2 - (int) (btnSize * 1.5);
        params_2.topMargin = (int)(6.5f * Constants.SCREEN_HEIGHT / 10);
        btnPlayers.setLayoutParams(params_2);

        // btn achievements
        btnAchievements = new ImageButton(myActivity);
        btnAchievements.setBackground(getResources().getDrawable(R.drawable.btn_ach));
        menuView.addView(btnAchievements);

        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) btnAchievements.getLayoutParams();
        params_3.height = (int) btnSize;
        params_3.width = (int) btnSize;
        params_3.leftMargin = Constants.SCREEN_WIDTH / 2 + (int) (btnSize * 0.5);
        params_3.topMargin = (int)(6.5f * Constants.SCREEN_HEIGHT / 10);
        btnAchievements.setLayoutParams(params_3);

        // btn multiplayer
        btnMultiplayer = new ImageButton(myActivity);
        btnMultiplayer.setBackground(getResources().getDrawable(R.drawable.btn_multiplayer));
        menuView.addView(btnMultiplayer);

        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) btnMultiplayer.getLayoutParams();
        params_5.height = (int) btnSize;
        params_5.width = (int) btnSize;
        params_5.topMargin = (int)( Constants.SCREEN_HEIGHT / 10);
        btnMultiplayer.setLayoutParams(params_5);

        // btn ads
        btnAds = new Button(myActivity);
        btnAds.setBackground(getResources().getDrawable(R.drawable.btn));
        btnAds.setTextSize(30);
        btnAds.setTextColor(Color.WHITE);
        btnAds.setText("NO\nADS");
        menuView.addView(btnAds);

        RelativeLayout.LayoutParams params_6 = (RelativeLayout.LayoutParams) btnAds.getLayoutParams();
        params_6.height = (int) btnSize;
        params_6.width = (int) btnSize;
        params_6.topMargin = (int)( 2.25 * Constants.SCREEN_HEIGHT / 10);
        params_6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnAds.setLayoutParams(params_6);

        // btn options
        createOptionsBtn();

        // stvaranje crnog kruga
        imgCircle = new ImageView(myActivity);
        imgCircle.setImageResource(R.drawable.black_circle);
        menuView.addView(imgCircle);

        // da se dobi duzina koju krug mora napraviti da popuni cijeli ekran
        //int diagonalXY = (int)Math.sqrt((Constants.SCREEN_WIDTH * Constants.SCREEN_WIDTH) + (Constants.SCREEN_HEIGHT * Constants.SCREEN_HEIGHT)) * 5;

        // parametri crnog kruga
        RelativeLayout.LayoutParams params_4 = (RelativeLayout.LayoutParams) imgCircle.getLayoutParams();
        params_4.height = Constants.SCREEN_WIDTH * 2;
        params_4.width = Constants.SCREEN_WIDTH * 2;
        //params_4.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.ABOVE);
        imgCircle.setLayoutParams(params_4);

        //imgCircle.setAdjustViewBounds(true);
        imgCircle.setVisibility(View.INVISIBLE);

        myActivity.setContentView(menuView);
    }

    private void createOptionsBtn(){
        // botun Options
        btnOptions = new ImageButton(myActivity);
        btnOptions.setBackground(getResources().getDrawable(R.drawable.btn_options));
        menuView.addView(btnOptions);

        // parametri botuna options
        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) btnOptions.getLayoutParams();
        params_3.height = (int)btnSize;
        params_3.width = (int)btnSize;
        params_3.topMargin = 1 * Constants.SCREEN_HEIGHT / 10;
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnOptions.setLayoutParams(params_3);
    }

    // podešavanje ako se vidi botun ili ne
    public void setVisibleBtnOptions(boolean visible){
        if (visible){
            btnOptions.clearAnimation();
            //btnOptions.setBackground(getResources().getDrawable(R.drawable.dugm_options_x2));
            btnOptions.setVisibility(menuView.VISIBLE);
        }else{
            btnOptions.clearAnimation();
            btnOptions.setVisibility(menuView.INVISIBLE);
            //btnOptions.setBackground(null);
        }
    }

    // podesavanje botuna profile
    public void setVisibleBtnProfile(boolean visible){
        if (visible){
            btnProfile.clearAnimation();
            //btnOptions.setBackground(getResources().getDrawable(R.drawable.dugm_options_x2));
            btnProfile.setVisibility(menuView.VISIBLE);
        }else{
            btnProfile.clearAnimation();
            btnProfile.setVisibility(menuView.INVISIBLE);
            //btnOptions.setBackground(null);
        }
    }

    // stvori oblake u pozadini
    private void createClouds(){
        imgClouds = new ImageView(myActivity);
        imgClouds.setImageResource(R.drawable.home_clouds);
        imgClouds.layout(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        menuView.addView(imgClouds);

        imgClouds.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        imgClouds.setScaleType(ImageView.ScaleType.FIT_XY);

        // animacija micanja
        menuAnimations.animateClouds(imgClouds);
    }

    private void createMovingBox(){
        imgBox = new ImageView(myActivity);
        imgBox.setImageResource(R.drawable.player0);
        menuView.addView(imgBox);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgBox.getLayoutParams();
        params.height = Constants.objectFrameWidth_8;
        params.width = Constants.objectFrameWidth_8;
        imgBox.setLayoutParams(params);

        // pokretanje animacije
        menuAnimations.animateMovigBox(imgBox, true);
    }

    private void createBlackScreen(){
        //  CRNA POZADINA
        blackScreen = new ImageView(myActivity);
        menuView.addView(blackScreen);

        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) blackScreen.getLayoutParams();
        params_5.height = Constants.SCREEN_HEIGHT;
        params_5.width = Constants.SCREEN_WIDTH;

        blackScreen.setLayoutParams(params_5);

        blackScreen.setBackgroundColor(getResources().getColor(R.color.black));
        blackScreen.setVisibility(View.INVISIBLE);
    }

}
