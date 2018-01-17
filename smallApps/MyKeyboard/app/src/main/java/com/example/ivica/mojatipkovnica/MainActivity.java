package com.example.ivica.mojatipkovnica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView outTextView;
    public ArrayList<Button> buttonList = new ArrayList<Button>();
    public ArrayList<String> privremenaSlova = new ArrayList<String>();
   // public String[] privremenaSlova = {};
    public String[] znakovi = {"%","_","!","@","#","/","^","&","(",")",".","-","'","\"",":",";",",","?","~","<",">","{","}","[","]","°"};
    boolean LOWECASE, znakovnaTipkovnica;
    Button velicinaSlova, buttonZnakovi;
    public int brojac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOWECASE = true;
        znakovnaTipkovnica = false;

        outTextView = (TextView) findViewById(R.id.textView);
        velicinaSlova = (Button)findViewById(R.id.promjenaVelicine);
        buttonZnakovi = (Button)findViewById(R.id.buttonZnakovi);

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
}
