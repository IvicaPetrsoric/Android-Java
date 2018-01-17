package com.example.ivica.letecakrafna.GameLvl;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ivica.letecakrafna.Menu.UlazniScreen;
import com.example.ivica.letecakrafna.R;

public class Game extends Activity {

    public static final String TAG = "com.example.ivica.letecakrafna";

    LinearLayout gameView;
    RelativeLayout buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
       // Log.i(TAG,"Proizvodac: "+ manufacturer);
       // Log.i(TAG,"model: "+ model);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // podesiti ekran na fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // extenda surfaceView, this je context, salje cijelu klasu
        setContentView(new GamePanel(this,model));

        gameView =  (LinearLayout)findViewById(R.id.gameView);
        buttonView = (RelativeLayout)findViewById(R.id.buttonView);
       // gameView.setVisibility(View.VISIBLE);
    }

    public void povratakMain(){
        Intent i = new Intent(Game.this, UlazniScreen.class);
        startActivity(i);
    }
}
