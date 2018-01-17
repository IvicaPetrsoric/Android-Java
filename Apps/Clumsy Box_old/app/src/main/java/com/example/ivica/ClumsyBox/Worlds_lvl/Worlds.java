package com.example.ivica.ClumsyBox.Worlds_lvl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.MainMenu.Main_menu;
import com.example.ivica.ClumsyBox.R;
import com.example.ivica.ClumsyBox.Service.BackgroundSoundService;


public class Worlds  extends Activity{

    //public static final String TAG = "com.example.ivica.smartclicker";

    TextView  ukupnoZlatnikaSkrinja, textIspodSvijeta_2;
    String odabraniSvijet,odabraniSvijetZaKupovinu, nemaDovoljnZlatnika,kupovinaSvijetaPrviDio, kupovinaSvijetaDrugiDio, textZaKupovinu, textNeKupovinu;
    String kupovinaUtrgovini, textIgracaUtrgovini, textZlatnikaUtrgovini, textPlayerFor;
    int odabraniJezik,stanjeSkrinje;
    ImageView zlatniciWorld_2,buttonKupovine, buttonOtkljucanihIgraca;
    Button buttonBack;
    LinearLayout world1, world2;
    LinearLayout izaWolrd1,izaWolrd2;
    GlobalneVariable globalVar = GlobalneVariable.getInstance();
    Dialog dialogTrgovine;
    boolean noviActivity, kupljenPopcorn_1;
    LinearLayout iznadSvijeta1, ispodSvijeta1, iznadSvijeta2, ispodSvijeta2;

