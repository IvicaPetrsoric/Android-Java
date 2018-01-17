package com.example.ivica.ClumsyBox.MainMenu;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;
import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Loading.Loading;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.ScoresIzbornik;

public class Main_menu extends Activity {

    public static final String TAG = " com.example.ivica.ClumsyBox.MainMenu";

    ImageButton muzika,SFXbutton, optionsButton, noAdsButton;
    Button Start, Worlds, Scores, buttonQuitGame;
    ImageButton language;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    String textIzlaza,textIzlazaPotvrda,textIzlazaNegacija, textNoAds, textBuyedAds, textMuzika, textSave , textCheckBox;
    int progressSFX, progressMusic, odabraniIgrac, odabraniJezik;
    boolean stanjeMuzike, stanjeSFXmuzike, noviActivity, omoguciPauzuMuzike, omoguciIntro;
    boolean stanjeCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        muzika = (ImageButton) findViewById(R.id.dugmeMuzike);
        SFXbutton = (ImageButton) findViewById(R.id.dugmeSFX);
        buttonQuitGame = (Button) findViewById(R.id.buttonQuit);
        Start = (Button) findViewById(R.id.buttonStart);
        Worlds = (Button) findViewById(R.id.buttonWorlds);
        Scores = (Button) findViewById(R.id.buttonScoreLvl);
        language = (ImageButton) findViewById(R.id.buttonTest);
        optionsButton = (ImageButton)findViewById(R.id.buttonOptions);
        noAdsButton = (ImageButton)findViewById(R.id.buttonNoAds);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsDialog();
            }
        });

        noAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noAdsDialog();
            }
        });

        buttonQuitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitGame();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog();
            }
        });

        SFXbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stanjeSFXmuzike = !stanjeSFXmuzike;
                kontrolaSFXmuzike();
            }
        });

        noviActivity = false;
        omoguciPauzuMuzike = true;
        globalVar.set_izMainMenu(true);
        globalVar.set_povrtakUmain(false);
        loadDataSharedPref();
        updateLanguagesOfUI();
    }

    public void pokreniGameLvl(View view){
        saveDataSharedPref();
        noviActivity = true;
        omoguciPauzuMuzike = false;
        Intent i = new Intent(this,Loading.class);
        startActivity(i);
    }

    public void pokreniScoreIzbornik(View view){
        saveDataSharedPref();
        omoguciPauzuMuzike = false;
        noviActivity = true;
        Intent i = new Intent(this,ScoresIzbornik.class);
        startActivity(i);
    }

    public void openWorlds(View view){
        omoguciPauzuMuzike = false;
        noviActivity = true;
        saveDataSharedPref();
        Intent i = new Intent(this, com.example.ivica.ClumsyBox.Worlds_lvl.Worlds.class);
        startActivity(i);
    }

    public void languageDialog(){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_languages);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        dialog.show();

        final TableRow tableRow1 = (TableRow)dialog.findViewById(R.id.tableRowPrvi);
        final TableRow tableRow2 = (TableRow)dialog.findViewById(R.id.tableDrugi);
        final TableRow tableRow3 = (TableRow)dialog.findViewById(R.id.tableTreci);
        final TableRow tableRow4 = (TableRow)dialog.findViewById(R.id.tableCetvrti);
        final TableRow tableRow5 = (TableRow)dialog.findViewById(R.id.tablePeti);

        if (odabraniJezik == 0){
            tableRow1.setBackgroundColor( Color.parseColor("#006699"));
        }
        else if (odabraniJezik == 1){
            tableRow2.setBackgroundColor( Color.parseColor("#006699"));
        }
        else if (odabraniJezik == 2){
            tableRow3.setBackgroundColor( Color.parseColor("#006699"));
        }
        else if (odabraniJezik == 3){
            tableRow4.setBackgroundColor( Color.parseColor("#006699"));
        }
        else
        {
            tableRow5.setBackgroundColor( Color.parseColor("#006699"));
        }

        final Button buttonEnglish  = (Button)dialog.findViewById(R.id.buttonEnglish);
        final Button buttonCroatian = (Button)dialog.findViewById(R.id.buttonCroatian);
        final Button buttonGerman  = (Button)dialog.findViewById(R.id.buttonGerman);
        final Button buttonItaliano  = (Button)dialog.findViewById(R.id.buttonItaliano);
        final Button buttonChinese  = (Button)dialog.findViewById(R.id.buttonChinese);

        buttonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniJezik = 0;
                updateLanguagesOfUI();
                dialog.cancel();
            }
        });

        buttonCroatian.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                odabraniJezik = 1;
                updateLanguagesOfUI();
                dialog.cancel();
           }
        });

        buttonGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniJezik = 2;
                updateLanguagesOfUI();
                dialog.cancel();
            }
        });

        buttonItaliano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniJezik = 3;
                updateLanguagesOfUI();
                dialog.cancel();
            }
        });

        buttonChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniJezik = 4;
                updateLanguagesOfUI();
                dialog.cancel();
            }
        });
    }

    public void optionsDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_options);
        dialog.setCancelable(false);
        dialog.show();

        stanjeCB = omoguciIntro;

        final SeekBar mySeekbarSFX = (SeekBar)dialog.findViewById(R.id.seekBarSFX);
        final TextView myTehxViewSFX = (TextView)dialog.findViewById(R.id.seekTextSFX);
        final SeekBar mySeekbarMusic = (SeekBar)dialog.findViewById(R.id.seekBarMusic);
        final TextView myTehxViewMusic = (TextView)dialog.findViewById(R.id.seekTextMusic);
        final ImageButton myCloseButton = (ImageButton)dialog.findViewById(R.id.buttonCloseOptions);
        final Button mySaveButton = (Button)dialog.findViewById(R.id.buttonSaveOptions);
        TextView postavkeMuzike = (TextView)dialog.findViewById(R.id.postavkeMuzika);
        TextView postavkeSfxMuzike = (TextView)dialog.findViewById(R.id.postavkeSfxMuzika);
        final TextView textCBox = (TextView)dialog.findViewById(R.id.textCheckBox);
        final CheckBox myCheckBox = (CheckBox)dialog.findViewById(R.id.checkBox);

        textCBox.setText(textCheckBox);
        myCheckBox.setChecked(stanjeCB);

        myCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myCheckBox.isChecked()){
                    stanjeCB = true;
                }
                else {
                    stanjeCB = false;
                }
            }
        });

        postavkeMuzike.setText(textMuzika);
        postavkeSfxMuzike.setText("SFX " +textMuzika);
        mySaveButton.setText(textSave);

        myCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //  SAVE
        mySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                omoguciIntro = stanjeCB;
                SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("seekBarSFX",progressSFX);
                editor.putInt("seekBarMusic",progressMusic);
                editor.putBoolean("introMuzika",omoguciIntro);
                editor.apply();

                Intent service = new Intent(Main_menu.this, BackgroundSoundService.class);
                String stanjeMuzikeInt = Float.toString(progressMusic); // pauza muzike
                service.putExtra("stanjeMuzike", stanjeMuzikeInt);
                startService(service);

                dialog.cancel();
            }
        });

        mySeekbarSFX.setProgress(progressSFX);
        mySeekbarMusic.setProgress(progressMusic);

        myTehxViewSFX.setText(progressSFX + " / " + mySeekbarSFX.getMax());
        myTehxViewMusic.setText(progressMusic + " / " + mySeekbarMusic.getMax());

        //  SFX
        mySeekbarSFX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressSFX = progress;
                myTehxViewSFX.setText(progressSFX + " / " + mySeekbarSFX.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myTehxViewSFX.setText(progressSFX + " / " + mySeekbarSFX.getMax());
            }
        });

        //  MUSIC
        mySeekbarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressMusic = progress;
                myTehxViewMusic.setText(progressMusic + " / " + mySeekbarMusic.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myTehxViewMusic.setText(progressMusic + " / " + mySeekbarMusic.getMax());
            }
        });
    }

    public void noAdsDialog(){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog_kupnje);
        dialog.setCancelable(true);

        dialog.show();

        final TextView textKupovine = (TextView)dialog.findViewById(R.id.textKupnjeSvijeta);
        ImageView  zlatnici = (ImageView)dialog.findViewById(R.id.dialogZlatnici);
        zlatnici.setVisibility(View.INVISIBLE);

        Button subButton = (Button)dialog.findViewById(R.id.button2);
        Button cancelButton = (Button)dialog.findViewById(R.id.button);

        if(globalVar.get_noAds()){
            textKupovine.setText(textBuyedAds);
            subButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
        }
        else{
            textKupovine.setText(textNoAds);
            subButton.setText(textIzlazaPotvrda);
            cancelButton.setText(textIzlazaNegacija);
        }

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalVar.set_noAds(true);
                dialog.cancel();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void quitGame(){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog_kupnje);
        dialog.setCancelable(false);

        dialog.show();

        final TextView textKupovine = (TextView)dialog.findViewById(R.id.textKupnjeSvijeta);
        ImageView  zlatnici = (ImageView)dialog.findViewById(R.id.dialogZlatnici);
        zlatnici.setVisibility(View.INVISIBLE);

        textKupovine.setText(textIzlaza);

        Button subButton = (Button)dialog.findViewById(R.id.button2);
        Button cancelButton = (Button)dialog.findViewById(R.id.button);

        subButton.setText(textIzlazaPotvrda);
        cancelButton.setText(textIzlazaNegacija);

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataSharedPref();

                finishAffinity();
                dialog.cancel();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void updateLanguagesOfUI(){
        switch (odabraniJezik){

            case 0:
                noAdsButton.setImageResource(R.drawable.ads_0);
                globalVar.set_textReklama(getString(R.string.textReklama0));
                globalVar.set_textNewLocalScore(getString(R.string.textNewLocalScore0));
                Start.setText(getString(R.string.start0));
                Worlds.setText(getString(R.string.worlds0));
                Scores.setText(getString(R.string.score0));
                buttonQuitGame.setText(getString(R.string.quit0));
                textIzlaza = getString(R.string.textQuit0);
                textIzlazaPotvrda = getString(R.string.textPozitive0);
                textIzlazaNegacija = getString(R.string.textNegative0);
                textNoAds = getString(R.string.buyNoAds0);
                textBuyedAds = getString(R.string.iskljuceneReklame0);
                textMuzika = getString(R.string.music0);
                textSave = getString(R.string.save0);
                textCheckBox = getString(R.string.textCheckBox0);
                break;

            case 1:
                noAdsButton.setImageResource(R.drawable.ads_1);
                globalVar.set_textReklama(getString(R.string.textReklama1));
                globalVar.set_textNewLocalScore(getString(R.string.textNewLocalScore1));
                Start.setText(getString(R.string.start1));
                Worlds.setText(getString(R.string.worlds1));
                Scores.setText(getString(R.string.score1));
                buttonQuitGame.setText(getString(R.string.quit1));
                textIzlaza = getString(R.string.textQuit1);
                textIzlazaPotvrda = getString(R.string.textPozitive1);
                textIzlazaNegacija = getString(R.string.textNegative1);
                textNoAds = getString(R.string.buyNoAds1);
                textBuyedAds = getString(R.string.iskljuceneReklame1);
                textMuzika = getString(R.string.music1);
                textSave = getString(R.string.save1);
                textCheckBox = getString(R.string.textCheckBox1);
                break;

            case 2:
                noAdsButton.setImageResource(R.drawable.ads_2);
                globalVar.set_textReklama(getString(R.string.textReklama2));
                globalVar.set_textNewLocalScore(getString(R.string.textNewLocalScore2));
                Start.setText(getString(R.string.start2));
                Worlds.setText(getString(R.string.worlds2));
                Scores.setText(getString(R.string.score2));
                buttonQuitGame.setText(getString(R.string.quit2));
                textIzlaza = getString(R.string.textQuit2);
                textIzlazaPotvrda = getString(R.string.textPozitive2);
                textIzlazaNegacija = getString(R.string.textNegative2);
                textNoAds = getString(R.string.buyNoAds2);
                textBuyedAds = getString(R.string.iskljuceneReklame2);
                textMuzika = getString(R.string.music2);
                textSave = getString(R.string.save2);
                textCheckBox = getString(R.string.textCheckBox2);
                break;

            case 3:
                noAdsButton.setImageResource(R.drawable.ads_3);
                globalVar.set_textReklama(getString(R.string.textReklama3));
                globalVar.set_textNewLocalScore(getString(R.string.textNewLocalScore3));
                Start.setText(getString(R.string.start3));
                Worlds.setText(getString(R.string.worlds3));
                Scores.setText(getString(R.string.score3));
                buttonQuitGame.setText(getString(R.string.quit3));
                textIzlaza = getString(R.string.textQuit3);
                textIzlazaPotvrda = getString(R.string.textPozitive3);
                textIzlazaNegacija = getString(R.string.textNegative3);
                textNoAds = getString(R.string.buyNoAds3);
                textBuyedAds = getString(R.string.iskljuceneReklame3);
                textMuzika = getString(R.string.music3);
                textSave = getString(R.string.save3);
                textCheckBox = getString(R.string.textCheckBox3);
                break;

            case 4:
                noAdsButton.setImageResource(R.drawable.ads_4);
                globalVar.set_textReklama(getString(R.string.textReklama4));
                globalVar.set_textNewLocalScore(getString(R.string.textNewLocalScore4));
                Start.setText(getString(R.string.start4));
                Worlds.setText(getString(R.string.worlds4));
                Scores.setText(getString(R.string.score4));
                buttonQuitGame.setText(getString(R.string.quit4));
                textIzlaza = getString(R.string.textQuit4);
                textIzlazaPotvrda = getString(R.string.textPozitive4);
                textIzlazaNegacija = getString(R.string.textNegative4);
                textNoAds = getString(R.string.buyNoAds4);
                textBuyedAds = getString(R.string.iskljuceneReklame4);
                textMuzika = getString(R.string.music4);
                textSave = getString(R.string.save4);
                textCheckBox = getString(R.string.textCheckBox4);
                break;
        }
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
        else
        {
            muzika.setImageResource(R.drawable.muzika_off);
            pauzirajMuziku();
        }
    }

    public void saveDataSharedPref(){

        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        // spremanje
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("stanjeMuzike",stanjeMuzike);
        editor.putBoolean("stanjeSFXmuzike",stanjeSFXmuzike);
        editor.putInt("odabraniJezik",odabraniJezik);
        //editor.putBoolean("kupljenIgracPopcorn_1",false);
        editor.apply();
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);

        boolean stanjeMuzikeSP = sharedPref.getBoolean("stanjeMuzike",true);
        stanjeSFXmuzike = sharedPref.getBoolean("stanjeSFXmuzike",true);
        omoguciIntro = sharedPref.getBoolean("introMuzika",false);

        progressSFX = sharedPref.getInt("seekBarSFX",100);
        progressMusic = sharedPref.getInt("seekBarMusic",100);

        odabraniJezik = sharedPref.getInt("odabraniJezik",0);

        odabraniIgrac = sharedPref.getInt("biranIgrac",0);
        globalVar.set_odabraniIgrac(odabraniIgrac);

        stanjeMuzike = stanjeMuzikeSP;

        globalVar.set_stanjeMuzike(stanjeMuzike);

        // muzika, pjesma
        if(stanjeMuzikeSP){
            pokrenijMuziku();
            muzika.setImageResource(R.drawable.muzika_onn);
        }
        else
        {
            muzika.setImageResource(R.drawable.muzika_off);
            pauzirajMuziku();
        }
        // SFX muzika
        globalVar.set_stanjeSFXmuzike(stanjeSFXmuzike);
        if(stanjeSFXmuzike)
        {
            SFXbutton.setImageResource(R.drawable.sfx_on);
        }
        else {
            SFXbutton.setImageResource(R.drawable.sfx_off);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        pauzirajMuziku();
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
        Intent service = new Intent(Main_menu.this, BackgroundSoundService.class);
        Main_menu.this.stopService(service);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            quitGame();
            //return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
