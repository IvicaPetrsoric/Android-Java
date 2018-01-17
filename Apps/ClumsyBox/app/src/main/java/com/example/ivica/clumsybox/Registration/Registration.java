package com.example.ivica.clumsybox.Registration;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.ivica.clumsybox.Loading.Loading;
import com.example.ivica.clumsybox.MainMenu.MainMenu;


/**
 * Created by Ivica on 27.4.2017..
 */

public class Registration extends View{

    private RegistrationGUI registrationGUI;

    private boolean enableTouch;

    public MainMenu mainMenu;
    public Loading loading;

    private boolean regOpenFromLoading;

    public Registration(Context context, RelativeLayout registrationView, boolean regOpenFromLoading){
        super(context);

        registrationGUI = new RegistrationGUI(context, registrationView);

        //hideKeyboard();
        setListeners();
        enableTouch = true;

        this.regOpenFromLoading = regOpenFromLoading;
    }

    private void setListeners(){
        registrationGUI.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRegistration();
            }
        });

        registrationGUI.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRegistration();
            }
        });
    }

    private void closeRegistration(){
        if (enableTouch){
            enableTouch = false;
            registrationGUI.hideKeyboard();
            registrationGUI.registrationAnimations.scaleAnimation(registrationGUI.registrationView, false, 500, 0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    registrationGUI.removeGUI();
                    registrationGUI.registrationAnimations = null;
                    registrationGUI = null;

                    // otvoren prije loadinga
                    if (regOpenFromLoading){
                        loading.startRegistrationView(false);
                    }else {
                        mainMenu.startRegistrationView(false);
                    }
                }
            }, 600);
        }
    }

}
