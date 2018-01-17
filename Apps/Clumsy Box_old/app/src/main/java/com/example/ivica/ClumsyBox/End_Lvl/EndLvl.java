package com.example.ivica.ClumsyBox.End_Lvl;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Loading.Loading;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.ScoresIzbornik;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;

public class EndLvl extends Activity {

    //public static final String TAG = "com.example.ivica.smartclicker";

    ImageButton muzika,SFXbutton;
    TextView  ukupnoBodova, stanjeSkrinje;
    Button buttonRestart,buttonScores,buttonMenu;
    int  brojZlatnika, ObadranJezik;
    boolean stanjeMuzike,stanjeSFXmuzike, noviActivity;
    String jezikUkupnihBodova;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endlvl);

        noviActivity = false;

        ukupnoBodova = (TextView)findViewById(R.id.ukupnoBodova);
        stanjeSkrinje = (TextView)findViewById(R.id.brojZlatnikaSkrinje);
        muzika = (ImageButton)findViewById(R.id.dugmeMuzike);
        buttonRestart = (Button)findViewById(R.id.buttonRestart);
        buttonScores = (Button)findViewById(R.id.buttonScoreLvl);
        buttonMenu =(Button)findViewById(R.id.buttonMainMenu);
        SFXbutton = (ImageButton) findViewById(R.id.dugmeSFX);

        SFXbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stanjeSFXmuzike = !stanjeSFXmuzike;
                kontrolaSFXmuzike();
            }
        });

        globalVar.set_izMainMenu(false);

        loadDataSharedPref();
        updateLanguagesOfUI();

        brojZlatnika = globalVar.get_OstBodovi();
        int ukpno = (brojZlatnika);
        ukupnoBodova.setText(jezikUkupnihBodova+": "+ukpno);
    }

    public void updateLanguagesOfUI(){
        switch (ObadranJezik){

            case 0:
                jezikUkupnihBodova = getString(R.string.totalScore0);
                buttonRestart.setText(getString(R.string.restart0));
                buttonScores.setText(getString(R.string.score0));
                buttonMenu.setText(getString(R.string.mainMenu0));
                break;

            case 1:
                jezikUkupnihBodova = getString(R.string.totalScore1);
                buttonRestart.setText(getString(R.string.restart1));
                buttonScores.setText(getString(R.string.score1));
                buttonMenu.setText(getString(R.string.mainMenu1));
                break;

            case 2:
                jezikUkupnihBodova = getString(R.string.totalScore2);
                buttonRestart.setText(getString(R.string.restart2));
                buttonScores.setText(getString(R.string.score2));
                buttonMenu.setText(getString(R.string.mainMenu2));
                break;

            case 3:
                jezikUkupnihBodova = getString(R.string.totalScore3);
                buttonRestart.setText(getString(R.string.restart3));
                buttonScores.setText(getString(R.string.score3));
                buttonMenu.setText(getString(R.string.mainMenu3));
                break;

            case 4:
                jezikUkupnihBodova = getString(R.string.totalScore4);
                buttonRestart.setText(getString(R.string.restart4));
                buttonScores.setText(getString(R.string.score4));
                buttonMenu.setText(getString(R.string.mainMenu4));
                break;
        }
    }

    public void pokreniGameLvl(View view){
        noviActivity = true;
        saveDataSharedPref();
        Intent gameLvl = new Intent(this,Loading.class);
        startActivity(gameLvl);
    }

    public void pokreniScoreLvl(View view){
        noviActivity = true;
        saveDataSharedPref();
        Intent gameLvl = new Intent(this,ScoresIzbornik.class);
        startActivity(gameLvl);
    }

    public void pokreniMainMenu(View view){
        noviActivity = true;
        saveDataSharedPref();
        globalVar.set_povrtakUmain(true);
        Intent gameLvl = new Intent(this,Loading.class);
        startActivity(gameLvl);
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        int ukupnoZlatnika = sharedPref.getInt("ukupnoCoina",0);
        boolean stanjeMuzikeSP = sharedPref.getBoolean("stanjeMuzike",false);
        stanjeSFXmuzike = sharedPref.getBoolean("stanjeSFXmuzike",true);
        ObadranJezik = sharedPref.getInt("odabraniJezik",0);
        stanjeSkrinje.setText(""+ukupnoZlatnika);
        stanjeMuzike = stanjeMuzikeSP;

        //  muzika, pjesme
        if(stanjeMuzikeSP){
            muzika.setImageResource(R.drawable.muzika_onn);
        }
        else
        {
            muzika.setImageResource(R.drawable.muzika_off);
        }
        //  SFX muzika
        if(stanjeSFXmuzike)
        {
            SFXbutton.setImageResource(R.drawable.sfx_on);
        }
        else {
            SFXbutton.setImageResource(R.drawable.sfx_off);
        }
    }

    public void saveDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("stanjeSFXmuzike",stanjeSFXmuzike);
        editor.putBoolean("stanjeMuzike",stanjeMuzike);
        editor.apply();
    }

    public void kontrolaSFXmuzike(){
        globalVar.set_stanjeSFXmuzike(stanjeSFXmuzike);

        if(stanjeSFXmuzike)
        {
            SFXbutton.setImageResource(R.drawable.sfx_on);
        }
        else {
            SFXbutton.setImageResource(R.drawable.sfx_off);
        }
    }

    public void kontrolaMuzike(View view){
        stanjeMuzike = !stanjeMuzike;
        globalVar.set_stanjeMuzike(stanjeMuzike);

        if(stanjeMuzike){
            pokrenijMuziku();
            muzika.setImageResource(R.drawable.muzika_onn);
        }
        else{
            pauzirajMuziku();
            muzika.setImageResource(R.drawable.muzika_off);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    public void pokrenijMuziku(){
        String stanjeMuzikeInt;
        Intent service = new Intent(this, BackgroundSoundService.class);
        if (stanjeMuzike){
            stanjeMuzikeInt = "1";
        }
        else{
            stanjeMuzikeInt = "0";
        }
        service.putExtra("stanjeMuzike", stanjeMuzikeInt);
        startService(service);
    }

    public void pauzirajMuziku (){
        Intent service = new Intent(EndLvl.this, BackgroundSoundService.class);
        EndLvl.this.stopService(service);
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
