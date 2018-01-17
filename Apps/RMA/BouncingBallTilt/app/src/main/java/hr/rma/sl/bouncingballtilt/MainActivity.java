package hr.rma.sl.bouncingballtilt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int FRAME_RATE = 20;
    int screenHeight, screenWidth;
    BounceView myBounceView;
    Handler taskHandler;
    long time;
    TextView myTextViewSeconds;
    TextView myTextViewLevel;
    ImageButton live1Button, live2Button, live3Button, live4Button, live5Button;
    ImageButton myButtonLeft, myButtonUp, myButtonDown, myButtonRight, myButtonPause;
    LinearLayout layoutLifeBox;
    int lives = 5;
    int level = 1;
    boolean liveJustLost = false;
    int radius;

    MenuItem stopGoItem, startOverItem, sendHighscoreItem, sendLastResultItem, snapshotItem;
    boolean ballPaused = true;
    String MY_PREFS_NAME = "HIGHSCORE_RECORD";
    AlertDialog highscoreAlertDlg;
    boolean needToCheckLevels = true;
    ArrayList<ImageButton> additionalButtons;
    long lastKnownResult = -1;

    // sensor suppport
    SensorManager sManager = null;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    float[] inR = new float[9];
    float[] gravity = new float[3];
    float[] geomag = new float[3];
    float[] orientVals = new float[3];
    double pitch;
    double roll;
    float RL_THRESHOLD = 30.0f;
    float RR_THRESHOLD = 30.0f;
    float PU_THRESHOLD = 25.0f;
    float PD_THRESHOLD = 25.0f;
    float BASE_THRESHOLD = 15.0f;
    private enum TiltAction {UP, DOWN, LEFT, RIGHT, NONE};
    TiltAction currentTiltAction = TiltAction.NONE;

    String snapshotFilePath = "";
    String snapshotFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sensor support
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
        // device in safezone position does not invoke any command:

        DisplayMetrics myDisplaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myDisplaymetrics);
        screenHeight = myDisplaymetrics.heightPixels;
        screenWidth = myDisplaymetrics.widthPixels;

        LinearLayout myLL = (LinearLayout) findViewById(R.id.myLL);
        LinearLayout myLLheader = (LinearLayout) findViewById(R.id.LLheader);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.view);
        layoutLifeBox = (LinearLayout) findViewById(R.id.life_box);

        myLL.getLayoutParams().height = screenHeight / 10;
        myLLheader.getLayoutParams().height = screenHeight / 12;
        this.radius = screenWidth / 36;

        myButtonLeft = (ImageButton) findViewById(R.id.imageButton);
        myButtonUp = (ImageButton) findViewById(R.id.imageButton2);
        myButtonDown = (ImageButton) findViewById(R.id.imageButton3);
        myButtonRight = (ImageButton) findViewById(R.id.imageButton4);
        myButtonPause = (ImageButton) findViewById(R.id.imageButtonPause);

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

        myButtonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handle_start_stop_toggling();
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


    private void handle_start_stop_toggling(){
        if (liveJustLost){
            // life was lost => we start moving again
            if (lives > 0) {
                myBounceView.setBallPosition(radius, radius);
                myBounceView.setStartVector();
                liveJustLost = false;
                stopGoItem.setIcon(R.drawable.stop);
                myButtonPause.setImageResource(R.drawable.stop_big);
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
                myButtonPause.setImageResource(R.drawable.play_big);
            } else {
                stopGoItem.setIcon(R.drawable.stop);
                myButtonPause.setImageResource(R.drawable.stop_big);
                taskHandler.postDelayed(myTask, FRAME_RATE);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_stopgo:
                handle_start_stop_toggling();
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

            case R.id.action_snapshot:
                takeSnapshot();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private String takeSnapshot() {
        String uniqueID = UUID.randomUUID().toString();
        Date now = new Date();
        String output = DateFormat.format("yyyy-MM-dd_hh-mm-ss", now).toString();
        File snapshotFile = null;

        try {
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            snapshotFile = File.createTempFile(output+ "_" +uniqueID, ".jpg", storageDir);
        } catch (IOException ex) {
            Toast.makeText(this, "Error taking snapshot: cannot create file",
                    Toast.LENGTH_SHORT).show();
            return "";
            // Error occurred while creating the File
        }

        // Continue only if the File was successfully created
        if (snapshotFile != null) {
            try {
                // create bitmap screen capture
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                FileOutputStream ostream = new FileOutputStream(snapshotFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                ostream.flush();
                ostream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Error taking snapshot", Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        Toast.makeText(this, "Snapshot taken", Toast.LENGTH_SHORT).show();
        //return output + "_" + uniqueID + ".jpg";
        snapshotFileName = snapshotFile.getName();
        return snapshotFile.getAbsolutePath();
    }


    private void send_lastscore(){

        if (lastKnownResult == -1) {
            Toast.makeText(this, "Error obtaining last known result",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        snapshotFilePath = takeSnapshot();
        if (snapshotFilePath.equals("")) {
            Toast.makeText(this, "No valid snapshot, highscore sending aborted.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dialog_for_sending(lastKnownResult / 1000,
                "http://rijeka.riteh.hr/~sljubic/bounce/highscores.php",
                "Enter your name and send your last game result [" + lastKnownResult/1000 +
                        "s] with snapshot to the Bouncing Ball Highscore list",
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
                                    localHigh,
                                    snapshotFilePath,
                                    snapshotFileName);
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

        snapshotFilePath = takeSnapshot();
        if (snapshotFilePath.equals("")) {
            Toast.makeText(this, "No valid snapshot, highscore sending aborted.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dialog_for_sending(localHigh,
                "http://rijeka.riteh.hr/~sljubic/bounce/highscores.php",
                "Enter your name and send your highscore [" + localHigh +
                        "s] with current snapshot to the Bouncing Ball Highscore list",
                "Send highscore",
                "Highscore successfully sent.",
                "Error sending highscore via network.");
    }


    private void showToastFromDialog(String message){
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }


    private void appQuit() {
        taskHandler.removeCallbacks(myTask);
        sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
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
        myButtonPause.setImageResource(R.drawable.play_big);

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
        myButtonPause.setImageResource(R.drawable.play_big);

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
        try {
            sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        } catch (Exception ex) {}

        this.finishAffinity();
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();

        sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        if ((taskHandler!=null) && (myTask!=null))
            taskHandler.removeCallbacks(myTask);
    }


    @Override
    public void onResume() {
        super.onResume();

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                    SensorManager.SENSOR_DELAY_GAME);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                                    SensorManager.SENSOR_DELAY_GAME);

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


    // SENSOR-BASED INTERACTION SUPPORT
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get data raw readings:
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, gravity, 0, event.values.length);
            mLastAccelerometerSet = true;
        }
        else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, geomag, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        // If data are valid, perform interaction logic:
        if (mLastMagnetometerSet && mLastAccelerometerSet) {
            SensorManager.getRotationMatrix(inR, null, gravity, geomag);
            SensorManager.getOrientation(inR, orientVals);
            // azimuth = Math.toDegrees(orientVals[0]);
            pitch = Math.toDegrees(orientVals[1]);
            roll = Math.toDegrees(orientVals[2]);

            // Basic tilt logic (could be enhanced):::
            // Pitch Up movement:
            if (pitch > PU_THRESHOLD)
            {
                if (currentTiltAction != TiltAction.UP) {
                    currentTiltAction = TiltAction.UP;
                    myBounceView.invokeCommand(2);
                    myButtonUp.setImageResource(R.drawable.up_highlighted);
                }
            }
            // Pitch Down movement:
            else if (pitch < - PD_THRESHOLD)
            {
                if (currentTiltAction != TiltAction.DOWN) {
                    currentTiltAction = TiltAction.DOWN;
                    myBounceView.invokeCommand(3);
                    myButtonDown.setImageResource(R.drawable.down_highlighted);
                }
            }
            // Roll Right movement:
            else if (roll > RR_THRESHOLD)
            {
                if (currentTiltAction != TiltAction.RIGHT) {
                    currentTiltAction = TiltAction.RIGHT;
                    myBounceView.invokeCommand(4);
                    myButtonRight.setImageResource(R.drawable.right_highlighted);
                }
            }
            // Roll Left movement:
            else if (roll < - RL_THRESHOLD)
            {
                if (currentTiltAction != TiltAction.LEFT) {
                    currentTiltAction = TiltAction.LEFT;
                    myBounceView.invokeCommand(1);
                    myButtonLeft.setImageResource(R.drawable.left_highlighted);
                }
            }
            // No moving detected, the device is assumed to be in the safezone position:
            else {
                if (currentTiltAction != TiltAction.NONE) {
                    currentTiltAction = TiltAction.NONE;
                    myButtonUp.setImageResource(R.drawable.up);
                    myButtonDown.setImageResource(R.drawable.down);
                    myButtonRight.setImageResource(R.drawable.right);
                    myButtonLeft.setImageResource(R.drawable.left);
                }
            }

        }
    }

}
