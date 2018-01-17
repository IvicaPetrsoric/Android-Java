package hr.rma.sl.bouncingball;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int FRAME_RATE = 20;
    BounceView myBounceView;
    Handler taskHandler;
    long time;
    TextView myTextViewSeconds;
    TextView myTextViewLevel;
    ImageButton live1Button, live2Button, live3Button, live4Button, live5Button;
    int lives = 5;
    int level = 1;
    boolean liveJustLost = false;
    int radius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics myDisplaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myDisplaymetrics);
        int screenHeight = myDisplaymetrics.heightPixels;
        int screenWidth = myDisplaymetrics.widthPixels;

        LinearLayout myLL = (LinearLayout)findViewById(R.id.myLL);
        LinearLayout myLLheader = (LinearLayout)findViewById(R.id.LLheader);
        LinearLayout mainView = (LinearLayout)findViewById(R.id.view);

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

        myTextViewSeconds = (TextView)findViewById(R.id.textViewSeconds);
        myTextViewLevel = (TextView)findViewById(R.id.textViewLevel);
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

        myBounceView.setBallInWallListener(new BounceView.ballInWallListener() {
            @Override
            public void onBallInWall() {
                liveLost();
            }
        });

        taskHandler = new Handler();
        time = 0;
        //taskHandler.postDelayed(myTask, FRAME_RATE);
        start_again();
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (lives > 0) {
                        //myBounceView.setBallInMiddle();
                        myBounceView.setBallPosition(radius, radius);
                        myBounceView.setStartVector();
                        liveJustLost = false;
                        taskHandler.postDelayed(myTask, FRAME_RATE);
                        break;
                    } else {
                        start_again();
                        break;
                    }
                case DialogInterface.BUTTON_NEGATIVE:
                    if (lives > 0) {
                        start_again();
                        break;
                    } else {
                        appQuit();
                        break;
                    }
            }
        }
    };


    private void appQuit() {
        taskHandler.removeCallbacks(myTask);
        this.finishAffinity();
    }


    private void start_again(){
        myTextViewLevel.setText("Level 1");
        myBounceView.setBallPosition(radius, radius);
        int speed = radius * 1 / 8;
        myBounceView.setSpeed(speed, speed, true);
        level = 1;
        lives = 5;
        showAvailableLives();
        myBounceView.setBallColor(Color.BLUE);
        liveJustLost = false;
        time = 0;
        taskHandler.postDelayed(myTask, FRAME_RATE);
    }


    private void showAvailableLives(){
        if (lives==0){
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.INVISIBLE);
            live1Button.setVisibility(View.INVISIBLE);
        } else if (lives==1){
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.INVISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives==2){
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.INVISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives==3){
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.INVISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives==4){
            live5Button.setVisibility(View.INVISIBLE);
            live4Button.setVisibility(View.VISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        } else if (lives==5){
            live5Button.setVisibility(View.VISIBLE);
            live4Button.setVisibility(View.VISIBLE);
            live3Button.setVisibility(View.VISIBLE);
            live2Button.setVisibility(View.VISIBLE);
            live1Button.setVisibility(View.VISIBLE);
        }
    }


    private void liveLost(){
        taskHandler.removeCallbacks(myTask);
        liveJustLost = true;
        lives--;
        showAvailableLives();

        String message="";
        String buttonLeft="";
        String buttonRight ="";
        if (lives > 0){
            message = "Oops. Life lost.";
            buttonLeft = "Continue";
            buttonRight = "Start again";
        } else {
            message = "Game over";
            buttonLeft = "Start again";
            buttonRight = "Exit app";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(buttonLeft, dialogClickListener)
                .setNegativeButton(buttonRight, dialogClickListener).show();
    }


    private void showTime(){
        long seconds = time / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        String output = String.format("%02d:%02d", minutes, seconds);
        myTextViewSeconds.setText(output);
    }


    private void checkLevel(){
        if (time > 40000) {
            level = 5;
            myBounceView.setBallColor(Color.BLACK);
            myTextViewLevel.setText("Level 5");
            int speed = radius * 4 / 4;
            myBounceView.setSpeed(speed, speed, false);
        } else if (time > 30000) {
            level = 4;
            myBounceView.setBallColor(Color.DKGRAY);
            myTextViewLevel.setText("Level 4");
            int speed = radius * 3 / 4;
            myBounceView.setSpeed(speed,speed, false);
        } else if (time > 20000){
            myBounceView.setBallColor(Color.GRAY);
            level = 3;
            myTextViewLevel.setText("Level 3");
            int speed = radius * 2 / 4;
            myBounceView.setSpeed(speed, speed, false);
        } else if (time > 10000){
            myBounceView.setBallColor(Color.RED);
            level = 2;
            myTextViewLevel.setText("Level 2");
            int speed = radius * 1 / 4;
            myBounceView.setSpeed(speed,speed, false);
        }
    }


    @Override
    public void onDestroy() {
        taskHandler.removeCallbacks(myTask);
        this.finishAffinity();
        super.onDestroy();
    }

    private Runnable myTask = new Runnable(){
        @Override
        public void run(){
            checkLevel();
            showTime();
            myBounceView.MoveBall();
            myBounceView.invalidate();

            if (!liveJustLost) {
                time += 40;
                taskHandler.postDelayed(myTask, FRAME_RATE);
            }
            else {
                taskHandler.removeCallbacks(myTask);
            }
        }
    };

}
