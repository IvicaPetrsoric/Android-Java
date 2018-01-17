package com.example.ivica.horizontallscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean patak;
    Button buton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buton2 = (Button)findViewById(R.id.botunec);
        patak = true;
    }

    public void promjenaSlova(View view){
//        buton2.setText("asd");

        if (patak){
            patak = false;
            buton2.setText("asd");
        }

        else{
            patak = true;
            buton2.setText("kale");
        }
    }
}
