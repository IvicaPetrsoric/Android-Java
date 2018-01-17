package com.example.ivica.clumsybox.Scores;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ivica.clumsybox.Data.Constants;

/**
 * Created by Ivica on 28.4.2017..
 */

public class ScoresAnimations {

    // micanje trake ispod botuna
    public void translateBottomBtns(ImageView bottom, int startBtn, int endBtn, int duration){
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        // stvaranje animacije  // x- dx, y - dy
        TranslateAnimation translate = new TranslateAnimation(startBtn, endBtn, 0,  0);
        translate.setDuration(duration);
        animSet.addAnimation(translate);
        bottom.startAnimation(animSet);
    }

    public void translateActivity(Activity activity, int startPoz, int endPoz, boolean show){
        AnimationSet animSet = new AnimationSet(true);

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

        activity.getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(anim);
    }
}
