//package com.example.ivica.clumsybox.Profile;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Handler;
//import android.text.InputFilter;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.ivica.clumsybox.Data.Constants;
//import com.example.ivica.clumsybox.R;
//
///**
// * Created by Ivica on 26.4.2017..
// */
//
//public class ProfileGUI extends View{
//
//    private RelativeLayout profileView;
//    private ProfileAnimations profileAnimations;
//    public Activity myActivity;
//
//    public ImageButton btnProfile, btnName;
//    public Button btnClose;
//    private Button btnExtenderMain, btnExtenderName;
//
//    private TextView nameText;
//    public EditText nameEditText;
//    private int textSize;
//
//    private ImageView extenderMain, extenderName;
//
//
//
//    public ProfileGUI(Context context){
//        super(context);
//
//        myActivity = (Activity) context;
//        profileAnimations = new ProfileAnimations();
//        getSceenDimension();
//    }
//
//    public void setRelativeLayout(RelativeLayout view){
//        this.profileView = view;
//        createGUI();
//
//
//    }
//
//    private void getSceenDimension(){
//        //  hvatanje dimenzija ekrana
//        DisplayMetrics dm = new DisplayMetrics();
//        myActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        float yInches = dm.heightPixels/dm.ydpi;
//        float xInches = dm.widthPixels/dm.xdpi;
//        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
//
//        if (diagonalInches >= 6.5){
//            System.out.println("TABLET");
//            textSize = 22;
//            // 6.5inch device or bigger
//        }else{
//            System.out.println("PHONE");
//            textSize = 12;
//            // smaller device
//        }
//    }
//
//    private void createGUI(){
//
//        btnClose = new Button(myActivity);
//        btnClose.setBackgroundColor(Color.TRANSPARENT);
//        profileView.addView(btnClose);
//
//        // parametri botuna close
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
//        params_1.height = Constants.SCREEN_HEIGHT;
//        params_1.width = Constants.SCREEN_WIDTH;
//        btnClose.setLayoutParams(params_1);
//
//
//        createBackgroundBtns();
//        createNameExtender();
//        createMainExtenders();
//        createMainExtenderElements();
//
//
//
//        // botun Profile
//        btnProfile = new ImageButton(myActivity);
//
//        profileView.addView(btnProfile);
//
//        // parametri botuna profile
//        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) btnProfile.getLayoutParams();
//        params_2.height = Constants.SCREEN_WIDTH / 4;
//        params_2.width = Constants.SCREEN_WIDTH / 4;
//        btnProfile.setLayoutParams(params_2);
//
//        // animacija botuna
//        startBtnProfileAnim(true);
//    }
//
//    private void createBackgroundBtns(){
//
//        // btn main extender background
//        btnExtenderMain = new Button(myActivity);
//        btnExtenderMain.setBackgroundColor(Color.TRANSPARENT);
//
//        profileView.addView(btnExtenderMain);
//
//        // parametri
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnExtenderMain.getLayoutParams();
//        params_1.height = (int)(Constants.objectFrameWidth_8 * 3.5f);
//        params_1.width = (int)(Constants.objectFrameWidth_8 * 1.75f);
//        params_1.topMargin = Constants.SCREEN_WIDTH / 4 - Constants.SCREEN_WIDTH / 6;
//        params_1.leftMargin = (int)(Constants.objectFrameWidth_8 * 0.125f);
//        params_1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        btnExtenderMain.setLayoutParams(params_1);
//
//
//        // name minor extender
//        btnExtenderName = new Button(myActivity);
//        btnExtenderName.setBackgroundColor(Color.TRANSPARENT);
//        profileView.addView(btnExtenderName);
//
//        // parametri
//        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) btnExtenderName.getLayoutParams();
//        params_2.height = (int)(Constants.objectFrameWidth_8 * 1.5f);
//        params_2.width = (int)(Constants.objectFrameWidth_8 * 3f);
//        params_2.topMargin = Constants.SCREEN_WIDTH / 4 + (int)(Constants.objectFrameWidth_8  * 0);
//        params_2.leftMargin = (int)(Constants.objectFrameWidth_8 * 1.75f);
//        params_2.addRule(RelativeLayout.ALIGN_LEFT);
//        btnExtenderName.setLayoutParams(params_2);
//
//        btnExtenderName.setVisibility(INVISIBLE);
//
//
//    }
//
//    private void createMainExtenders(){
//
//        // glavni extender
//        extenderMain = new ImageView(myActivity);
//        extenderMain.setBackground(getResources().getDrawable(R.drawable.options_background_ccw_x1));
//
//        profileView.addView(extenderMain);
//        extenderMain.setAlpha(0);
//
//        // parametri
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) extenderMain.getLayoutParams();
//        params_1.height = (int)(Constants.objectFrameWidth_8 * 3.5f);
//        params_1.width = (int)(Constants.objectFrameWidth_8 * 1.75f);
//        params_1.topMargin = Constants.SCREEN_WIDTH / 4 - Constants.SCREEN_WIDTH / 6;
//        params_1.leftMargin = (int)(Constants.objectFrameWidth_8 * 0.125f);
//        params_1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        extenderMain.setLayoutParams(params_1);
//    }
//
//    // elemetni po glavnoj traci
//    private void createMainExtenderElements(){
//
//        //  BOTUN NAME
//        btnName = new ImageButton(myActivity);
//        btnName.setBackground(getResources().getDrawable(R.drawable.profile_player_name_x1));
//        profileView.addView(btnName);
//
//        // parametri
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnName.getLayoutParams();
//        params_1.height = (int)(Constants.objectFrameWidth_8);
//        params_1.width = (int)(Constants.objectFrameWidth_8);
//        params_1.topMargin = Constants.SCREEN_WIDTH / 4 + (int)(Constants.objectFrameWidth_8  * 0.25f);
//        params_1.leftMargin = (int)(Constants.objectFrameWidth_8 * 0.5f);
//        params_1.addRule(RelativeLayout.ALIGN_LEFT);
//        btnName.setLayoutParams(params_1);
//
//    }
//
//    private void createNameExtender(){
//
//        //  extender NAME
//        extenderName = new ImageView(myActivity);
//
//        profileView.addView(extenderName);
//
//        // parametri
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) extenderName.getLayoutParams();
//        params_1.height = (int)(Constants.objectFrameWidth_8 * 1.5f);
//        params_1.width = (int)(Constants.objectFrameWidth_8 * 3f);
//        params_1.topMargin = Constants.SCREEN_WIDTH / 4 + (int)(Constants.objectFrameWidth_8  * 0);
//        params_1.leftMargin = (int)(Constants.objectFrameWidth_8 * 1.75f);
//        params_1.addRule(RelativeLayout.ALIGN_LEFT);
//        extenderName.setLayoutParams(params_1);
//
//
//
//        // name text
//        nameText = new TextView(myActivity);
//        nameText.setTextSize((int)(textSize));
//        nameText.setText("Name:");
//        //nameText.setBackgroundColor(Color.RED);
//        nameText.setGravity(Gravity.CENTER);
//        nameText.setTextColor(Color.WHITE);
//
//        profileView.addView(nameText);
//        //nameText.setVisibility(INVISIBLE);
//
//        RelativeLayout.LayoutParams params_2 = (RelativeLayout.LayoutParams) nameText.getLayoutParams();
//        //RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.CENTER_VERTICAL);
//        params_2.height = Constants.objectFrameWidth_8 / 2;
//        params_2.width = Constants.objectFrameWidth_8 * 2;
//        params_2.topMargin = Constants.SCREEN_WIDTH / 4 + (int)(Constants.objectFrameWidth_8  * 0.15f);
//        params_2.leftMargin = (int)(Constants.objectFrameWidth_8 * 2.1f);
//        params_2.addRule(RelativeLayout.ALIGN_LEFT);
//        nameText.setLayoutParams(params_2);
//
//
//
//
//        InputFilter[] filterArray = new InputFilter[1];
//        filterArray[0] = new InputFilter.LengthFilter(12);
//
//        //nameEditText.setBackgroundColor(Color.TRANSPARENT);
//        //nameEditText.setBackgroundResource(R.drawable.edit_text_background);
//
//        // text edit
//        nameEditText = new EditText(myActivity);
//        nameEditText.setTextSize(textSize);
//        nameEditText.setFilters(filterArray);
//        nameEditText.setGravity(Gravity.CENTER);
//        nameEditText.setHint("Enter name!");
//        nameEditText.setMaxLines(1);
//        nameEditText.setSingleLine();
//
//        profileView.addView(nameEditText);
//
//        RelativeLayout.LayoutParams params_3 = (RelativeLayout.LayoutParams) nameEditText.getLayoutParams();
//        params_3.height = (int)(Constants.objectFrameWidth_8 * 0.75f);
//        params_3.width = (int)(Constants.objectFrameWidth_8 * 2.5f);
//        params_3.topMargin = Constants.SCREEN_WIDTH / 4 + (int)(Constants.objectFrameWidth_8  * 0.1f) + Constants.objectFrameWidth_8 / 2;
//        params_3.leftMargin = (int)(Constants.objectFrameWidth_8 * 1.85f);
//        params_3.addRule(RelativeLayout.ALIGN_LEFT);
//        nameEditText.setLayoutParams(params_3);
//
//        extenderName.setVisibility(View.INVISIBLE);
//        nameText.setVisibility(View.INVISIBLE);
//        nameEditText.setVisibility(View.INVISIBLE);
//
//    }
//
//
//    public void startBtnProfileAnim(boolean show){
//
//        int offSet = 0;
//
//        if (!enableNameExtender){
//            offSet = 400;
//            showNameExtender(false);
//            btnExtenderMain.setVisibility(INVISIBLE);
//        }
//
//
//        profileAnimations.rotateOptionsBtn(btnProfile, show, 1000, 0 + offSet);
//        profileAnimations.moveBtns(btnName, show, 400, 200 + offSet);
//        profileAnimations.extendMainExtender(extenderMain, show, 700, 0 + offSet);
//    }
//
//    public boolean enableNameExtender = true;
//
//    public void showNameExtender(boolean show){
//
//        int offSet = 0;
//        // zatvaranje
//        if (!show){
//            nameEditText.setFocusable(false);
//            btnExtenderName.setVisibility(INVISIBLE);
//            offSet = 150;
//
//        }else{
//            nameEditText.setFocusable(true);
//            btnExtenderName.setVisibility(VISIBLE);
//            setVisibiletiy();
//        }
//
//        profileAnimations.extendMinorExtender(extenderName, show, 400 , 0 + offSet);
//        profileAnimations.changeAlphaTextViews(nameText, show, 300, 100);
//        profileAnimations.changeAlphaEditText(nameEditText, show, 300, 100);
//        enableNameExtender = !enableNameExtender;
//    }
//
//    private void setVisibiletiy(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                extenderName.setVisibility(View.VISIBLE);
//                nameText.setVisibility(View.VISIBLE);
//                nameEditText.setVisibility(View.VISIBLE);
//            }
//        }, 800);
//    }
//
//
//
//
//
//
//    public void removeGUI(){
//        profileView.removeView(btnProfile);
//        profileView.removeView(btnClose);
//        profileView.removeView(extenderMain);
//        profileView.removeView(btnName);
//        profileView.removeView(extenderName);
//        profileView.removeView(nameText);
//        profileView.removeView(nameEditText);
//        profileView.removeView(btnExtenderMain);
//        profileView.removeView(btnExtenderName);
//
//        btnProfile = null;
//        btnClose = null;
//        extenderMain = null;
//        btnName = null;
//        extenderName = null;
//        nameText = null;
//        nameEditText = null;
//        btnExtenderMain = null;
//        btnExtenderName = null;
//    }
//
//
//}