    @Override
    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worlds);

        noviActivity = false;
        String vrijednostSvijeta_2 = "200";

        ukupnoZlatnikaSkrinja = (TextView)findViewById(R.id.brojZlatnikaSkrinje);
        buttonBack = (Button)findViewById(R.id.buttonBackWorlds);
        buttonKupovine = (ImageView)findViewById(R.id.dugmeKupnje);

        buttonKupovine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTrgovine = new Dialog(Worlds.this);
                dialogTrgovine();
            }
        });

        loadDataSharedPref();
        updateLanguagesOfUI();

        textIspodSvijeta_2 = (TextView)findViewById(R.id.textIspodWrold_2);
        zlatniciWorld_2 = (ImageView)findViewById(R.id.zlatniciIspodWorld_2);
        buttonOtkljucanihIgraca = (ImageView)findViewById(R.id.dugmeMogucihIgraca);

        buttonOtkljucanihIgraca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noviActivity = true;
                Intent i = new Intent(Worlds.this,Worlds_players.class);
                startActivity(i);
            }
        });

        textIspodSvijeta_2.setText(vrijednostSvijeta_2);

        world1 = (LinearLayout)findViewById(R.id.buttonWorld_1);
        izaWolrd1 = (LinearLayout)findViewById(R.id.izaWorld1);
        world2 = (LinearLayout)findViewById(R.id.buttonWorld_2);
        izaWolrd2 = (LinearLayout)findViewById(R.id.izaWorld2);

        iznadSvijeta1 = (LinearLayout)findViewById(R.id.biranSvijet1);
        ispodSvijeta1 = (LinearLayout)findViewById(R.id.biranSvijet11);
        iznadSvijeta2 = (LinearLayout)findViewById(R.id.biranSvijet2);
        ispodSvijeta2 = (LinearLayout)findViewById(R.id.biranSvijet21);

        provjeraAktivnogSvijeta(odabraniSvijet);

        world1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniSvijet = "1";
                provjeraAktivnogSvijeta(odabraniSvijet);

            }
        });

        world2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textIspodSvijeta_2.getText().toString() != ""){
                    odabraniSvijetZaKupovinu = "2";
                    DialogBoxKupnje();
                }
                else{
                    odabraniSvijet = "2";
                    provjeraAktivnogSvijeta(odabraniSvijet);
                }
            }
        });
    }

    public void dialogTrgovine(){
        dialogTrgovine.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogTrgovine.setContentView(R.layout.dialog_kupnje_igraca_coina);

        Window window = dialogTrgovine.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
       // wlp.width = 500;
        window.setAttributes(wlp);

        dialogTrgovine.show();

        TextView textIgraca = (TextView)dialogTrgovine.findViewById(R.id.textTrgovinaPlayers);
        TextView textZlaznika = (TextView)dialogTrgovine.findViewById(R.id.textTrgovinaZlatnici);

        textIgraca.setText(textIgracaUtrgovini);
        textZlaznika.setText(textZlatnikaUtrgovini);

        final TableRow tableRow1 = (TableRow)dialogTrgovine.findViewById(R.id.tableRowPrvi);
        final TableRow tableRow2 = (TableRow)dialogTrgovine.findViewById(R.id.tableDrugi);
        final TableRow tableRow3 = (TableRow)dialogTrgovine.findViewById(R.id.tableTreci);
        final TableRow tableRow4 = (TableRow)dialogTrgovine.findViewById(R.id.tableCetvrti);

        if (!kupljenPopcorn_1){
            tableRow1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tableRow1.setBackgroundColor( Color.parseColor("#006699"));
                    dialogKupnjeUtrgovini(10000);
                    //dialog.cancel();
                }
            });
        }
        else{
            tableRow1.setVisibility(View.INVISIBLE);
        }

        tableRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRow2.setBackgroundColor( Color.parseColor("#006699"));
                dialogKupnjeUtrgovini(100);
            }
        });

        tableRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRow3.setBackgroundColor( Color.parseColor("#006699"));
                dialogKupnjeUtrgovini(500);
            }
        });

        tableRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRow4.setBackgroundColor( Color.parseColor("#006699"));
                dialogKupnjeUtrgovini(1000);
            }
        });
    }

    public void dialogKupnjeUtrgovini(final int vrijednost){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog_kupnje);
        //dialog.setCancelable(false);

        dialog.show();

        final TextView textKupovine = (TextView)dialog.findViewById(R.id.textKupnjeSvijeta);

        Button subButton = (Button)dialog.findViewById(R.id.button2);
        Button cancelButton = (Button)dialog.findViewById(R.id.button);

        if (stanjeSkrinje < vrijednost && vrijednost > 2000 ){
            textKupovine.setText(nemaDovoljnZlatnika);
            subButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
        }
        else if (stanjeSkrinje > vrijednost && vrijednost > 2000)
        {
            textKupovine.setText(kupovinaUtrgovini + textPlayerFor + " "+vrijednost);
        }

        if (vrijednost == 100){
            textKupovine.setText(kupovinaUtrgovini + vrijednost);
        }

        else if (vrijednost == 500){
            textKupovine.setText(kupovinaUtrgovini + vrijednost);
        }

        else if (vrijednost == 1000){
            textKupovine.setText(kupovinaUtrgovini + vrijednost);
        }

        subButton.setText(textZaKupovinu);
        cancelButton.setText(textNeKupovinu);

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vrijednost < 2000){
                    stanjeSkrinje = stanjeSkrinje + vrijednost;
                }
                else{
                    kupljenPopcorn_1 = true;
                    stanjeSkrinje = stanjeSkrinje - vrijednost;
                }

                saveDataSharedPref();
                loadDataSharedPref();

                dialogTrgovine.cancel();
                dialog.cancel();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTrgovine.cancel();
                dialog.cancel();
            }
        });

    }

    public void povratakMain(View view){
        spremanjeBiranogSvijeta();
        Intent i = new Intent(this,Main_menu.class);
        startActivity(i);
        noviActivity = true;
        this.finish();
    }

    public void updateLanguagesOfUI(){
        switch (odabraniJezik){

            case 0:
                textZlatnikaUtrgovini = getString(R.string.textZlatnika0);
                textIgracaUtrgovini = getString(R.string.textPlayers0);
                buttonBack.setText(getString(R.string.back0));
                nemaDovoljnZlatnika = getString(R.string.nemaDovoljnoZlatnika0) + " ";
                kupovinaSvijetaPrviDio = getString(R.string.kupovinaSvijetaPrviDio0) + " ";
                kupovinaSvijetaDrugiDio = " " + getString(R.string.kupovinaSvijetaDrugiDio0) + " ";
                kupovinaUtrgovini = getString(R.string.kupovinaUtrgovini0) + " ";
                textZaKupovinu = getString(R.string.textPozitive0);
                textNeKupovinu = getString(R.string.textNegative0);
                textPlayerFor = getString(R.string.textPlayerFor0);
                break;

            case 1:
                textZlatnikaUtrgovini = getString(R.string.textZlatnika1);
                textIgracaUtrgovini = getString(R.string.textPlayers1);
                buttonBack.setText(getString(R.string.back1));
                nemaDovoljnZlatnika = getString(R.string.nemaDovoljnoZlatnika1) + " ";
                kupovinaSvijetaPrviDio = getString(R.string.kupovinaSvijetaPrviDio1) + " ";
                kupovinaSvijetaDrugiDio = " " + getString(R.string.kupovinaSvijetaDrugiDio1) + " ";
                kupovinaUtrgovini = getString(R.string.kupovinaUtrgovini1) + " ";
                textZaKupovinu = getString(R.string.textPozitive1);
                textNeKupovinu = getString(R.string.textNegative1);
                textPlayerFor = getString(R.string.textPlayerFor1);
                break;

            case 2:
                textZlatnikaUtrgovini = getString(R.string.textZlatnika2);
                textIgracaUtrgovini = getString(R.string.textPlayers2);
                buttonBack.setText(getString(R.string.back2));
                nemaDovoljnZlatnika = getString(R.string.nemaDovoljnoZlatnika2) + " ";
                kupovinaSvijetaPrviDio = getString(R.string.kupovinaSvijetaPrviDio2) + " ";
                kupovinaSvijetaDrugiDio = " " + getString(R.string.kupovinaSvijetaDrugiDio2) + " ";
                kupovinaUtrgovini = getString(R.string.kupovinaUtrgovini2) + " ";
                textZaKupovinu = getString(R.string.textPozitive2);
                textNeKupovinu = getString(R.string.textNegative2);
                textPlayerFor = getString(R.string.textPlayerFor2);
                break;

            case 3:
                textZlatnikaUtrgovini = getString(R.string.textZlatnika3);
                textIgracaUtrgovini = getString(R.string.textPlayers3);
                buttonBack.setText(getString(R.string.back3));
                nemaDovoljnZlatnika = getString(R.string.nemaDovoljnoZlatnika3) + " ";
                kupovinaSvijetaPrviDio = getString(R.string.kupovinaSvijetaPrviDio3) + " ";
                kupovinaSvijetaDrugiDio = " " + getString(R.string.kupovinaSvijetaDrugiDio3) + " ";
                kupovinaUtrgovini = getString(R.string.kupovinaUtrgovini3) + " ";
                textZaKupovinu = getString(R.string.textPozitive3);
                textNeKupovinu = getString(R.string.textNegative3);
                textPlayerFor = getString(R.string.textPlayerFor3);
                break;

            case 4:
                textZlatnikaUtrgovini = getString(R.string.textZlatnika4);
                textIgracaUtrgovini = getString(R.string.textPlayers4);
                buttonBack.setText(getString(R.string.back4));
                nemaDovoljnZlatnika = getString(R.string.nemaDovoljnoZlatnika4) + " ";
                kupovinaSvijetaPrviDio = getString(R.string.kupovinaSvijetaPrviDio4) + " ";
                kupovinaSvijetaDrugiDio = " " + getString(R.string.kupovinaSvijetaDrugiDio4) + " ";
                kupovinaUtrgovini = getString(R.string.kupovinaUtrgovini4) + " ";
                textZaKupovinu = getString(R.string.textPozitive4);
                textNeKupovinu = getString(R.string.textNegative4);
                textPlayerFor = getString(R.string.textPlayerFor4);
                break;
        }
    }

    public void DialogBoxKupnje(){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog_kupnje);

        dialog.show();

        final TextView textKupovine = (TextView)dialog.findViewById(R.id.textKupnjeSvijeta);

        textKupovine.setText(kupovinaSvijetaPrviDio+odabraniSvijetZaKupovinu+kupovinaSvijetaDrugiDio+textIspodSvijeta_2.getText().toString());

        Button subButton = (Button)dialog.findViewById(R.id.button2);
        Button cancelButton = (Button)dialog.findViewById(R.id.button);

        final int vrijednostSvijetaZaKupiti = Integer.parseInt (textIspodSvijeta_2.getText().toString());

        subButton.setText(textZaKupovinu);
        cancelButton.setText(textNeKupovinu);

        if (vrijednostSvijetaZaKupiti > stanjeSkrinje  ){
            subButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
            textKupovine.setText(nemaDovoljnZlatnika);
        }
        else{
            dialog.setCancelable(false);
        }

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odabraniSvijet = "2";
                provjeraAktivnogSvijeta(odabraniSvijet);
                stanjeSkrinje = stanjeSkrinje - vrijednostSvijetaZaKupiti;
                saveDataSharedPref();
                textIspodSvijeta_2.setText("");
                zlatniciWorld_2.setVisibility(View.INVISIBLE);
                loadDataSharedPref();
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

    public void provjeraAktivnogSvijeta(String aktivni){

        if (aktivni.equals("1")){
            iznadSvijeta1.setBackgroundResource(R.drawable.odabrani_svijet);
            ispodSvijeta1.setBackgroundResource(R.drawable.odabrani_svijet);
            iznadSvijeta2.setBackgroundColor(Color.TRANSPARENT);
            ispodSvijeta2.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            iznadSvijeta1.setBackgroundColor(Color.TRANSPARENT);
            ispodSvijeta1.setBackgroundColor(Color.TRANSPARENT);
            iznadSvijeta2.setBackgroundResource(R.drawable.odabrani_svijet);
            ispodSvijeta2.setBackgroundResource(R.drawable.odabrani_svijet);
        }
    }

    public void loadDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        int ukupnoZlatnika = sharedPref.getInt("ukupnoCoina",0);
        odabraniSvijet = sharedPref.getString("odabraniSvijet","1");
        odabraniJezik = sharedPref.getInt("odabraniJezik",0);
        kupljenPopcorn_1 = sharedPref.getBoolean("kupljenIgracPopcorn_1",false);

        //Log.i(TAG,"stanje kokice : " + kupljenPopcorn_1);

        stanjeSkrinje = ukupnoZlatnika;
        ukupnoZlatnikaSkrinja.setText(""+ukupnoZlatnika);
    }

    public void saveDataSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("ukupnoCoina",stanjeSkrinje);
        editor.putString("odabraniSvijet",odabraniSvijet);
        editor.putBoolean("kupljenIgracPopcorn_1",kupljenPopcorn_1);
        editor.apply();
    }

    public void spremanjeBiranogSvijeta(){
        SharedPreferences sharedPref = getSharedPreferences("gameInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("odabraniSvijet",odabraniSvijet);
        editor.apply();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (globalVar.get_stanjeMuzike()) {
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

