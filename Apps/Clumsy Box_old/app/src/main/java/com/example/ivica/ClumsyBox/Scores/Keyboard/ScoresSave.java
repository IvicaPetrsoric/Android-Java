package com.example.ivica.ClumsyBox.Scores.Keyboard;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.Scores.DB.MyDBHandler;
import com.example.ivica.ClumsyBox.Scores.DB.Players;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Scores.ScoresLocalView;
import com.example.ivica.ClumsyBox.Scores.ScoresOnlineView;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;
import com.example.ivica.ClumsyBox.Scores.Server.SpremanjeNaServer;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ScoresSave extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    TextView outTextView,  ostvarenRezultat, playerName, playerScore;
    ArrayList<Button> buttonList = new ArrayList<>();
    ArrayList<String> privremenaSlova = new ArrayList<>();
    String[] znakovi = {"%","_","!","@","#","/","^","&","(",")",".","-","'","\"",":",";",",","?","~","<",">","{","}","[","]","°"};
    boolean LOWECASE, znakovnaTipkovnica, noviActivity;
    Button velicinaSlova, buttonZnakovi, povratakSave, buttonSave;
    int brojac, ObadranJezik;
    String formattedDate, textPotrebnoIme;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    MyDBHandler dbHandler;
    SpremanjeNaServer mojServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.score_save);

        dbHandler = new MyDBHandler(this, null);

        LOWECASE = true;
        znakovnaTipkovnica = false;
        noviActivity = false;
        mojServer = new SpremanjeNaServer();

        playerName = (TextView)findViewById(R.id.textName);
        playerScore = (TextView)findViewById(R.id.textScore);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        povratakSave = (Button)findViewById(R.id.buttonPovratakSaveLvl);

        loadDataSharedPref();
        updateLanguagesOfUI();

        outTextView = (TextView) findViewById(R.id.textView);
        ostvarenRezultat = (TextView)findViewById(R.id.textBodovi);
        velicinaSlova = (Button)findViewById(R.id.promjenaVelicine);
        buttonZnakovi = (Button)findViewById(R.id.buttonZnakovi);

        ostvarenRezultat.setText("" + globalVar.get_OstBodovi());

        if (globalVar.get_ImeSpremljenog() != null){
            outTextView.setText(globalVar.get_ImeSpremljenog());
        }

        Button button0 = (Button)findViewById(R.id.button0);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        Button button7 = (Button)findViewById(R.id.button7);
        Button button8 = (Button)findViewById(R.id.button8);
        Button button9 = (Button)findViewById(R.id.button9);

        Button button10 = (Button)findViewById(R.id.button10);
        Button button11 = (Button)findViewById(R.id.button11);
        Button button12 = (Button)findViewById(R.id.button12);
        Button button13 = (Button)findViewById(R.id.button13);
        Button button14 = (Button)findViewById(R.id.button14);
        Button button15 = (Button)findViewById(R.id.button15);
        Button button16 = (Button)findViewById(R.id.button16);
        Button button17 = (Button)findViewById(R.id.button17);
        Button button18 = (Button)findViewById(R.id.button18);

        Button button19 = (Button)findViewById(R.id.button19);
        Button button20 = (Button)findViewById(R.id.button20);
        Button button21 = (Button)findViewById(R.id.button21);
        Button button22 = (Button)findViewById(R.id.button22);
        Button button23 = (Button)findViewById(R.id.button23);
        Button button24 = (Button)findViewById(R.id.button24);
        Button button25 = (Button)findViewById(R.id.button25);
        Button button26 = (Button)findViewById(R.id.button26); // DEL

        Button button27 = (Button)findViewById(R.id.button27); // SPACE

        //  brojevi 1 -- 9, 0
        Button button28 = (Button)findViewById(R.id.button28);
        Button button29 = (Button)findViewById(R.id.button29);
        Button button30 = (Button)findViewById(R.id.button30);
        Button button31 = (Button)findViewById(R.id.button31);
        Button button32 = (Button)findViewById(R.id.button32);
        Button button33 = (Button)findViewById(R.id.button33);
        Button button34 = (Button)findViewById(R.id.button34);
        Button button35 = (Button)findViewById(R.id.button35);
        Button button36 = (Button)findViewById(R.id.button36);
        Button button37 = (Button)findViewById(R.id.button37);

        //buttonList = new ArrayList<Button>();
        buttonList.add(button0); buttonList.add(button1); buttonList.add(button2);
        buttonList.add(button3); buttonList.add(button4); buttonList.add(button5);
        buttonList.add(button6); buttonList.add(button7); buttonList.add(button8);
        buttonList.add(button9); buttonList.add(button10); buttonList.add(button11);
        buttonList.add(button12); buttonList.add(button13); buttonList.add(button14);
        buttonList.add(button15); buttonList.add(button16); buttonList.add(button17);
        buttonList.add(button18); buttonList.add(button19); buttonList.add(button20);
        buttonList.add(button21); buttonList.add(button22); buttonList.add(button23);
        buttonList.add(button24); buttonList.add(button25); buttonList.add(button26);
        buttonList.add(button27);
        //  brojevi
        buttonList.add(button28); buttonList.add(button29); buttonList.add(button30);
        buttonList.add(button31); buttonList.add(button32); buttonList.add(button33);
        buttonList.add(button34); buttonList.add(button35); buttonList.add(button36);
        buttonList.add(button37);

        for (Button iButton : buttonList){
            iButton.setPaddingRelative(0,0,0,0);
            iButton.setTransformationMethod(null);
            if (LOWECASE){
                iButton.setText(iButton.getText().toString().toLowerCase());
            }
            else{
                iButton.setText(iButton.getText().toString().toUpperCase());
            }
        }

        for (Button iButton : buttonList){

            iButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Button  mButton = (Button)v;
                    if (outTextView.getText().toString().length() < 15 && (mButton != buttonList.get(26))){
                        //  SPACE
                        if (mButton == buttonList.get(27)){
                            outTextView.setText(outTextView.getText().toString() + " ");
                        }
                        //  regularni char
                        else{
                            outTextView.setText(outTextView.getText().toString() + mButton.getText());
                        }
                    }
                    //  BRISANJE
                    else if (mButton == buttonList.get(26)) {
                        System.out.println("brisi");
                        if (outTextView.getText().toString().length() > 0 ){
                            String temp = outTextView.getText().toString().substring(0,outTextView.getText().toString().length() - 1);
                            outTextView.setText(temp);
                        }
                    }
                }

            });
        }
    }

    public void updateLanguagesOfUI(){
        switch (ObadranJezik){

            case 0:
                povratakSave.setText(getString(R.string.back0));
                playerName.setText(getString(R.string.playerName0));
                playerScore.setText(getString(R.string.playerScore0));
                buttonSave.setText(getString(R.string.save0));
                textPotrebnoIme = getString(R.string.insertText0);
                break;

            case 1:
                povratakSave.setText(getString(R.string.back1));
                playerName.setText(getString(R.string.playerName1));
                playerScore.setText(getString(R.string.playerScore1));
                buttonSave.setText(getString(R.string.save1));
                textPotrebnoIme = getString(R.string.insertText1);
                break;

            case 2:
                povratakSave.setText(getString(R.string.back2));
                playerName.setText(getString(R.string.playerName2));
                playerScore.setText(getString(R.string.playerScore2));
                buttonSave.setText(getString(R.string.save2));
                textPotrebnoIme = getString(R.string.insertText2);
                break;

            case 3:
                povratakSave.setText(getString(R.string.back3));
                playerName.setText(getString(R.string.playerName3));
                playerScore.setText(getString(R.string.playerScore3));
                buttonSave.setText(getString(R.string.save3));
                textPotrebnoIme = getString(R.string.insertText3);
                break;

            case 4:
                povratakSave.setText(getString(R.string.back4));
                playerName.setText(getString(R.string.playerName4));
                playerScore.setText(getString(R.string.playerScore4));
                buttonSave.setText(getString(R.string.save4));
                textPotrebnoIme = getString(R.string.insertText4);
                break;
        }
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        ObadranJezik = sharedPref.getInt("odabraniJezik",0);
    }

    public void promjenaVelicineSlova(View view){
        LOWECASE = !LOWECASE;

        if (LOWECASE){
            velicinaSlova.setText("^");
        }
        else {
            velicinaSlova.setText("ˇ");
        }

        for (Button iButton : buttonList){

            if (LOWECASE){
                iButton.setText(iButton.getText().toString().toLowerCase());
            }
            else{
                iButton.setText(iButton.getText().toString().toUpperCase());
            }
        }
    }

    public void ukljuciZnakove(View view){
        znakovnaTipkovnica = !znakovnaTipkovnica;

        if(znakovnaTipkovnica) {
            buttonZnakovi.setText("abc");
            velicinaSlova.setVisibility(View.INVISIBLE);
            brojac = 0;

            for (Button mButton: buttonList){

                privremenaSlova.add(mButton.getText().toString());
                mButton.setText(znakovi[brojac]);
                brojac++;

                if (brojac == 26)   break;
            }
        }
        else{

            brojac = 0;
            buttonZnakovi.setText("Sym");

            for (Button mButton: buttonList){
                mButton.setText(privremenaSlova.get(brojac));
                brojac++;
                if (brojac == 26)   break;
            }

            velicinaSlova.setVisibility(View.VISIBLE);
            privremenaSlova.clear();
        }
    }


    public void spremiRezultat(View view){
        noviActivity = true;

        // sanjin: .getText().trim().isEmpty()

        //  postoje slova
        if (outTextView.getText().toString().matches(".*\\w.*")){

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy%20HH:mm");
            formattedDate = df.format(c.getTime());

            if (globalVar.get_localScoreActive()){

                //dodavanje u bazi podataka
                Players player = new Players(outTextView.getText().toString(),globalVar.get_OstBodovi(),formattedDate );
                dbHandler.addPlayer(player);

                globalVar.set_imeSpremljenogIgraca(outTextView.getText().toString());
                globalVar.set_obavljenoSpremanjeULocalScore(true);
                globalVar.set_vrijemeTrenutnogIgraca(formattedDate);

                Intent intent = new Intent(this,ScoresLocalView.class);
                startActivity(intent);
            }

            else {
                saveOnServer();
                globalVar.set_imeSpremljenogIgraca(outTextView.getText().toString());
                globalVar.set_obavljenoSpremanjeNaWeb(true);

            }
        }
        // prazno, space samo unutra
        else
        {
            Toast.makeText(this,textPotrebnoIme,Toast.LENGTH_SHORT).show();
        }
    }

    public void povratakScoreView(View view){
        noviActivity = true;

        if (globalVar.get_localScoreActive()){
            Intent intent = new Intent(this,ScoresLocalView.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this,ScoresOnlineView.class);
            startActivity(intent);
        }
    }

    public void saveOnServer(){
        sendPlayer task = new sendPlayer(this);
        task.execute();
    }

    //  ASYNC TASK ZA SERVER
    public class sendPlayer extends AsyncTask<Void, Void,Void> {
        private final WeakReference<Activity> activityWeakRef;

        boolean nemaNeta = false;
        String rezultat =""+ globalVar.get_OstBodovi();
        String ime = PrilagodbaImena(outTextView.getText().toString());
        String vrijeme = formattedDate;
        String uniqueID = UUID.randomUUID().toString();

        public sendPlayer(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params ){
            try {
                mojServer.dodaj(uniqueID,ime,rezultat,vrijeme);
            } catch (IOException e) {
                e.printStackTrace();
                nemaNeta = true;
                //Log.i(TAG,"io");
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i(TAG,"json");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                /*
                if (nemaNeta){
                    Toast.makeText(ScoresSave.this,"NO WEB CONNECTION! ",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ScoresSave.this,"Send on server",Toast.LENGTH_SHORT).show();
                }
                */
            }

            Intent intent = new Intent(ScoresSave.this,ScoresOnlineView.class);
            startActivity(intent);
        }
    }

    private String PrilagodbaImena(String ime){
        return ime.replaceAll(" ","%20");
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
