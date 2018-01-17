package com.example.ivica.java2gui;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  LAYOUT
        RelativeLayout buckysLayout = new RelativeLayout(this);
        buckysLayout.setBackgroundColor(Color.GREEN);

        //  STVARANJE BOTUNA
        Button redButton = new Button(this);
        redButton.setText(" Log In! ");
        redButton.setBackgroundColor(Color.RED);
        redButton.setId(1);

        //  UserName input
        EditText username = new EditText(this);
        //EditText.setId(2);

        // NACIN PONASANJA LAYOUTA BOTUNA
        RelativeLayout.LayoutParams buttonDetails = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        buttonDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        // NACIN PONASANJA LAYOUTA userInputa
        RelativeLayout.LayoutParams usernameDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        // pozicija username
        usernameDetails.addRule(RelativeLayout.ABOVE,redButton.getId());
        usernameDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        usernameDetails.setMargins(0,0,0,50);

        //konvert dip u pixels
        Resources r = getResources();
        // dp => pixels, na svim jednako
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,r.getDisplayMetrics());    // uzima DIP i prebacuje u px,mjenja se od devica to device

        username.setWidth(px);




        // dodavanje botuna na layout pri odredenim zakonitostima
        buckysLayout.addView(redButton,buttonDetails);
        buckysLayout.addView(username,usernameDetails);
        //  postaviti layout za aktivni,main
        setContentView(buckysLayout);


    }
}
