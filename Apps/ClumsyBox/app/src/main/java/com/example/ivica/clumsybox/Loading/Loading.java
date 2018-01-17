package com.example.ivica.clumsybox.Loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.MainActivity;
import com.example.ivica.clumsybox.MainMenu.MainMenu;
import com.example.ivica.clumsybox.R;
import com.example.ivica.clumsybox.Registration.Registration;

public class Loading extends Activity {

    // gui elements
    private ImageView loading;
    private RelativeLayout loadingView;

    private String destination;

    // registracija korisnika
    private Registration registration;

    // za više animacija da se odvrti u sekvenci
    private AnimationSet animSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // stvori gui
        createGUI();

        destination = getIntent().getExtras().getString("destination");

        if (destination .equals("menu")){
            startRegistrationView(true);
        }else{
            pokretanjeLvl();
        }
    }

    private void createGUI(){
        // RelativeLayout View
        loadingView = new RelativeLayout(this);
        loadingView.layout(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        loadingView.setBackgroundColor(getResources().getColor(R.color.black));

        // slika loadinga
        loading = new ImageView(this);
        loading.setImageResource(R.drawable.loading);

        loadingView.addView(loading);

        // parametri loadinga
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) loading.getLayoutParams();
        params.height = Constants.SCREEN_WIDTH / 4;
        params.width = Constants.SCREEN_WIDTH / 4;
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        loading.setLayoutParams(params);

        setContentView(loadingView);
    }

    public void startRegistrationView(boolean show){
        if (show){
            loading.setVisibility(View.INVISIBLE);
            registration = new Registration(this, loadingView, true);
            registration.loading = this;
        }
        else{
            registration = null;
            pokretanjeLvl();
        }
    }

    // animacija skaliranja
    public void scaleAnimation(ImageView view){
        animSet = new AnimationSet(true);

        // animacija skaliranja
        ScaleAnimation anim = new ScaleAnimation(
                0, 1, // Start and end values for the X axis scaling
                0, 1, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(false); // Needed to keep the result of the animation
        //anim.setStartOffset(100);
        anim.setDuration(1000);
        animSet.addAnimation(anim);


        final RotateAnimation animRotate = new RotateAnimation(0, 360 * 3,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        //animRotate.setStartOffset(1100);
        animRotate.setDuration(2500);
        animRotate.setFillAfter(true);

        animSet.addAnimation(animRotate);

        // animacija fade-anja
        AlphaAnimation animation1 = new AlphaAnimation(1, 0);
        animation1.setDuration(700);
        animation1.setStartOffset(2500);
        animation1.setFillAfter(false);
        animSet.addAnimation(animation1);

        view.startAnimation(animSet);
    }

    public void pokretanjeLvl(){
        scaleAnimation(loading);

        // listeneri kada je animacija gotova
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }
            @Override public void onAnimationRepeat(Animation animation) {
            }
            @Override public void onAnimationEnd(Animation animation) {
                loading.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();

                switch (destination){

                    case "game":
                        intent =  new Intent(Loading.this, MainActivity.class);
                        break;

                    case "menu":
                        intent =  new Intent(Loading.this, MainMenu.class);
                        intent.putExtra("destination", "menu");
                        break;

                    default:
                        break;
                }

                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}



/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3100);

 */