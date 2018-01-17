package com.example.ivica.clumsybox.MainMenu;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.ivica.clumsybox.Data.Constants;

import java.util.Random;

/**
 * Created by Ivica on 25.4.2017..
 */

public class MenuAnimations{

    Random rnd = new Random();

    // pozadinski oblaci se kreću gore dole
    public void animateClouds(ImageView clouds){

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());

        // stvaranje animacije
        TranslateAnimation translate = new TranslateAnimation(0, 0, 0,  -Constants.SCREEN_HEIGHT / 10);
        translate.setStartOffset(500);
        translate.setDuration(10000);
        translate.setRepeatMode(Animation.REVERSE);
        translate.setRepeatCount(Animation.INFINITE);

        //animSet.addAnimation(translate);
        clouds.startAnimation(translate);
    }

    // pozadisnka kocka ide dole gore
    public void animateMovigBox(final ImageView box, final boolean startFromUp){
        //System.out.println("ANIMIRAJ VIEW!!!");

        // x poz
        int rndPlayerX = rnd.nextInt(8);
        rndPlayerX = rndPlayerX * Constants.SCREEN_WIDTH_10;

        int startY;
        int endY;

        // ako ide od gore podesiti ga na poziciji
        if (startFromUp){
            startY = - Constants.objectFrameWidth_8;
            endY = Constants.SCREEN_HEIGHT + Constants.objectFrameWidth_8;
        }else{
            startY = Constants.SCREEN_HEIGHT + Constants.objectFrameWidth_8;
            endY = - Constants.objectFrameWidth_8;
        }

        // stvaranje animacije              // x- dx, y - dy
        TranslateAnimation translate = new TranslateAnimation(rndPlayerX, rndPlayerX, startY,  endY);
        translate.setStartOffset(500);
        translate.setDuration(15000);

        box.startAnimation(translate);

        // listeneri kada je animacija gotova
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }
            @Override public void onAnimationRepeat(Animation animation) {
            }
            @Override public void onAnimationEnd(Animation animation) {
                animateMovigBox(box, !startFromUp);
            }
        });
    }
    // blackCircle
    public void animateZoom(ImageView img ,boolean show, int duration){
        float fromScale;
        float toScale;

        if (show){
            fromScale = 0;
            toScale = 2.5f;

        } else {
            fromScale = 2.5f;
            toScale = 0;
        }

        ScaleAnimation anim = new ScaleAnimation(
                fromScale, toScale, // Start and end values for the X axis scaling
                fromScale, toScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(false); // Needed to keep the result of the animation
        //anim.setStartOffset(100);
        anim.setDuration(duration);
        img.startAnimation(anim);
    }

}
