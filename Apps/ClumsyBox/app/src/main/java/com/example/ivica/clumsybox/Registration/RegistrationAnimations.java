package com.example.ivica.clumsybox.Registration;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Ivica on 27.4.2017..
 */

public class RegistrationAnimations {

    // animacija skaliranja
    public void scaleAnimation(RelativeLayout view, boolean show, int duration, int offSet){
        float startScale;
        float endScale;

        if (show){

            startScale = 0;
            endScale = 1;
        }else{
            startScale = 1;
            endScale = 0;
        }

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());


        ScaleAnimation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setStartOffset(offSet);
        anim.setDuration(duration);
        view.startAnimation(anim);
    }
}
