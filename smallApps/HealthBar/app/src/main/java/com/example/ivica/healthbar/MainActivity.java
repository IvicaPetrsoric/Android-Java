package com.example.ivica.healthbar;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    GameView gameView;
    public static boolean homeButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);   //  FULLSCREEN CANVAS
        //setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();    //  W, H od ekrana te spramanje u varijablama
        Var.DEVICEscreenWidth = metrics.widthPixels;
        Var.DEVICEscreenHeight = metrics.heightPixels;

        GameLoopThread.FPS = 10;

        gameView = new GameView(this);
        setContentView(gameView);   // view class
    }

    //  dok apk nije upaljena
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    @Override
    protected void onResume()
    {
        super.onPause();
    }
    //  pregled pri pritisku dugmad na screen
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this,"Kliknuli ste za nazad ali ne dela",Toast.LENGTH_SHORT).show();
                return true;    //NE IZLAZI
            case KeyEvent.KEYCODE_MENU:
                Toast.makeText(this,"Menu bar u banani",Toast.LENGTH_SHORT).show();
                return true;    // NE OTVARA MENI

            default:
                return false;
        }
    }

}
