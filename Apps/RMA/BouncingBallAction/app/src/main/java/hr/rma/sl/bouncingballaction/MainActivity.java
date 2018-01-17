package hr.rma.sl.bouncingballaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    int FRAME_RATE = 20;
    BounceView myBounceView;
    Handler taskHandler;
    long time;
    TextView myTextViewSeconds;
    TextView myTextViewLevel;
    ImageButton live1Button, live2Button, live3Button, live4Button, live5Button;
    LinearLayout layoutLifeBox;
    int lives = 5;
    int level = 1;
    boolean liveJustLost = false;
    int radius;

    MenuItem stopGoItem, startOverItem, sendHighscoreItem, sendLastResultItem;
    boolean ballPaused = true;
    String MY_PREFS_NAME = "HIGHSCORE_RECORD";
    AlertDialog highscoreAlertDlg;
    //boolean highscoreAlertReady = false;
    boolean needToCheckLevels = true;
    ArrayList<ImageButton> additionalButtons;
    long lastKnownResult = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics myDisplaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myDisplaymetrics);
        int screenHeight = myDisplaymetrics.heightPixels;
        int screenWidth = myDisplaymetrics.widthPixels;

        LinearLayout myLL = (LinearLayout) findViewById(R.id.myLL);
        LinearLayout myLLheader = (LinearLayout) findViewById(R.id.LLheader);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.view);
        layoutLifeBox = (LinearLayout) findViewById(R.id.life_box);

        myLL.getLayoutParams().height = screenHeight / 10;
        myLLheader.getLayoutParams().height = screenHeight / 12;
        this.radius = screenWidth / 36;

        ImageButton myButtonLeft = (ImageButton) findViewById(R.id.imageButton);
        ImageButton myButtonUp = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton myButtonDown = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton myButtonRight = (ImageButton) findViewById(R.id.imageButton4);

        live1Button = (ImageButton) findViewById(R.id.imageButton5);
        live2Button = (ImageButton) findViewById(R.id.imageButton6);
        live3Button = (ImageButton) findViewById(R.id.imageButton7);
        live4Button = (ImageButton) findViewById(R.id.imageButton8);
        live5Button = (ImageButton) findViewById(R.id.imageButton9);

        myTextViewSeconds = (TextView) findViewById(R.id.textViewSeconds);
        myTextViewLevel = (TextView) findViewById(R.id.textViewLevel);
        myTextViewLevel.setText("Level 1");

        //myBounceView = (BounceView)findViewById(R.id.view);
        myBounceView = new BounceView(this);

        myBounceView.setBallColor(Color.BLUE);
        myBounceView.setBallRadius((float) radius);
        mainView.addView(myBounceView);

        myButtonLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myBounceView.invokeCommand(1);
            }
        });

        myButtonUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myBounceView.invokeCommand(2);
            }
        });

        myButtonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myBounceView.invokeCommand(3);
            }
        });

        myButtonRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myBounceView.invokeCommand(4);
            }
        });

        final SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        final int mpHit = soundPool.load(this, R.raw.wallhit, 1);
        final int mpUp = soundPool.load(this, R.raw.lifeup, 1);

        myBounceView.setCollisionListener(new BounceView.collisionListener() {
            @Override
            public void onCollision(String cause) {
                if (cause.equals("wall")) {
                    soundPool.play(mpHit, 10, 10, 1, 0, 1f);
                    liveLost();
                } else if (cause.equals("obstacle")) {
                    soundPool.play(mpUp, 10, 10, 1, 0, 1f);
                    liveGained();
                }
            }
        });

        additionalButtons = new ArrayList<ImageButton>();
        taskHandler = new Handler();
        time = 0;
        start_again();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        stopGoItem = menu.findItem(R.id.action_stopgo);
        startOverItem = menu.findItem(R.id.action_startover);
        sendHighscoreItem = menu.findItem(R.id.action_sendhigh);
        sendLastResultItem = menu.findItem(R.id.action_sendlast);

        MenuItem buyLifeItem = menu.findItem(R.id.action_gainlife);
        buyLifeItem.setEnabled(false);

        if (!liveJustLost) {
            startOverItem.setEnabled(false);
            sendHighscoreItem.setEnabled(false);
            sendLastResultItem.setEnabled(false);
        } else {
            startOverItem.setEnabled(true);
            sendHighscoreItem.setEnabled(true);
            sendLastResultItem.setEnabled(true);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_stopgo:
                if (liveJustLost){
                    // life was lost => we start moving again
                    if (lives > 0) {
                        myBounceView.setBallPosition(radius, radius);
                        myBounceView.setStartVector();
                        liveJustLost = false;
                        stopGoItem.setIcon(R.drawable.stop);
                        startOverItem.setEnabled(false);
                        sendHighscoreItem.setEnabled(false);
                        sendLastResultItem.setEnabled(false);
                        taskHandler.postDelayed(myTask, FRAME_RATE);
                    } else {
                        // the last life was lost => we're starting another round
                        start_again();
                        taskHandler.postDelayed(myTask, FRAME_RATE);
                    }
                } else {
                    // life was not lost => we want to un/pause
                    ballPaused = !ballPaused;
                    if (ballPaused) {
                        taskHandler.removeCallbacks(myTask);
                        stopGoItem.setIcon(R.drawable.play);
                    } else {
                        stopGoItem.setIcon(R.drawable.stop);
                        taskHandler.postDelayed(myTask, FRAME_RATE);
                    }
                }
                return true;

            case R.id.action_startover:
                start_again();
                myBounceView.invalidate();
                return true;

            case R.id.action_sendhigh:
                send_highscore();
                return true;

            case R.id.action_sendlast:
                send_lastscore();
                return true;

            case R.id.action_gainlife:
                //lives++;
                liveGained();
                return true;

            case R.id.action_highscore_results:
                this.closeOptionsMenu();
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private void send_lastscore(){

        if (lastKnownResult == -1) {
            Toast.makeText(this, "Error obtaining last known result",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dialog_for_sending(lastKnownResult / 1000,
                "http://rijeka.riteh.hr/~sljubic/bounce/highscores.php",
                "Enter your name and send your last game result [" + lastKnownResult/1000 +
                        "s] to the Bouncing Ball Highscore list",
                "Send last game result",
                "Last result successfully sent.",
                "Error sending last result via network.");
    }


    private void dialog_for_sending(long highscore, final String urladdress,
                                        String dlgMessage, String dlgTitle,
                                        final String dlgResultOK, final String dlgResultNOTok){
        final long localHigh = highscore;

        // Let's design dialog programatically:
        // It will contain title, text, editbox, and progressbar. And 2 buttons, of course.
        // Progress bar will be shown only when network operation lasts longer.
        final LinearLayout myDialogLayout = new LinearLayout(this);
        myDialogLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText myDialogEditText = new EditText(this);
        final ProgressBar myBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        myDialogLayout.addView(myDialogEditText);
        // myDialogLayout.addView(myBar); // only when network operation is performed

        final AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        highscoreAlertDlg = alertDlg.create();
        highscoreAlertDlg.setMessage(dlgMessage);
        highscoreAlertDlg.setTitle(dlgTitle);
        highscoreAlertDlg.setView(myDialogLayout);

        /*
        Add an OnShowListener to change the OnClickListener on the first time the alert is shown.
        Calling getButton() before the alert is shown will return null.
        Then use a regular View.OnClickListener for the button, which will not dismiss
        the AlertDialog after it has been called.
        */

        highscoreAlertDlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                final Button button = highscoreAlertDlg.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String myEditTextValue = myDialogEditText.getText().toString();
                        System.out.println("Entered: " + myEditTextValue);
                        if (myEditTextValue.equals(""))
                        {
                            showToastFromDialog("Name not entered");
                        } else {
                            // SEND highscore via network!
                            myDialogLayout.addView(myBar);
                            button.setEnabled(false);
                            highscoreAlertDlg.getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                            String currentDateandTime = sdf.format(new Date());
                            String gadget = Build.MANUFACTURER + "-" + Build.MODEL;

                            SendTask mySendTask = new SendTask(
                                    urladdress,
                                    myEditTextValue,
                                    gadget,
                                    currentDateandTime,
                                    localHigh);
                            mySendTask.setNetworkOperationFinished(new SendTask.NetworkOperationFinished() {
                                @Override
                                public void onNetworkOperationFinished(String response) {
                                    myBar.setVisibility(View.INVISIBLE);
                                    highscoreAlertDlg.cancel();
                                    if (response!="") {
                                        showToastFromDialog(dlgResultOK);
                                    } else {
                                        showToastFromDialog(dlgResultNOTok);
                                    }
                                }
                            });
                            mySendTask.execute();

                        }
                    }
                });
            }
        });

        highscoreAlertDlg.show();
    }


    private void send_highscore(){
        long localHigh = -1;
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        long restoredHighScoreRecord = prefs.getLong("local_highscore", -1);
        if (restoredHighScoreRecord != -1) {
            localHigh = restoredHighScoreRecord;
        }

        if (localHigh == -1) {
            Toast.makeText(this, "Valid higscore cannot be read from SP",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dialog_for_sending(localHigh,
                "http://rijeka.riteh.hr/~sljubic/bounce/highscores.php",
                "Enter your name and send your highscore [" + localHigh +
                        "s] to the Bouncing Ball Highscore list",
                "Send highscore",
                "Highscore successfully sent.",
                "Error sending highscore via network.");
    }


    private void showToastFromDialog(String message){
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }


    private void appQuit() {
        taskHandler.removeCallbacks(myTask);
        this.finishAffinity();
    }


    private void start_again() {
        myTextViewLevel.setText("Level 1");
        needToCheckLevels = true;
        myBounceView.setBallPosition(radius, radius);
        int speed = radius * 1 / 8;
        myBounceView.setSpeed(speed, speed, true);

        level = 1;
        lives = 5;

        for (int i=0; i<additionalButtons.size(); i++){
            ImageButton tempButton = (ImageButton)additionalButtons.get(i);
            layoutLifeBox.removeView(tempButton);
        }
        additionalButtons.clear();
        scale_remaining_buttons();
        showAvailableLives();

        if (startOverItem!=null) {
            startOverItem.setEnabled(false);
        }
        if (sendHighscoreItem!=null) {
            sendHighscoreItem.setEnabled(false);
        }
        if (sendLastResultItem!=null) {
            sendLastResultItem.setEnabled(false);
        }

        if (stopGoItem!=null)
            stopGoItem.setIcon(R.drawable.play);

        myBounceView.setFirstDraw(true);
        liveJustLost = false;
        ballPaused = true; // let's start with ball paused!
        time = 0;
        showTime();
        //taskHandler.postDelayed(myTask, FRAME_RATE);
    }


    private void showAvailableLives() {
        if (lives == 0) {
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.INVISIBLE);
            live1Button.setVisibility(View.INVISIBLE);
            myTextViewLevel.setText("G-over");
            lastKnownResult = time;
        } else if (lives == 1) {
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.INVISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives == 2) {
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives == 3) {
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives == 4) {
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.VISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives == 5) {
            live5Button.setVisibility(View.VISIBLE);
            live4Button.setVisibility(View.VISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives > 5) {

        }
    }


    private void liveLost() {
        if (stopGoItem!=null)
            stopGoItem.setIcon(R.drawable.play);

        if (startOverItem!=null) {
            startOverItem.setEnabled(true);
        }

        if (sendHighscoreItem!=null) {
            sendHighscoreItem.setEnabled(true);
        }

        if (sendLastResultItem!=null) {
            sendLastResultItem.setEnabled(true);
        }

        taskHandler.removeCallbacks(myTask);
        liveJustLost = true;
        lives--;

        if (lives >= 5) {
            // remove additional button from existing collection
            int addCount = additionalButtons.size();
            additionalButtons.remove(addCount - 1);

            // remove last added view in lifebox (should be imagebutton representing lost life)
            layoutLifeBox.removeViewAt(layoutLifeBox.getChildCount() - 1);

            // scale remaining buttons:
            scale_remaining_buttons();
        }

        showAvailableLives();

        if (lives==0) {
            check_local_highscore();
        }
    }


    private void scale_remaining_buttons(){
        float weightFactor = 1.0f / lives;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, weightFactor);

        // change scale factors for additional buttons (if exist):
        for (int i = 0; i < additionalButtons.size();  i++){
            ImageButton tempButton = (ImageButton)additionalButtons.get(i);
            tempButton.setLayoutParams(params);
        }

        // change scale for "basic" buttons:
        live1Button.setLayoutParams(params);
        live2Button.setLayoutParams(params);
        live3Button.setLayoutParams(params);
        live4Button.setLayoutParams(params);
        live5Button.setLayoutParams(params);
    }


    private void liveGained() {
        lives++;

        if (lives > 5) {
            // change scale factors for existing buttons (according to the one added)
            scale_remaining_buttons();

            // add new image button
            ImageButton newButton = new ImageButton(this);
            newButton.setImageResource(R.drawable.life);

            newButton.setBackground(null);
            newButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (3*scale + 0.5f);
            newButton.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, (1.0f / lives));
            newButton.setLayoutParams(params);

            // add to the corresponding view:
            layoutLifeBox.addView(newButton);

            // add to the corresponding collection:
            additionalButtons.add(newButton);
        }

        showAvailableLives();
    }


    private void check_local_highscore(){
        long localHigh = -1;

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        long restoredHighScoreRecord = prefs.getLong("local_highscore", -1);
        if (restoredHighScoreRecord != -1) {
            localHigh = restoredHighScoreRecord;
            System.out.println("Higscore stored in SP: " + localHigh);
        }

        long currentScoreSeconds = time / 1000;
        try {
            if (currentScoreSeconds > localHigh) {
                SharedPreferences.Editor editor =
                        getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putLong("local_highscore", currentScoreSeconds);
                editor.commit();
                Toast.makeText(this, "Highscore stored in Shared Preferences",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            Toast.makeText(this, "Error storing highscore in Shared Preferences",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void showTime() {
        // show time changes only at 1's seconds:
        if (time % 1000 != 0) return;

        long seconds = time / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        String output = String.format("%02d:%02d", minutes, seconds);
        myTextViewSeconds.setText(output);
    }


    private void checkLevel() {
        // Check levels only up to level 5;
        if (!needToCheckLevels) return;

        // Do not check at all frames; only those in 10's seconds:
        if (time % 10000 != 0) return;

        if (time >= 40000) {
            needToCheckLevels = false;
            level = 5;
            myBounceView.setBallColor(Color.BLACK);
            myTextViewLevel.setText("Level 5");
            int speed = radius * 4 / 4;
            myBounceView.setSpeed(speed, speed, false);
        } else if (time >= 30000) {
            level = 4;
            myBounceView.setBallColor(Color.DKGRAY);
            myTextViewLevel.setText("Level 4");
            int speed = radius * 3 / 4;
            myBounceView.setSpeed(speed, speed, false);
        } else if (time >= 20000) {
            myBounceView.setBallColor(Color.GRAY);
            level = 3;
            myTextViewLevel.setText("Level 3");
            int speed = radius * 2 / 4;
            myBounceView.setSpeed(speed, speed, false);
        } else if (time >= 10000) {
            myBounceView.setBallColor(Color.RED);
            level = 2;
            myTextViewLevel.setText("Level 2");
            int speed = radius * 1 / 4;
            myBounceView.setSpeed(speed, speed, false);
        }
    }


    @Override
    public void onDestroy() {
        taskHandler.removeCallbacks(myTask);
        this.finishAffinity();
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();

        if ((taskHandler!=null) && (myTask!=null))
            taskHandler.removeCallbacks(myTask);
    }


    @Override
    public void onResume() {
        super.onResume();

        if ((taskHandler!=null) && (myTask!=null)) {
            if (!ballPaused) {
                taskHandler.postDelayed(myTask, FRAME_RATE);
            }
        }
    }


    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            checkLevel();
            showTime();
            myBounceView.MoveBall(time);
            myBounceView.invalidate();

            if ((!liveJustLost) && (!ballPaused)) {
                time += FRAME_RATE;
                taskHandler.postDelayed(myTask, FRAME_RATE);
            } else {
                taskHandler.removeCallbacks(myTask);
            }
        }
    };


}
