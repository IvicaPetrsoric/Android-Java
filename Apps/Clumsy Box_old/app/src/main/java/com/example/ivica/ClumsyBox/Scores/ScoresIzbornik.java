package com.example.ivica.ClumsyBox.Scores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.ivica.ClumsyBox.End_Lvl.EndLvl;
import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.MainMenu.Main_menu;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;

public class ScoresIzbornik extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    int odabraniJezik;
    Button backScoreIzbornik, localScores, onlineScores;
    boolean noviActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_izbornik);

        noviActivity = false;

        loadDataSharedPref();
        backScoreIzbornik = (Button)findViewById(R.id.buttonBackScoreLvl);
        localScores = (Button) findViewById(R.id.buttonLocalScore);
        onlineScores = (Button) findViewById(R.id.buttonOnlineScore);
        updateLanguagesOfUI();
   }

    public void updateLanguagesOfUI(){
        switch (odabraniJezik){

            case 0:
                backScoreIzbornik.setText(getString(R.string.back0));
                localScores.setText(getString(R.string.localScore0));
                onlineScores.setText(getString(R.string.onlineScore0));
                break;

            case 1:
                backScoreIzbornik.setText(getString(R.string.back1));
                localScores.setText(getString(R.string.localScore1));
                onlineScores.setText(getString(R.string.onlineScore1));
                break;

            case 2:
                backScoreIzbornik.setText(getString(R.string.back2));
                localScores.setText(getString(R.string.localScore2));
                onlineScores.setText(getString(R.string.onlineScore2));
                break;

            case 3:
                backScoreIzbornik.setText(getString(R.string.back3));
                localScores.setText(getString(R.string.localScore3));
                onlineScores.setText(getString(R.string.onlineScore3));
                break;

            case 4:
                backScoreIzbornik.setText(getString(R.string.back4));
                localScores.setText(getString(R.string.localScore4));
                onlineScores.setText(getString(R.string.onlineScore4));
                break;
        }
    }

    public void otvoriLocalScoreView(View view){
        //  otvori scoreView
        noviActivity = true;
        globalVar.set_localScoreActive(true);
        Intent intent = new Intent(this,ScoresLocalView.class);
        startActivity(intent);
    }

    public void otvoriOnlineScoreView(View view){
        //  otvori onlineView
        noviActivity = true;
        globalVar.set_localScoreActive(false);
        Intent intent = new Intent(this,ScoresOnlineView.class);
        startActivity(intent);
    }


    public void povratakEndLvl(View view){
        noviActivity = true;
        if (globalVar.get_izMainMenu()){
            Intent intent = new Intent(this,Main_menu.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,EndLvl.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        odabraniJezik = sharedPref.getInt("odabraniJezik",0);
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



