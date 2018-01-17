package com.example.ivica.intenti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pocetak servica
       /* Intent intent = new Intent(this,MojIntentService.class);
        startService(intent);*/


        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    public void onClick(View v){
        Intent i = new Intent(this,acon.class);
        //slanje podataka

        final EditText applesInput =(EditText)findViewById(R.id.editText_id);
        String userMessage = applesInput.getText().toString();
        i.putExtra("JabucnaPoruka",userMessage);

        startActivity(i);
    }
}
