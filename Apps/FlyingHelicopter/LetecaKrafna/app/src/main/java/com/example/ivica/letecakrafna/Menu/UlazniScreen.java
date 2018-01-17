package com.example.ivica.letecakrafna.Menu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ivica.letecakrafna.GameLvl.Game;
import com.example.ivica.letecakrafna.R;

public class UlazniScreen extends Activity {

    Button buttonStart, buttonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ulazni_screen);

        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonQuit = (Button)findViewById(R.id.buttonQuit);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UlazniScreen.this, Game.class);
                startActivity(i);
                //finish();
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}
