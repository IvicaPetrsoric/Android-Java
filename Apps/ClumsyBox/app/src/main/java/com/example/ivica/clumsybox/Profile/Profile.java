package com.example.ivica.clumsybox.Profile;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.ivica.clumsybox.MainMenu.MainMenu;
import com.example.ivica.clumsybox.R;

/**
 * Created by Ivica on 26.4.2017..
 */

public class Profile extends View {

    //private ProfileGUI profileGUI;

    public MainMenu mainMenu;

    private boolean enableTouch;

    public Profile(Context context){
        super(context);

        //profileGUI = new ProfileGUI(context);
    }

    private void setListeners(){
//        profileGUI.btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeProfileiew();
//            }
//        });
//
//        profileGUI.btnProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeProfileiew();
//            }
//        });
//
//        profileGUI.btnName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showNameExtender();
//            }
//        });
    }

    // miče tipkovnicu
    private void hideKeyboard() {
        //InputMethodManager imm = (InputMethodManager)profileGUI.myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(profileGUI.nameEditText.getWindowToken(), 0);
    }

    private void showNameExtender(){
        if (enableTouch){

            hideKeyboard();
            enableTouch = false;

            //profileGUI.showNameExtender(profileGUI.enableNameExtender);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    enableTouch = true;

                }
            }, 500);
        }
    }

    public void closeProfileiew(){

        //System.out.println("TOSTER");

        if (enableTouch){
            hideKeyboard();
            enableTouch = false;

            //profileGUI.startBtnProfileAnim(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mainMenu.startProfileView(false);
             //       profileGUI.removeGUI();

                }
            }, 1100);
        }

    }

    public void setRelativeLayout(RelativeLayout layout){
        //this.profileView = layout;

        //profileGUI.setRelativeLayout(layout);

        setListeners();
        enableTouch = true;
    }

}


