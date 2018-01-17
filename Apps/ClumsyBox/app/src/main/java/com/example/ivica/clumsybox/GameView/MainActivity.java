package com.example.ivica.clumsybox.GameView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.Loading.Loading;
import com.example.ivica.clumsybox.R;

public class MainActivity extends Activity {

    private GamePanel gamePanel;

    // ovo se prvo poziva tijekom pokretanja gameView-a
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  podešavanje ekrana na full screen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gamePanel = new GamePanel(this);

        // stvaranej gameView-a
        setContentView(gamePanel);

        //MainActivity.this.onBackPressed();
    }

    public void enterLoading(){
        Intent i = new Intent(Constants.CURRENT_CONTEXT, Loading.class);
        Constants.CURRENT_CONTEXT.startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("OnResume");
        /*
        if (globalVar.get_stanjeMuzike()){
            pokreniServiceNakonResume();
        }
        */
    }

    @Override
    public void onPause(){
        super.onPause();

        System.out.println("OnPause");

        gamePanel.onPause();
        /*
        if (globalVar.get_stanjeMuzike() && (!noviActivity)){
            pauzirajService();
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            if (gamePanel.enableTouchRespond){
                System.out.println("KEYCODE_BACK");
                gamePanel.pauseGame();
            }

            //quitGame();
            return false;
        }

        else if (keyCode == KeyEvent.KEYCODE_HOME){
            System.out.println("KEYCODE_HOME");
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy: Activity");
        Runtime.getRuntime().gc();
    }

}
