package com.example.ivica.sharebear;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    TextView mojText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        mojText = (TextView)findViewById(R.id.mojText);

    }

    // save usser info
    public void saveInfo(View view){

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);  // ssamo moguce preko ove apk pristupiti

        SharedPreferences.Editor editor = sharedPref.edit();    // samo mozemo editat
        editor.putString("username",usernameInput.getText().toString());
        editor.putString("password",passwordInput.getText().toString());
        editor.apply();

        Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show();
    }

    // print out saved data
    public void displayData(View view){

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);  // ssamo moguce preko ove apk pristupiti, tablica userInfo

        String name = sharedPref.getString("username", ""); // vraca sver sto je spremljeno unutra
        String pw = sharedPref.getString("password", ""); // vraca sver sto je spremljeno unutra, ako kljuc ne postoji vraca prazno

        mojText.setText(name + " "+ pw);
    }










}
