package com.example.ivica.clumsybox.Profile;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;

/**
 * Created by Ivica on 26.4.2017..
 */

public class ProfileAnimations {

    // rotacija dugmeta
    public void rotateOptionsBtn(ImageButton btnImg, boolean show, int duration, int offSet) {

        float startDegree;
        float endDegree;

        if (show) {
            startDegree = 0;
            endDegree = 180.0f;
        } else {
            startDegree = 180.0f;
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

        btnImg.startAnimation(animSet);
    }

    // pozadinski oblaci se kreću gore dole
    public void moveBtns(ImageButton btn, boolean show, int duration, int offset){
        int startY;
        int endY;

        if (show){
            endY = (int) btn.getX();

            startY =  -(int)(Constants.objectFrameWidth_8 * 1.5f);

        }else{
            startY = 0;

            endY = -(int)(Constants.objectFrameWidth_8 * 1.5f);
        }

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);// nakon sto dode do translacije objekt ostaje na toj poziciji!
        animSet.setFillEnabled(true);

        // stvaranje animacije  // x- dx, y - dy
        TranslateAnimation translate = new TranslateAnimation(0, 0, startY,  endY);
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
                1, 1, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setStartOffset(offSet);
        anim.setDuration(duration);
        img.startAnimation(anim);
    }

    // animacija glavno extendera
    public void extendMinorExtender(ImageView img, boolean show, int duration, int offSet){
        float startScale;
        float endScale;

        if (show){
            img.setVisibility(View.VISIBLE);
            startScale = 0;
            endScale = 1;
        }else{
            startScale = 1;
            endScale = 0;
        }

        ScaleAnimation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                1, 1, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setStartOffset(offSet);
        anim.setDuration(duration);
        img.startAnimation(anim);
    }

    // promjena alpha texv Viewa
    public void changeAlphaTextViews(TextView textView, boolean show, int duration, int offSet){
        int startAlpha;
        int endAlpha;

        if (show){
            textView.setVisibility(View.VISIBLE);
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

    // promjena alpha componente textView - a
    public void changeAlphaEditText(EditText editText, boolean show, int duration, int offSet){
        int startAlpha;
        int endAlpha;

        if (show){
            editText.setVisibility(View.VISIBLE);
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
        editText.startAnimation(animation1);
    }

}
