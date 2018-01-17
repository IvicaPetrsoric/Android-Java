package com.example.ivica.ClumsyBox.Scores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ivica.ClumsyBox.Scores.DB.CustomAdapter;
import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Scores.DB.MyDBHandler;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.Keyboard.ScoresSave;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ScoresLocalView extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    TextView  NazivIgraca, ostvarenRezultat;
    Button mRegButton, buttonPovratak;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    MyDBHandler dbHandler;
    int odabraniJezik;
    String imeIgraca, rezultatIgraca;
    boolean noviActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scores_local_view);

        noviActivity = false;

        buttonPovratak = (Button)findViewById(R.id.buttonPovratakLocalView);
        mRegButton = (Button)findViewById(R.id.buttonRegister);
        NazivIgraca = (TextView) findViewById(R.id.textNazivIgraca);
        ostvarenRezultat = (TextView) findViewById(R.id.rezultatIgraca);

        loadDataSharedPref();
        updateLanguagesOfUI();


        if (globalVar.get_SaveLocal() || globalVar.get_izMainMenu()){
            mRegButton.setVisibility(View.INVISIBLE);
        }

        if (globalVar.get_ImeSpremljenog() != null) {
            NazivIgraca.setText(" " +imeIgraca +" " + globalVar.get_ImeSpremljenog());
        }
        else{
            NazivIgraca.setText(" " +imeIgraca +" ");
        }

        ostvarenRezultat.setText(" " + rezultatIgraca+ " "  + globalVar.get_OstBodovi());

        dbHandler = new MyDBHandler(this, null);
        printDatabase();
    }

    public void updateLanguagesOfUI(){
        switch (odabraniJezik){

            case 0:
                buttonPovratak.setText(getString(R.string.back0));
                imeIgraca = (getString(R.string.playerName0));
                rezultatIgraca = (getString(R.string.playerScore0));
                mRegButton.setText(getString(R.string.register0));
                break;

            case 1:
                buttonPovratak.setText(getString(R.string.back1));
                imeIgraca = (getString(R.string.playerName1));
                rezultatIgraca = (getString(R.string.playerScore1));
                mRegButton.setText(getString(R.string.register1));
                break;

            case 2:
                buttonPovratak.setText(getString(R.string.back2));
                imeIgraca = (getString(R.string.playerName2));
                rezultatIgraca = (getString(R.string.playerScore2));
                mRegButton.setText(getString(R.string.register2));
                break;

            case 3:
                buttonPovratak.setText(getString(R.string.back3));
                imeIgraca = (getString(R.string.playerName3));
                rezultatIgraca = (getString(R.string.playerScore3));
                mRegButton.setText(getString(R.string.register3));
                break;

            case 4:
                buttonPovratak.setText(getString(R.string.back4));
                imeIgraca = (getString(R.string.playerName4));
                rezultatIgraca = (getString(R.string.playerScore4));
                mRegButton.setText(getString(R.string.register4));
                break;
        }
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        odabraniJezik = sharedPref.getInt("odabraniJezik",0);
    }

    public void importScore(View view){
        noviActivity = true;
        Intent intent = new Intent(this,ScoresSave.class);
        startActivity(intent);
    }

    public void povratakScoreIzbornik(View view){
        noviActivity = true;
        Intent intent = new Intent(this,ScoresIzbornik.class);
        startActivity(intent);
    }

    public void printDatabase(){
        GetPlayerListTask task = new GetPlayerListTask(this);
        task.execute((Void) null);
    }

    // priprema podataka iz DB
    public class GetPlayerListTask extends AsyncTask<Void, Void,ArrayList[]> {
        private final WeakReference<Activity> activityWeakRef;

        public GetPlayerListTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected ArrayList[] doInBackground(Void... arg0 ){
            return dbHandler.databaseToString();
        }

        @Override
        protected void onPostExecute(ArrayList[] dbString){
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                if(dbString[0].size() != 0){

                    ArrayList dbString_name;
                    ArrayList dbString_score;
                    ArrayList dbString_datePlayed;

                    dbString_name = dbString[0];
                    dbString_score = dbString[1];
                    dbString_datePlayed = dbString[2];

                    ListAdapter mojAdapter = new CustomAdapter(ScoresLocalView.this,dbString_name,dbString_score,dbString_datePlayed,false,globalVar.get_trenutnoVrijeme());
                    ListView mojListView = (ListView)findViewById(R.id.listView);
                    mojListView.setAdapter(mojAdapter);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        printDatabase();

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
