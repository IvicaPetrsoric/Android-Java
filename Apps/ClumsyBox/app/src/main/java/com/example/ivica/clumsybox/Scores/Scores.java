package com.example.ivica.clumsybox.Scores;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.MainMenu.MainMenu;
import com.example.ivica.clumsybox.R;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Scores extends Activity {

    private ImageView bottomLocal;

    private ScoresAnimations scoresAnimations;
    private Button btnLocal, btnGlobal;
    private TextView playerPoz, totalPlayers;

    private ProgressBar progressBar;

    private int startX, endX;

    boolean localShow = true;

    private Server myServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        bottomLocal = (ImageView)findViewById(R.id.bottomLocal);
        btnLocal = (Button)findViewById(R.id.btnLocal);
        btnGlobal = (Button)findViewById(R.id.btnGlobal);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        playerPoz = (TextView)findViewById(R.id.myPostion);
        totalPlayers = (TextView)findViewById(R.id.myPostion);

        scoresAnimations = new ScoresAnimations();

        //scoresAnimations.translateActivity(this, 0, 1000, true);

        myServer = new Server();

        // dohvacanje igrača sa servera
        //getPlayersScores();

        prepareScoreList(true);

        String locale = getResources().getConfiguration().locale.getCountry();
        System.out.println("Zemlja: " + locale);

        //overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        startX = bottomLocal.getLeft();
        endX = startX + btnLocal.getWidth();

        bottomLocal.getLayoutParams().width = btnLocal.getWidth();
        bottomLocal.setX( btnLocal.getX());
    }

    public void closeScoreView(View view){
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("destination", "static");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.scores_slide_out);
        finish();
    }

    public void toggleScores(View view){
        if (view == findViewById(R.id.btnLocal)){
            System.out.println("LOCAL");

            if (!localShow){
                scoresAnimations.translateBottomBtns(bottomLocal, endX, startX,  500);
                localShow = true;

                prepareScoreList(true);

                progressBar.setVisibility(View.INVISIBLE);
            }
        }else{
            System.out.println("GLOBAL");

            if (localShow){
                scoresAnimations.translateBottomBtns(bottomLocal, startX, endX, 500);
                localShow = false;

                prepareScoreList(false);

                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getPlayersScores(){
        getOnlinePlayerTask task = new getOnlinePlayerTask(this);
        task.execute();
    }

    // PRIPREMA PODATAKA SA SERVERA
    public class getOnlinePlayerTask extends AsyncTask<Void, Void,ArrayList[]> {
        private final WeakReference<Activity> activityWeakRef;

        //TextView textTop50 = (TextView)findViewById(R.id.top50Server);
        boolean noWEB;

        public getOnlinePlayerTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
            //textTop50.setText(textLoading);
            noWEB = false;
        }

        @Override
        protected ArrayList[] doInBackground(Void... arg0 ){

            ArrayList[] playersOnServer  = new ArrayList[0];
            try {
                myServer.getScores();
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

                    //textTop50.setText(textTop50Players);

                    ArrayList name_Server;
                    ArrayList score_Server;
                    ArrayList date_Server;

                    name_Server = playersOnServer[0];
                    score_Server = playersOnServer[1];
                    date_Server = playersOnServer[2];

                }
                else {
                    //textTop50.setText(textNoWebConn);
                    //mRegButton.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void prepareScoreList(boolean showLocal){
       // System.out.println("PATKA");

        // prikazi lokalne rezultate
        if (showLocal){

            ListAdapter mojAdapter = new ScoresListAdapter(Scores.this,
                    myServer.local_playerPosition, myServer.local_playerFlag,
                    myServer.local_playerName, myServer.local_playerScore,
                    showLocal);

            ListView mojListView = (ListView)findViewById(R.id.scoresList);
            //mojListView.setEnabled(false);
            mojListView.setAdapter(mojAdapter);

            playerPoz.setText("You: #" + myServer.local_playerPos);
            totalPlayers.setText("Total: " + myServer.local_totalPlayers);
        }else{

            ListAdapter mojAdapter = new ScoresListAdapter(Scores.this,
                    myServer.global_playerPosition, myServer.global_playerFlag,
                    myServer.global_playerName, myServer.global_playerScore,
                    showLocal);

            ListView mojListView = (ListView)findViewById(R.id.scoresList);

            mojListView.setAdapter(mojAdapter);

            playerPoz.setText("You: #" + myServer.global_playerPos);
            totalPlayers.setText("Total: " + myServer.global_totalPlayers);

        }
    }


}
