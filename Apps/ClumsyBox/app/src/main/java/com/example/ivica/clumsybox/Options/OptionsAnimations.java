package com.example.ivica.clumsybox.Options;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;

/**
 * Created by Ivica on 25.4.2017..
 */

public class OptionsAnimations {

    // rotacija dugmeta
    public void rotateOptionsBtn(ImageButton btnOptions, boolean firstEnter, int duration, int offSet){
        float startDegree;
        float endDegree;

        if (firstEnter) {
            startDegree = 0;
            endDegree = -90.0f;
        } else {
            startDegree = -90.0f;
            endDegree = 0;
        }

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(startDegree, endDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setStartOffset(offSet);
        animRotate.setDuration(duration);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        btnOptions.startAnimation(animSet);
    }

    // pozadinski oblaci se kreću gore dole
    public void moveBtnMusic(ImageButton btn, boolean out,boolean btnMusic, int duration, int offset){
        int endX;
        int startX;

        if (out){
            endX = (int) btn.getX();
            if (btnMusic){
                startX =  (int)(Constants.objectFrameWidth_8 * 1.5f);
            }else{
                startX = (int)(Constants.objectFrameWidth_8 * 2.5f);
            }

        }else{
            startX = 0;

            if (btnMusic){
                endX = (int)(Constants.objectFrameWidth_8 * 1.7f);
            }else {
                endX = (int)(Constants.objectFrameWidth_8 * 2.5f);
            }
        }

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);


        // stvaranje animacije  // x- dx, y - dy
        TranslateAnimation translate = new TranslateAnimation(startX, endX, 0,  0);
        translate.setDuration(duration);
        translate.setStartOffset(offset);
        animSet.addAnimation(translate);

        btn.startAnimation(animSet);
    }

    // animacija glavno extendera
    public void extendMainExtender(ImageView img, boolean show, int duration, int offSet){
        float startScale;
        float endScale;

        if (show){
            img.setAlpha(255);
            startScale = 0;
            endScale = 1;
        }else{
            startScale = 1;
            endScale = 0;
        }

        ScaleAnimation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                1, 1, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setStartOffset(offSet);
        anim.setDuration(duration);
        img.startAnimation(anim);
    }

    // animacija sporednih extendera
    public void extendMinorExtenders(ImageView imgView, boolean extend, int duration, int offSet){
        float startScale;
        float endScale;

        if (extend){

            startScale = 0;
            endScale = 1;
        }else{
            startScale = 1;
            endScale = 0;
        }

        ScaleAnimation anim = new ScaleAnimation(
                1, 1, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setStartOffset(offSet);
        anim.setDuration(duration);
        imgView.startAnimation(anim);
    }

    // promjena alpha componente Seekbara
    public void changeAlphaSeekBars(SeekBar seekBar, boolean show, int duration, int offSet){
        int startAlpha;
        int endAlpha;

        if (show){
            startAlpha = 0;
            endAlpha = 1;
        }
        else{
            startAlpha = 1;
            endAlpha = 0;
        }

        AlphaAnimation animation1 = new AlphaAnimation(startAlpha, endAlpha);
        animation1.setDuration(duration);
        animation1.setStartOffset(offSet);
        animation1.setFillAfter(true);
        seekBar.startAnimation(animation1);
    }

    // promjena alpha componente textView - a
    public void changeAlphaTextViews(TextView textView, boolean show, int duration, int offSet){
        int startAlpha;
        int endAlpha;

        if (show){
            startAlpha = 0;
            endAlpha = 1;
        }
        else{
            startAlpha = 1;
            endAlpha = 0;
        }

        AlphaAnimation animation1 = new AlphaAnimation(startAlpha, endAlpha);
        animation1.setDuration(duration);
        animation1.setStartOffset(offSet);
        animation1.setFillAfter(true);
        textView.startAnimation(animation1);
    }

}
