package com.example.ivica.intenti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class acon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acon);

        Bundle applesData = getIntent().getExtras();

        if(applesData == null){
            return;
        }
        String appleMessage = applesData.getString("JabucnaPoruka");
        final TextView text = (TextView)findViewById(R.id.textView_2);
        text.setText(appleMessage);

    }

    public void povratak(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
