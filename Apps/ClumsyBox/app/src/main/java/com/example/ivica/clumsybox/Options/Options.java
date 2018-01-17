package com.example.ivica.clumsybox.Options;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.MainMenu.MainMenu;

/**
 * Created by Ivica on 24.4.2017..
 */

public class Options extends View {

    private OptionsGUI optionsGUI;
    private RelativeLayout optionsView;

    public MainMenu mainMenu;

    private boolean enableTouch;
    private boolean optionsAlive;

    public Options(Context context) {
        super(context);

        optionsGUI = new OptionsGUI(context);

        enableTouch = true;
    }

    private void setListener(){
        optionsGUI.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOptionsView();
            }
        });

        optionsGUI.btnOptions_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOptionsView();
            }
        });

        optionsGUI.btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeExtender("music");
            }
        });

        optionsGUI.btnSFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeExtender("sfx");
            }
        });

        // SEEKBARS
        // music
        SeekBar.OnSeekBarChangeListener musicSeek = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Executed when progress is changed
                //System.out.println(progress);
                if (optionsGUI.showMusicExtender){
                    optionsGUI.musicText.setText("" + progress);
                    optionsGUI.changeBtnImg(false, progress);
                }
            }
        };
        optionsGUI.musicSeekBar.setOnSeekBarChangeListener(musicSeek);

        // sfx
        SeekBar.OnSeekBarChangeListener sfxSeek = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Executed when progress is changed
                //System.out.println(progress);

                if (optionsGUI.showSfxExtender){
                    optionsGUI.sfxText.setText("" + progress);
                    optionsGUI.changeBtnImg(true, progress);
                }
            }
        };
        optionsGUI.sfxSeekBar.setOnSeekBarChangeListener(sfxSeek);
    }

    private void closeExtender(String name){
        if (enableTouch){
            enableTouch = false;

            optionsGUI.showMinorExtender(name);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableTouch = true;
                }
            }, 800);
        }
    }

    public void closeOptionsView(){
        if (optionsAlive){
            optionsAlive = false;
            if (enableTouch){
                enableTouch = false;

                optionsGUI.startOptionsAnimation(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mainMenu.startOptionsView(false);
                        optionsGUI.removeGUI();

                    }
                }, 1100);
            }
        }
    }

    public void setRelativeLayout(RelativeLayout layout){
        this.optionsView = layout;
        optionsGUI.setRelativeLayout(optionsView);

        setListener();
        optionsAlive = true;
    }

}
