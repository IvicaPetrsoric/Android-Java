package com.example.ivica.framelayouts;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity  {

    ImageView slika1, slika2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slika1 = (ImageView) findViewById(R.id.imageView);
        slika2 = (ImageView) findViewById(R.id.imageView2);

        slika2.setVisibility(View.INVISIBLE);

        slika1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slika1.setVisibility(View.INVISIBLE);
                slika2.setVisibility(View.VISIBLE);
            }
        });

        slika2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slika2.setVisibility(View.INVISIBLE);
                slika1.setVisibility(View.VISIBLE);
            }
        });

    }
}
