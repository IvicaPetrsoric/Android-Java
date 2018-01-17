package com.example.ivica.clumsybox.Registration;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;

import org.w3c.dom.Text;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * Created by Ivica on 27.4.2017..
 */

public class RegistrationGUI extends View {

    public Activity myActivity;
    public RegistrationAnimations registrationAnimations;

    private RelativeLayout superView;
    public RelativeLayout registrationView;
    public Button btnClose;
    public Button btnSave;
    private ImageView background_outside, background_inside;

    private TextView nameText, textSave;
    public EditText nameEditText;

    private int textSize;

    public RegistrationGUI(Context context, RelativeLayout view) {
        super(context);

        myActivity = (Activity) context;
        superView = view;

        registrationAnimations = new RegistrationAnimations();

        getSceenDimension();

        createGUI();
    }

    private void getSceenDimension(){
        //  hvatanje dimenzija ekrana
        DisplayMetrics dm = new DisplayMetrics();
        myActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        float yInches = dm.heightPixels/dm.ydpi;
        float xInches = dm.widthPixels/dm.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);

        if (diagonalInches >= 6.5){
            System.out.println("TABLET");
            textSize = 22;
            // 6.5inch device or bigger
        }else{
            System.out.println("PHONE");
            textSize = 12;
            // smaller device
        }
    }

    private void createGUI(){
        // cointaner objekata
        registrationView = new RelativeLayout(myActivity);
        //registrationView
        registrationView.setBackgroundColor(Color.TRANSPARENT);

        int corecctionFactorY = 2;

        // background rounded
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10);
        shape.setColor(Color.rgb(1,182,212));

        // pozadina vanjska
        background_outside = new ImageView(myActivity);
        //background_outside.setBackground(getResources().getDrawable(R.drawable.blue_background_x1));
        //background_outside.setBackgroundColor(Color.rgb(1,182,212));
        background_outside.setBackground(shape);
        registrationView.addView(background_outside);

        // parametri botuna close
        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) background_outside.getLayoutParams();
        params_1.height = Constants.SCREEN_HEIGHT / 4;
        params_1.width = Constants.objectFrameWidth_8 * 6;
        params_1.topMargin = Constants.SCREEN_HEIGHT / 3 - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_1.rightMargin = Constants.objectFrameWidth_8;
       // params_1.leftMargin = Constants.objectFrameWidth_8;
        params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        background_outside.setLayoutParams(params_1);

        GradientDrawable shape_2 = new GradientDrawable();
        shape_2.setCornerRadius(10);
        shape_2.setColor(Color.rgb(224,247,250));

        // pozadina unutarnja
        background_inside = new ImageView(myActivity);
        //background_inside.setBackground(getResources().getDrawable(R.drawable.inner_background_x1));
        background_inside.setBackground(shape_2);
        registrationView.addView(background_inside);

        // parametri botuna close
        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) background_inside.getLayoutParams();
        params_3.height = Constants.SCREEN_HEIGHT / 6;
        params_3.width = Constants.objectFrameWidth_8 * 5;
        params_3.topMargin = (int)(Constants.SCREEN_HEIGHT / 2.5f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_3.rightMargin = Constants.objectFrameWidth_8 + Constants.objectFrameWidth_8 / 2 ;
        params_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        background_inside.setLayoutParams(params_3);

        // botun zatvaranja
        btnClose = new Button(myActivity);
        btnClose.setBackground(getResources().getDrawable(R.drawable.btn_close));
        registrationView.addView(btnClose);

        // parametri botuna close
        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
        params_2.height = Constants.objectFrameWidth_8 / 2;
        params_2.width = Constants.objectFrameWidth_8 / 2;
        params_2.topMargin = (int)(Constants.SCREEN_HEIGHT / 2.8f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_2.rightMargin = Constants.objectFrameWidth_8 * 6;
        params_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnClose.setLayoutParams(params_2);

        // BOTUN SAVE
        btnSave = new Button(myActivity);
        btnSave.setBackground(getResources().getDrawable(R.drawable.btn_blue));
        //btnSave.setTypeface(null, Typeface.BOLD);
        registrationView.addView(btnSave);

        // parametri botuna close
        RelativeLayout.LayoutParams params_4 = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
        params_4.height = (int)(Constants.objectFrameWidth_8 * 0.7f) ;
        params_4.width = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_4.topMargin = (int)(Constants.SCREEN_HEIGHT / 2f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_4.rightMargin = Constants.objectFrameWidth_8 * 3 + (int)(Constants.objectFrameWidth_8 * 0.875f/8);
        params_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnSave.setLayoutParams(params_4);

        // label iznad botuna save
        textSave = new TextView(myActivity);
        textSave.setTextSize((int)(textSize));
        textSave.setText("Save");
        textSave.setGravity(Gravity.CENTER);
        textSave.setTextColor(Color.WHITE);

        registrationView.addView(textSave);
        //nameText.setVisibility(INVISIBLE);

        RelativeLayout.LayoutParams params_7 = (RelativeLayout.LayoutParams) textSave.getLayoutParams();
        params_7.height = (int)(Constants.objectFrameWidth_8 * 0.7f) ;
        params_7.width = (int)(Constants.objectFrameWidth_8 * 1.75f);
        params_7.topMargin = (int)(Constants.SCREEN_HEIGHT / 2f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_7.rightMargin = Constants.objectFrameWidth_8 * 3 + (int)(Constants.objectFrameWidth_8 * 0.875f/8);
        params_7.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        textSave.setLayoutParams(params_7);

        // label name text
        nameText = new TextView(myActivity);
        nameText.setTextSize((int)(textSize));
        nameText.setText("Name:");
        //nameText.setBackgroundColor(Color.RED);
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextColor(Color.BLACK);

        registrationView.addView(nameText);
        //nameText.setVisibility(INVISIBLE);

        RelativeLayout.LayoutParams params_5 = (RelativeLayout.LayoutParams) nameText.getLayoutParams();
        params_5.height = Constants.objectFrameWidth_8 / 2;
        params_5.width = Constants.objectFrameWidth_8 * 2;
        params_5.topMargin = (int)(Constants.SCREEN_HEIGHT / 2.35f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_5.rightMargin = (int)(Constants.objectFrameWidth_8 * 4.5f) ;
        params_5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nameText.setLayoutParams(params_5);

        // masx 12 znakova
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(12);

        //nameEditText.setBackgroundResource(R.drawable.edit_text_background);

        // text edit
        nameEditText = new EditText(myActivity);
        nameEditText.setTextSize(textSize);
        nameEditText.setFilters(filterArray);
        nameEditText.setGravity(Gravity.CENTER);
        nameEditText.setHint("Enter name!");
        nameEditText.setMaxLines(1);
        nameEditText.setSingleLine();
        nameEditText.clearFocus();
        registrationView.addView(nameEditText);

        RelativeLayout.LayoutParams params_6 = (RelativeLayout.LayoutParams) nameEditText.getLayoutParams();
        params_6.height = (int)(Constants.objectFrameWidth_8 * 0.75f);
        params_6.width = (int)(Constants.objectFrameWidth_8 * 3f);
        params_6.topMargin = (int)(Constants.SCREEN_HEIGHT / 2.35f) - Constants.objectFrameWidth_8 * corecctionFactorY;
        params_6.rightMargin = (int)(Constants.objectFrameWidth_8 * 1.85f) ;
        params_6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nameEditText.setLayoutParams(params_6);

        //hideKeyboard();

        superView.addView(registrationView);

        // korekcija visine i duzine relative layoutas
        registrationView.getLayoutParams().height = Constants.SCREEN_HEIGHT - Constants.objectFrameWidth_8 * corecctionFactorY;
        registrationView.getLayoutParams().width = Constants.SCREEN_WIDTH;

        registrationAnimations.scaleAnimation(registrationView, true, 1000, 0);
    }


    // miče tipkovnicu
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);

        nameEditText.clearFocus();
    }

    public void removeGUI(){
        registrationView.removeAllViewsInLayout();
        superView.removeView(registrationView);

        registrationView = null;
        btnClose = null;
        btnSave = null;
        background_outside = null;
        background_inside = null;
        nameText = null;
        textSave = null;
        nameEditText = null;
    }

}
