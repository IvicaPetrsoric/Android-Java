package com.example.ivica.ClumsyBox.Loading;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Game_Lvl.MainActivity;
import com.example.ivica.ClumsyBox.MainMenu.Main_menu;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;

public class Loading extends Activity {

    //public static final String TAG = "com.example.ivica.smartclicker";

    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    boolean zaMain, noviActivity;
    ImageView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        noviActivity = false;

        loading = (ImageView)findViewById(R.id.loadingSlika);
        zaMain = globalVar.get_povratakMain();
        pokreniAnimaciju();
    }

    public void pokreniAnimaciju(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.kruzno_rotiranje);
        loading.startAnimation(animation);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                long futureTime = System.currentTimeMillis()+3100;
                while(System.currentTimeMillis() <futureTime) {
                    synchronized (this) {
                        try {
                            wait(futureTime - System.currentTimeMillis());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                pokretanjeLvl();
            }
        };

        Thread mojThread = new Thread(r);
        mojThread.start();
    }

    public void pokretanjeLvl(){
        noviActivity = true;

        if (zaMain ){
            Intent pokreniMain = new Intent(this,Main_menu.class);
            startActivity(pokreniMain);
        }
        else{
            Intent pokreniMain = new Intent(this,MainActivity.class);
            startActivity(pokreniMain);
        }
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
