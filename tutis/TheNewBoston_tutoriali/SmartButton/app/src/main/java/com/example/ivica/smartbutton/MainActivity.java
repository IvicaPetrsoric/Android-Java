package com.example.ivica.smartbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mojBotun;
    TextView textZaPrikazati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mojBotun = (Button)findViewById(R.id.myBottun);
        textZaPrikazati = (TextView)findViewById(R.id.myTextView);

        mojBotun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textZaPrikazati.setText("PILAAAARRR");

            }
        });

        mojBotun.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                textZaPrikazati.setText("DRZAN KLIK");
                return true;    // ovo izvršava, u protivnom ce izvrsiti i nakon pustanja klika ce stacviti kao da je bio klik
            }
        });
    }
}
