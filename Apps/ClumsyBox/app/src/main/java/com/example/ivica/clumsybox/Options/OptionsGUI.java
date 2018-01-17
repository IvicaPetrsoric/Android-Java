package com.example.ivica.clumsybox.Options;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 25.4.2017..
 */

public class OptionsGUI extends View{

    private RelativeLayout optionsView;

    public ImageButton btnOptions_2, btnMusic, btnSFX;

    public Button btnClose, btnCloseMainExt, btnCloseMusicExt, btnCloseSfxExt;

    private ImageView mainExtender, musicExtender, musicExtenderBlock, sfxExtender, sfxExtenderBlock;

    public SeekBar musicSeekBar, sfxSeekBar;
    public TextView musicText, sfxText;

    private float textSize = 13;

    private Activity myActivity;
    private OptionsAnimations optionsAnimations;

    public OptionsGUI(Context context){
        super(context);

        myActivity = (Activity)context;
        optionsAnimations = new OptionsAnimations();

        getSceenDimension();
    }

    private void getSceenDimension(){
        //  hvatanje dimenzija ekrana
        DisplayMetrics dm = new DisplayMetrics();
        myActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        float yInches = dm.heightPixels/dm.ydpi;
        float xInches = dm.widthPixels/dm.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            System.out.println("TABLET");
            textSize = 22;
            // 6.5inch device or bigger
        }else{
            System.out.println("PHONE");
            textSize = 12;
            // smaller device
        }
    }

    public void setRelativeLayout(RelativeLayout layout){
        this.optionsView = layout;
        createGUI();
    }

    private void createGUI(){
        btnClose = new Button(myActivity);
        btnClose.setBackgroundColor(Color.TRANSPARENT);
        optionsView.addView(btnClose);

        // parametri botuna close
        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
        params_1.height = Constants.SCREEN_HEIGHT;
        params_1.width = Constants.SCREEN_WIDTH;
        btnClose.setLayoutParams(params_1);

        // stvaranje botuna koji ne dopuštanju da se options zatvori ako se klikne na plavu pozadinu jer inače hvata glavni botun zatvaranja koji je preko cijelog ekrana
        btnCloseMainExt = new Button(myActivity);
        btnCloseMainExt.setBackgroundColor(Color.TRANSPARENT);
        optionsView.addView(btnCloseMainExt);

        // parametri botuna close
        RelativeLayout.LayoutParams params_4 = (RelativeLayout.LayoutParams) btnCloseMainExt.getLayoutParams();
        params_4.height = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_4.width = (int)(Constants.objectFrameWidth_8 * 3.5f);
        params_4.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + params_4.height / 16;
        params_4.rightMargin = Constants.objectFrameWidth_8;
        params_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnCloseMainExt.setLayoutParams(params_4);


        // botun ispod music extender
        btnCloseMusicExt = new Button(myActivity);
        btnCloseMusicExt.setBackgroundColor(Color.TRANSPARENT);
        optionsView.addView(btnCloseMusicExt);
        btnCloseMusicExt.setVisibility(INVISIBLE);

        // parametri botuna close
        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) btnCloseMusicExt.getLayoutParams();
        params_5.height = (int)(Constants.objectFrameWidth_8 * 3.5f);
        params_5.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_5.topMargin = (2 * Constants.SCREEN_HEIGHT / 6);
        params_5.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnCloseMusicExt.setLayoutParams(params_5);

        // botun ispod SFX extendera

        btnCloseSfxExt = new Button(myActivity);
        btnCloseSfxExt.setBackgroundColor(Color.TRANSPARENT);
        optionsView.addView(btnCloseSfxExt);
        btnCloseSfxExt.setVisibility(INVISIBLE);

        // parametri botuna close
        RelativeLayout.LayoutParams params_6 = (RelativeLayout.LayoutParams) btnCloseSfxExt.getLayoutParams();
        params_6.height = (int)(Constants.objectFrameWidth_8 * 3.5f);
        params_6.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_6.topMargin = (2 * Constants.SCREEN_HEIGHT / 6);
        params_6.rightMargin = Constants.objectFrameWidth_8 * 3;
        params_6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnCloseSfxExt.setLayoutParams(params_6);

        // sve sta mora biti ispod botuna options
        createSecondGUI();

        // botun Options
        btnOptions_2 = new ImageButton(myActivity);
        btnOptions_2.setBackground(getResources().getDrawable(R.drawable.btn_options));
        //btnOptions.layout(0, 0, 150, 150);
        optionsView.addView(btnOptions_2);

        // parametri botuna options
        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) btnOptions_2.getLayoutParams();
        params_3.height = Constants.SCREEN_WIDTH / 4;
        params_3.width = Constants.SCREEN_WIDTH / 4;
        params_3.topMargin = 2 * Constants.SCREEN_HEIGHT / 10;
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        btnOptions_2.setLayoutParams(params_3);

        startOptionsAnimation(true);
    }

    private void createSecondGUI(){
        // music
        // music extender block
        musicExtenderBlock = new ImageView(myActivity);
        musicExtenderBlock.setBackground(getResources().getDrawable(R.drawable.options_background_rect));
        optionsView.addView(musicExtenderBlock);

        // parametri
        RelativeLayout.LayoutParams params_6 = (RelativeLayout.LayoutParams) musicExtenderBlock.getLayoutParams();
        params_6.height = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_6.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_6.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_6.height * 1.25f);
        params_6.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        musicExtenderBlock.setLayoutParams(params_6);

        musicExtenderBlock.setVisibility(INVISIBLE);

        // music extender
        musicExtender = new ImageView(myActivity);
        musicExtender.setBackground(getResources().getDrawable(R.drawable.options_background_ccw));
        optionsView.addView(musicExtender);

        // parametri
        RelativeLayout.LayoutParams params_4 = (RelativeLayout.LayoutParams) musicExtender.getLayoutParams();
        params_4.height = (int)(Constants.objectFrameWidth_8 * 2.5f);
        params_4.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_4.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_4.height * 1.05f);
        params_4.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        musicExtender.setLayoutParams(params_4);

        musicExtender.setVisibility(INVISIBLE);

        // SFX
        // sfx extender block
        sfxExtenderBlock = new ImageView(myActivity);
        sfxExtenderBlock.setBackground(getResources().getDrawable(R.drawable.options_background_rect));
        optionsView.addView(sfxExtenderBlock);

        // parametri
        RelativeLayout.LayoutParams params_7 = (RelativeLayout.LayoutParams) sfxExtenderBlock.getLayoutParams();
        params_7.height = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_7.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_7.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_7.height * 1.25f);
        params_7.rightMargin = (int)(Constants.objectFrameWidth_8 * 3);
        params_7.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sfxExtenderBlock.setLayoutParams(params_7);

        sfxExtenderBlock.setVisibility(INVISIBLE);

        // sfx extender
        sfxExtender = new ImageView(myActivity);
        sfxExtender.setBackground(getResources().getDrawable(R.drawable.options_background_ccw));
        optionsView.addView(sfxExtender);

        // parametri
        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) sfxExtender.getLayoutParams();
        params_5.height = (int)(Constants.objectFrameWidth_8 * 2.5f);
        params_5.width = (int)(Constants.objectFrameWidth_8 * 1.25f);
        params_5.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int) (params_4.height * 1.05f) ;
        params_5.rightMargin = Constants.objectFrameWidth_8 * 3;
        params_5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sfxExtender.setLayoutParams(params_5);

        sfxExtender.setVisibility(INVISIBLE);

        // glavni extender
        mainExtender = new ImageView(myActivity);
        mainExtender.setBackground(getResources().getDrawable(R.drawable.options_background));

        optionsView.addView(mainExtender);
        mainExtender.setAlpha(0);

        // parametri
        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) mainExtender.getLayoutParams();
        params_1.height = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_1.width = (int)(Constants.objectFrameWidth_8 * 3.5f);
        params_1.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + params_1.height / 16;
        params_1.rightMargin = Constants.objectFrameWidth_8;
        params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mainExtender.setLayoutParams(params_1);

        float btnSize = 1f;

        //  BOTUN SFX
        btnSFX = new ImageButton(myActivity);
        btnSFX.setBackground(getResources().getDrawable(R.drawable.sfx_on));
        optionsView.addView(btnSFX);

        // parametri
        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) btnSFX.getLayoutParams();
        params_2.height = (int)(Constants.objectFrameWidth_8 * btnSize);
        params_2.width = (int)(Constants.objectFrameWidth_8 * btnSize);
        params_2.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(Constants.objectFrameWidth_8 * 0.1875f) + params_2.height / 4;
        params_2.rightMargin = (int)(Constants.objectFrameWidth_8 * 3.1f);
        params_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnSFX.setLayoutParams(params_2);

        // botun music
        btnMusic = new ImageButton(myActivity);
        btnMusic.setBackground(getResources().getDrawable(R.drawable.music_on));
        optionsView.addView(btnMusic);

        // parametri
        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) btnMusic.getLayoutParams();
        params_3.height = (int)(Constants.objectFrameWidth_8 * btnSize);
        params_3.width = (int)(Constants.objectFrameWidth_8 * btnSize);
        params_3.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(Constants.objectFrameWidth_8 * 0.1875f) + params_3.height / 4 ;
        params_3.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.9f);
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnMusic.setLayoutParams(params_3);

        createSeekBars();
    }

    // seek bar i njihovi textViwi
    private void createSeekBars(){

        //  Postavke seekBarova
        // music
        // dio nakon thumba
        /*
        GradientDrawable shape2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.rgb(25, 75, 100), Color.GREEN});
        shape2.setCornerRadius(50);
        ClipDrawable clip = new ClipDrawable(shape2, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // dio prije thumba
        GradientDrawable shape1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.GRAY, getResources().getColor(R.color.gray)});
        shape1.setCornerRadius(50);//change the corners of the rectangle
        InsetDrawable d1=  new InsetDrawable(shape1,0,0,0,0);//the padding u want to use

        LayerDrawable mylayer_1 = new LayerDrawable(new Drawable[]{d1,clip});


        // sfx
        GradientDrawable shape4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.rgb(25, 75, 100), Color.GREEN});
        shape4.setCornerRadius(50);
        ClipDrawable clip_2 = new ClipDrawable(shape4, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // dio prije thumba
        GradientDrawable shape3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.GRAY, getResources().getColor(R.color.gray)});
        shape3.setCornerRadius(50);//change the corners of the rectangle
        InsetDrawable d2=  new InsetDrawable(shape3,0,0,0,0);//the padding u want to use
        LayerDrawable mylayer_2 = new LayerDrawable(new Drawable[]{d2,clip_2});
        */

        // before thumb
        GradientDrawable shape4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{ Color.GREEN, Color.GREEN});
        shape4.setCornerRadius((int)(Constants.objectFrameWidth_8 * 0.5f));
        ClipDrawable clip_2 = new ClipDrawable(shape4, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        // after thumb
        GradientDrawable shape3 = new GradientDrawable();
        shape3.setColor(Color.GRAY);
        shape3.setCornerRadius((int)(Constants.objectFrameWidth_8 * 0.5f));
        InsetDrawable d2=  new InsetDrawable(shape3,0,0,0,0);//the padding u want to use
        LayerDrawable mylayer_2 = new LayerDrawable(new Drawable[]{d2,clip_2});

        // music seek bar
        musicSeekBar = new SeekBar(myActivity);
        //Drawable thumb = getResources().getDrawable( R.drawable.player_box );
        musicSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        //musicSeekBar.setThumb(thumb);
        musicSeekBar.setProgressDrawable(mylayer_2);

        optionsView.addView(musicSeekBar);

        GradientDrawable thumb = new GradientDrawable();
        thumb.setColor(Color.WHITE);
        thumb.setCornerRadius((int)(Constants.objectFrameWidth_8 * 0.8f) / 2);
        thumb.setSize((int)(Constants.objectFrameWidth_8 * 0.8f),(int)(Constants.objectFrameWidth_8 * 0.8f));


        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) musicSeekBar.getLayoutParams();
        params_1.height = (int)(Constants.objectFrameWidth_8 * 1f);
        params_1.width = (int)(Constants.objectFrameWidth_8 * 3.05f);
        params_1.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_1.width * 0.825f);
        params_1.rightMargin = (int)(Constants.objectFrameWidth_8 * 0.825f);

        params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        int padding = (int)(Constants.objectFrameWidth_8 * 0.4f);
        musicSeekBar.setPadding(padding,padding,padding,padding);
        musicSeekBar.setThumb(thumb);
        musicSeekBar.setThumbOffset(0);
        musicSeekBar.setLayoutParams(params_1);
        musicSeekBar.setRotation(-90);
        musicSeekBar.setVisibility(INVISIBLE);
        musicSeekBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

        // music seek bars texView
        musicText = new TextView(myActivity);
        musicText.setTextSize((int)(textSize));
        musicText.setText("" + musicSeekBar.getProgress());
        musicText.setGravity(Gravity.CENTER);
        musicText.setTextColor(Color.WHITE);
        optionsView.addView(musicText);
        musicText.setVisibility(INVISIBLE);

        RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.CENTER_VERTICAL);
        params_2.height = Constants.objectFrameWidth_8;
        params_2.width = Constants.objectFrameWidth_8;
        params_2.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_1.width * 1.35f);
        params_2.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.875f);
        params_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        musicText.setLayoutParams(params_2);

        // sfx seek bar
        sfxSeekBar = new SeekBar(myActivity);
        optionsView.addView(sfxSeekBar);
        sfxSeekBar.setProgressDrawable(mylayer_2);
        sfxSeekBar.setVisibility(INVISIBLE);

        GradientDrawable thumb_2 = new GradientDrawable();
        thumb_2.setColor(Color.WHITE);
        thumb_2.setCornerRadius((int)(Constants.objectFrameWidth_8 * 0.8f) / 2);
        thumb_2.setSize((int)(Constants.objectFrameWidth_8 * 0.8f),(int)(Constants.objectFrameWidth_8 * 0.8f));
        // params
        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) sfxSeekBar.getLayoutParams();
        params_3.height = (int)(Constants.objectFrameWidth_8 * 1f);
        params_3.width = (int)(Constants.objectFrameWidth_8 * 3.05f);
        params_3.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_3.width * 0.825f);
        params_3.rightMargin = (int)(Constants.objectFrameWidth_8 * 2.1f);
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        sfxSeekBar.setThumb(thumb_2);
        sfxSeekBar.setPadding(padding,padding,padding,padding);
        sfxSeekBar.setThumbOffset(0);
        sfxSeekBar.setLayoutParams(params_3);
        sfxSeekBar.setRotation(-90);

        // sfx seek bars texView
        sfxText = new TextView(myActivity);
        sfxText.setTextSize((int)(textSize));
        sfxText.setText("" + sfxSeekBar.getProgress());
        //sfxText.setTypeface(null, Typeface.BOLD);
        sfxText.setGravity(Gravity.CENTER);
        sfxText.setTextColor(Color.WHITE);
        //sfxText.setBackgroundColor(Color.RED);
        optionsView.addView(sfxText);
        sfxText.setVisibility(INVISIBLE);

        // params
        RelativeLayout.LayoutParams params_4 = new RelativeLayout.LayoutParams(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.CENTER_VERTICAL);
        params_4.height = Constants.objectFrameWidth_8;
        params_4.width = Constants.objectFrameWidth_8;
        params_4.topMargin = (2 * Constants.SCREEN_HEIGHT / 10) + (int)(params_1.width * 1.35f);
        params_4.rightMargin = (int)(Constants.objectFrameWidth_8 * 3.125f);
        params_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sfxText.setLayoutParams(params_4);

        // podeavanje ikona glavnih botuna
        changeBtnImg(false, sfxSeekBar.getProgress());
        changeBtnImg(true, musicSeekBar.getProgress());
    }

    public void visibleMinorExtenders(boolean music){
        if (music){
            btnCloseMusicExt.setVisibility(VISIBLE);
            musicExtender.setVisibility(VISIBLE);
            musicExtenderBlock.setVisibility(VISIBLE);
            musicSeekBar.setVisibility(VISIBLE);
            musicText.setVisibility(VISIBLE);


            btnCloseSfxExt.setVisibility(INVISIBLE);
            sfxExtender.setVisibility(INVISIBLE);
            sfxExtenderBlock.setVisibility(INVISIBLE);
            sfxSeekBar.setVisibility(INVISIBLE);
            sfxText.setVisibility(INVISIBLE);
        }else{
            btnCloseMusicExt.setVisibility(INVISIBLE);
            musicExtender.setVisibility(INVISIBLE);
            musicExtenderBlock.setVisibility(INVISIBLE);
            musicSeekBar.setVisibility(INVISIBLE);
            musicText.setVisibility(INVISIBLE);

            btnCloseSfxExt.setVisibility(VISIBLE);
            sfxExtender.setVisibility(VISIBLE);
            sfxExtenderBlock.setVisibility(VISIBLE);
            sfxSeekBar.setVisibility(VISIBLE);
            sfxText.setVisibility(VISIBLE);
        }
    }

    // na početku i kgraju zatvaranj opcija
    public void startOptionsAnimation(boolean activate){
        int offSet = 0;

        //  Show elements
        if (activate){
            optionsAnimations.extendMainExtender(mainExtender, activate, 600, 0);
            optionsAnimations.moveBtnMusic(btnSFX, activate,false, 700, 200 );
            optionsAnimations.moveBtnMusic(btnMusic, activate,true, 800, 200 );
            optionsAnimations.rotateOptionsBtn(btnOptions_2, activate, 1100, 0);

        }else {
            // ako je aktivan neki od minor extendera treba dodati offset pri zatvaranju
            if (showSfxExtender){
                showMinorExtender("sfx");
                offSet = 500;
            }else if (showMusicExtender) {
                offSet = 500;
                showMinorExtender("music");
            }

            optionsAnimations.extendMainExtender(mainExtender,activate,900, 0 + offSet);
            optionsAnimations.moveBtnMusic(btnSFX, activate, false, 800, 0 + offSet);
            optionsAnimations.moveBtnMusic(btnMusic, activate,true, 700, 0 + offSet);
            optionsAnimations.rotateOptionsBtn(btnOptions_2, activate, 1100, 0 + offSet);
        }
    }

    public boolean showMusicExtender = false;
    public boolean showSfxExtender = false;

    public void showMinorExtender(String extenderName){

        switch (extenderName){

            case "music":

                // zatvaranje
                if (showMusicExtender){
                    showMusicExtender = false;
                    // musfix
                    optionsAnimations.extendMinorExtenders(musicExtenderBlock, false, 200, 400);
                    optionsAnimations.extendMinorExtenders(musicExtender, false, 400, 100);

                    optionsAnimations.changeAlphaSeekBars(musicSeekBar,false,300,0);
                    optionsAnimations.changeAlphaTextViews(musicText,false,300,0);
                }else {
                    showMusicExtender = true;
                    visibleMinorExtenders(true);
                    // musfix
                    optionsAnimations.extendMinorExtenders(musicExtenderBlock, true, 450, 0);
                    optionsAnimations.extendMinorExtenders(musicExtender, true, 300, 300);

                    optionsAnimations.changeAlphaSeekBars(musicSeekBar,true,300,150);
                    optionsAnimations.changeAlphaTextViews(musicText,true,300,150);

                    if (showSfxExtender){
                        showMinorExtender("sfx");
                    }
                }

                break;

            case "sfx":

                System.out.println("SFX");

                // makni
                if (showSfxExtender){
                    // sfx

                    showSfxExtender = false;

                    optionsAnimations.extendMinorExtenders(sfxExtenderBlock, false, 200, 400);
                    optionsAnimations.extendMinorExtenders(sfxExtender, false, 400, 100);

                    optionsAnimations.changeAlphaSeekBars(sfxSeekBar,false,300,0);
                    optionsAnimations.changeAlphaTextViews(sfxText,false,300,0);
                }else {

                    showSfxExtender = true;

                    visibleMinorExtenders(false);
                    optionsAnimations.extendMinorExtenders(sfxExtenderBlock, true, 450, 0);
                    optionsAnimations.extendMinorExtenders(sfxExtender, true, 300, 300);

                    optionsAnimations.changeAlphaSeekBars(sfxSeekBar,true,300,150);
                    optionsAnimations.changeAlphaTextViews(sfxText,true,300,150);

                    if (showMusicExtender) {
                        showMinorExtender("music");
                    }
                }

                break;


            default:
                break;
        }
    }

    private boolean sfxOn;
    private boolean musicOn;

    // ovisno o progresu mijenjaj texturu botuna
    public void changeBtnImg(boolean changeSFX, int progress){
        // SFX
        if (changeSFX){
            if (progress == 0){
                sfxOn = false;
                btnSFX.setBackground(getResources().getDrawable(R.drawable.sfx_off));
            }else {
                if (!sfxOn){
                    sfxOn = true;
                    btnSFX.setBackground(getResources().getDrawable(R.drawable.sfx_on));
                }
            }
        }
        // MUSIKA
        else{
            if (progress == 0){
                musicOn = false;
                //System.out.println("DA");
                btnMusic.setBackground(getResources().getDrawable(R.drawable.music_off));
            }else {
                if (!musicOn){
                    musicOn = true;
                    btnMusic.setBackground(getResources().getDrawable(R.drawable.music_on));
                }
            }
        }
    }

    public void removeGUI(){
        optionsView.removeView(btnOptions_2);
        optionsView.removeView(btnClose);
        optionsView.removeView(mainExtender);
        optionsView.removeView(btnSFX);
        optionsView.removeView(btnMusic);
        optionsView.removeView(musicExtender);
        optionsView.removeView(musicExtenderBlock);
        optionsView.removeView(sfxExtender);
        optionsView.removeView(sfxExtenderBlock);
        optionsView.removeView(musicSeekBar);
        optionsView.removeView(musicText);
        optionsView.removeView(sfxSeekBar);
        optionsView.removeView(sfxText);
        optionsView.removeView(btnCloseMainExt);
        optionsView.removeView(btnCloseMusicExt);
        optionsView.removeView(btnCloseSfxExt);
        btnClose = null;
        btnOptions_2 = null;
        mainExtender = null;
        btnSFX = null;
        btnMusic = null;
        musicExtender = null;
        musicExtenderBlock = null;
        sfxExtender = null;
        sfxExtenderBlock = null;
        musicSeekBar = null;
        musicText = null;
        sfxSeekBar = null;
        sfxText = null;
        btnCloseMainExt = null;
        btnCloseMusicExt = null;
        btnCloseSfxExt = null;
    }

}
