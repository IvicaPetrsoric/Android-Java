package com.example.ivica.ClumsyBox.Worlds_lvl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;

public class Worlds_players extends Activity {

    //public static final String TAG = "com.example.ivica.smartclicker";

    Button backButton,buttonSavePlayer;
    ImageView spremljenIgracSlika;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    boolean noviActivity, prviPrikazListe, kupljenPopcorn_1;
    int odabraniIgrac,odabraniJezik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worlds_players);

        backButton = (Button)findViewById(R.id.buttonBackWorlds);
        buttonSavePlayer = (Button)findViewById(R.id.buttonSavePlayer);
        spremljenIgracSlika = (ImageView)findViewById(R.id.spremljenIgracSP);

        prviPrikazListe = false;
        noviActivity = false;
        loadDataSharedPref();
        updateLanguagesOfUI();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noviActivity = true;
                Intent i = new Intent(Worlds_players.this,Worlds.class);
                startActivity(i);
            }
        });


        buttonSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataSharedPref();
                noviActivity = true;
                Intent i = new Intent(Worlds_players.this,Worlds.class);
                startActivity(i);
            }
        });

        prikazSpremljenogIgraca(odabraniIgrac);

        String[] brojIgrca = {"crvena","plava","zelena","kokica_1","kokica_2","1","2","3","4"};
        ListAdapter mojAdapter = new Worlds_players_adapter(this,brojIgrca,kupljenPopcorn_1);

        final ListView mojaListaIgraca = (ListView)findViewById(R.id.listViewIgraca);
        mojaListaIgraca.setAdapter(mojAdapter);

        mojaListaIgraca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                prviPrikazListe = true;

                if (position >2 && !kupljenPopcorn_1){
                    buttonSavePlayer.setVisibility(View.INVISIBLE);
                    //Toast.makeText(Worlds_players.this,"LOCKED!",Toast.LENGTH_SHORT).show();
                }
                else if (position >3 && kupljenPopcorn_1){
                    buttonSavePlayer.setVisibility(View.INVISIBLE);
                    //Toast.makeText(Worlds_players.this,"LOCKED!",Toast.LENGTH_SHORT).show();
                }
                else{
                    buttonSavePlayer.setVisibility(View.VISIBLE);
                    odabraniIgrac = position;
                   // Toast.makeText(Worlds_players.this,"igrac: " + odabraniIgrac,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateLanguagesOfUI(){
        switch (odabraniJezik){

            case 0:
                backButton.setText(getString(R.string.back0));
                buttonSavePlayer.setText(getString(R.string.save0));
                break;

            case 1:
                backButton.setText(getString(R.string.back1));
                buttonSavePlayer.setText(getString(R.string.save1));
                break;

            case 2:
                backButton.setText(getString(R.string.back2));
                buttonSavePlayer.setText(getString(R.string.save2));
                break;

            case 3:
                backButton.setText(getString(R.string.back3));
                buttonSavePlayer.setText(getString(R.string.save3));
                break;

            case 4:
                backButton.setText(getString(R.string.back4));
                buttonSavePlayer.setText(getString(R.string.save4));
                break;
        }
    }

    public void prikazSpremljenogIgraca(int spremljenIgrac){
        switch (spremljenIgrac){

            case 0:
                spremljenIgracSlika.setImageResource(R.drawable.kutija_igrac_c);
                break;

            case 1:
                spremljenIgracSlika.setImageResource(R.drawable.kutija_igrac_p);
                break;

            case 2:
                spremljenIgracSlika.setImageResource(R.drawable.kutija_igrac_z);
                break;

            case 3:
                spremljenIgracSlika.setImageResource(R.drawable.kokica_1);
                break;

            case 4:
                spremljenIgracSlika.setImageResource(R.drawable.kokica_2);
                break;
        }
    }

    public void saveDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("biranIgrac",odabraniIgrac);
        editor.apply();
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        odabraniIgrac = sharedPref.getInt("biranIgrac",0);
        odabraniJezik = sharedPref.getInt("odabraniJezik",0);
        kupljenPopcorn_1 = sharedPref.getBoolean("kupljenIgracPopcorn_1",false);
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
    public void onStop(){
        super.onStop();
        if (noviActivity){
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (globalVar.get_stanjeMuzike() && (!noviActivity)){
            pauzirajService();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
