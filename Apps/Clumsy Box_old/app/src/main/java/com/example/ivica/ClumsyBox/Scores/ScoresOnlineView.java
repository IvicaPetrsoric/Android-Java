package com.example.ivica.ClumsyBox.Scores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.Keyboard.ScoresSave;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;
import com.example.ivica.ClumsyBox.Scores.Server.SpremanjeNaServer;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ScoresOnlineView extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    TextView  NazivIgraca, ostvarenRezultat;
    Button mRegButton,buttonPovratak, buttonWeb;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    String playerName, playerScore, textTop50Players, textNoWebConn, textLoading;
    int odabraniJezik;
    boolean noviActivity;
    SpremanjeNaServer myServer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_online_view);

        noviActivity = false;

        mRegButton = (Button)findViewById(R.id.buttonRegister);
        NazivIgraca = (TextView) findViewById(R.id.textNazivIgraca);
        ostvarenRezultat = (TextView) findViewById(R.id.rezultatIgraca);
        buttonPovratak = (Button)findViewById(R.id.buttonPovratakOnlineView);
        buttonWeb = (Button)findViewById(R.id.openWebButton);
        buttonWeb.setVisibility(View.INVISIBLE);

        /*buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://sanjin.eu/plexer/rma_projekt/Web.php"));
                startActivity(intent);
            }
        });*/

        myServer = new SpremanjeNaServer();

        loadDataSharedPref();
        updateLanguagesOfUI();
        printPlayersOnline();

        if (globalVar.get_SaveWeb() || globalVar.get_izMainMenu()){
            mRegButton.setVisibility(View.INVISIBLE);
        }

        if (globalVar.get_ImeSpremljenog() != null) {
            NazivIgraca.setText(" "+ playerName +" " + globalVar.get_ImeSpremljenog());
        }
        else{
            NazivIgraca.setText(" "+ playerName +" " );
        }

        ostvarenRezultat.setText(" " + playerScore +  " " + globalVar.get_OstBodovi());
    }

    public void povratakScoreIzbornik(View view){
        noviActivity = true;
        Intent intent = new Intent(this,ScoresIzbornik.class);
        startActivity(intent);
    }

    public void importScore(View view){
        noviActivity = true;
        Intent intent = new Intent(this,ScoresSave.class);
        startActivity(intent);
    }

    public void printPlayersOnline(){
        GetOnlinePlayerTask task = new GetOnlinePlayerTask(this);
        task.execute();
    }

    // PRIPREMA PODATAKA SA SERVERA
    public class GetOnlinePlayerTask extends AsyncTask<Void, Void,ArrayList[]> {
        private final WeakReference<Activity> activityWeakRef;

        TextView textTop50 = (TextView)findViewById(R.id.top50Server);
        boolean noWEB;

        public GetOnlinePlayerTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
            textTop50.setText(textLoading);
            noWEB = false;
        }

        @Override
        protected ArrayList[] doInBackground(Void... arg0 ){

            ArrayList[] playersOnServer  = new ArrayList[0];
            try {
                playersOnServer = myServer.refresh();
                noWEB = false;
               // myServer.refresh();
            } catch (IOException e) {
                e.printStackTrace();
                noWEB = true;
                System.out.println("Problemi 1");
            } catch (JSONException e) {
                e.printStackTrace();
                noWEB = true;
                System.out.println("Problemi 2");
            }
            return playersOnServer;
        }

        @Override
        protected void onPostExecute(ArrayList[] playersOnServer){
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                if(playersOnServer.length != 0 && !noWEB){

                    textTop50.setText(textTop50Players);

                    ArrayList name_Server;
                    ArrayList score_Server;
                    ArrayList date_Server;

                    name_Server = playersOnServer[0];
                    score_Server = playersOnServer[1];
                    date_Server = playersOnServer[2];

                    ListAdapter mojAdapter = new CustomAdapter(ScoresOnlineView.this,name_Server,score_Server,date_Server,true,null);
                    ListView mojListView = (ListView)findViewById(R.id.listView);
                    mojListView.setAdapter(mojAdapter);
                }
                else {
                    textTop50.setText(textNoWebConn);
                    mRegButton.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void updateLanguagesOfUI(){

        switch (odabraniJezik){

            case 0:
                buttonPovratak.setText(getString(R.string.back0));
                playerName = (getString(R.string.playerName0));
                playerScore = (getString(R.string.playerScore0));
                mRegButton.setText(getString(R.string.register0));
                textTop50Players  = (getString(R.string.top0));
                textNoWebConn  = (getString(R.string.noWebConn0));
                textLoading = getString(R.string.loading0);
                break;

            case 1:
                buttonPovratak.setText(getString(R.string.back1));
                playerName = (getString(R.string.playerName1));
                playerScore = (getString(R.string.playerScore1));
                mRegButton.setText(getString(R.string.register1));
                textTop50Players  = (getString(R.string.top1));
                textNoWebConn  = (getString(R.string.noWebConn1));
                textLoading = getString(R.string.loading1);
                break;

            case 2:
                buttonPovratak.setText(getString(R.string.back2));
                playerName = (getString(R.string.playerName2));
                playerScore = (getString(R.string.playerScore2));
                mRegButton.setText(getString(R.string.register2));
                textTop50Players  = (getString(R.string.top2));
                textNoWebConn  = (getString(R.string.noWebConn2));
                textLoading = getString(R.string.loading2);
                break;

            case 3:
                buttonPovratak.setText(getString(R.string.back3));
                playerName = (getString(R.string.playerName3));
                playerScore = (getString(R.string.playerScore3));
                mRegButton.setText(getString(R.string.register3));
                textTop50Players  = (getString(R.string.top3));
                textNoWebConn  = (getString(R.string.noWebConn3));
                textLoading = getString(R.string.loading3);
                break;

            case 4:
                buttonPovratak.setText(getString(R.string.back4));
                playerName = (getString(R.string.playerName4));
                playerScore = (getString(R.string.playerScore4));
                mRegButton.setText(getString(R.string.register4));
                textTop50Players  = (getString(R.string.top4));
                textNoWebConn  = (getString(R.string.noWebConn4));
                textLoading = getString(R.string.loading4);
                break;
        }
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        odabraniJezik = sharedPref.getInt("odabraniJezik",0);
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
