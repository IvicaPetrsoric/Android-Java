package hr.rma.sl.airviewhover;

import android.graphics.PointF;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Button> buttonList;
    ArrayList<Button> buttonsToEnlarge;
    ArrayList<Button> buttonsToShrink;
    TextView mTextView, outTextView, wTextView;
    float mX = 0f, mY = 0f;
    float moveThreshold = 30;
    LinearLayout firstLL, secondLL, thirdLL, fourthLL;
    long timeEntered;
    long timeThreshold = 100;
    int buttonsInFirstRow = 10;
    int buttonsInSecondRow = 9;
    int buttonsInThirdRow = 8;
    int buttonsInFourthRow = 1;

    boolean hoverInside = false;

    float SCALE_FACTOR_SINGLE_BUTTON = 0.25f;
    float SCALE_FACTOR_DEL_SHRINKED = 0.15f;
    float SCALE_FACTOR_SINGLE_ROW = 0.15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outTextView = (TextView)findViewById(R.id.textView);

        firstLL = (LinearLayout)findViewById(R.id.firstView);
        secondLL = (LinearLayout)findViewById(R.id.secondView);
        thirdLL = (LinearLayout)findViewById(R.id.thirdView);
        fourthLL = (LinearLayout)findViewById(R.id.fourthView);

        Button button0 = (Button)findViewById(R.id.button0);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        Button button7 = (Button)findViewById(R.id.button7);
        Button button8 = (Button)findViewById(R.id.button8);
        Button button9 = (Button)findViewById(R.id.button9);

        Button button10 = (Button)findViewById(R.id.button10);
        Button button11 = (Button)findViewById(R.id.button11);
        Button button12 = (Button)findViewById(R.id.button12);
        Button button13 = (Button)findViewById(R.id.button13);
        Button button14 = (Button)findViewById(R.id.button14);
        Button button15 = (Button)findViewById(R.id.button15);
        Button button16 = (Button)findViewById(R.id.button16);
        Button button17 = (Button)findViewById(R.id.button17);
        Button button18 = (Button)findViewById(R.id.button18);

        Button button19 = (Button)findViewById(R.id.button19);
        Button button20 = (Button)findViewById(R.id.button20);
        Button button21 = (Button)findViewById(R.id.button21);
        Button button22 = (Button)findViewById(R.id.button22);
        Button button23 = (Button)findViewById(R.id.button23);
        Button button24 = (Button)findViewById(R.id.button24);
        Button button25 = (Button)findViewById(R.id.button25);
        Button button26 = (Button)findViewById(R.id.button26); // DEL

        Button button27 = (Button)findViewById(R.id.button27); // SPACE

        button0.setPaddingRelative(0, 0, 0, 0);
        button1.setPaddingRelative(0, 0, 0, 0);
        button2.setPaddingRelative(0, 0, 0, 0);
        button3.setPaddingRelative(0, 0, 0, 0);
        button4.setPaddingRelative(0, 0, 0, 0);
        button5.setPaddingRelative(0, 0, 0, 0);
        button6.setPaddingRelative(0, 0, 0, 0);
        button7.setPaddingRelative(0, 0, 0, 0);
        button8.setPaddingRelative(0, 0, 0, 0);
        button9.setPaddingRelative(0, 0, 0, 0);
        button10.setPaddingRelative(0, 0, 0, 0);
        button11.setPaddingRelative(0, 0, 0, 0);
        button12.setPaddingRelative(0, 0, 0, 0);
        button13.setPaddingRelative(0, 0, 0, 0);
        button14.setPaddingRelative(0, 0, 0, 0);
        button15.setPaddingRelative(0, 0, 0, 0);
        button16.setPaddingRelative(0, 0, 0, 0);
        button17.setPaddingRelative(0, 0, 0, 0);
        button18.setPaddingRelative(0, 0, 0, 0);
        button19.setPaddingRelative(0, 0, 0, 0);
        button20.setPaddingRelative(0, 0, 0, 0);
        button21.setPaddingRelative(0, 0, 0, 0);
        button22.setPaddingRelative(0, 0, 0, 0);
        button23.setPaddingRelative(0, 0, 0, 0);
        button24.setPaddingRelative(0, 0, 0, 0);
        button25.setPaddingRelative(0, 0, 0, 0);
        button26.setPaddingRelative(0, 0, 0, 0);
        button27.setPaddingRelative(0, 0, 0, 0);

        buttonList = new ArrayList<Button>();
        buttonList.add(button0);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);
        buttonList.add(button8);
        buttonList.add(button9);
        buttonList.add(button10);
        buttonList.add(button11);
        buttonList.add(button12);
        buttonList.add(button13);
        buttonList.add(button14);
        buttonList.add(button15);
        buttonList.add(button16);
        buttonList.add(button17);
        buttonList.add(button18);
        buttonList.add(button19);
        buttonList.add(button20);
        buttonList.add(button21);
        buttonList.add(button22);
        buttonList.add(button23);
        buttonList.add(button24);
        buttonList.add(button25);
        buttonList.add(button26);
        buttonList.add(button27);


        buttonsToEnlarge = new ArrayList<Button>();
        buttonsToShrink = new ArrayList<Button>(buttonList);

        for(Button iButton : buttonList){

            iButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Button mButton = (Button)v;
                    //int targetIndex = buttonList.indexOf(mButton);
                    if (mButton == buttonList.get(26)){ // DEL
                        if (outTextView.getText().toString().length() > 0) {
                            String temp = outTextView.getText().toString().substring(
                                    0, outTextView.getText().toString().length() - 1);
                            outTextView.setText(temp);
                        }
                    } else if (mButton == buttonList.get(27)){ // SPACE
                        outTextView.setText(outTextView.getText().toString() + " ");
                    } else // REGULAR CHAR
                        outTextView.setText(outTextView.getText().toString() + mButton.getText());
                }
            });



            iButton.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_HOVER_ENTER: // Enter Hovered

                            timeEntered = SystemClock.elapsedRealtime();
                            //scaleButtons(view, "ENTER");
                            return true;
                            //break;

                        case MotionEvent.ACTION_HOVER_EXIT:   // Leave Hovered
                            //scaleButtons(view, "EXIT");
                            return true;
                            //break;

                        case MotionEvent.ACTION_HOVER_MOVE:   // On Hover
                            hoverInside = true;
                            // Do not scale if we are only short above the button (usual press):
                            if ((SystemClock.elapsedRealtime() - timeEntered) < timeThreshold){
                                return false;
                            }

                            if (hoverInside) {
                                scaleButtons(view, "MOVE");
                                hoverInside = false;
                            }
                            return true;
                            //break;
                    }
                    return false;
                }
            });

        }

    }


    private void scaleButtons(View targetButton, String hoverEvent) {
        Button mButton = (Button)targetButton;
        int buttonIndex = buttonList.indexOf(mButton);
        int lowerBoundButton = 0, upperBoundButton = 0;
        int targetRow = 0;
        float scalefactorOthers = 0f;
        float scalefactorOthersWhenDelEnlarged = 0f;
        boolean delEnlarged = false;

        if (buttonIndex <= 9) {
            lowerBoundButton = 0;
            upperBoundButton = 9;
            targetRow = 1;
        } else if ((buttonIndex >= 10) && (buttonIndex <= 18)) {
            lowerBoundButton = 10;
            upperBoundButton = 18;
            targetRow = 2;
        } else if ((buttonIndex >= 19) && (buttonIndex <= 26)) {
            lowerBoundButton = 19;
            upperBoundButton = 26;
            targetRow = 3;
        } else if ((buttonIndex >= 27) && (buttonIndex <= 27)) {
            lowerBoundButton = 27;
            upperBoundButton = 27;
            targetRow = 4;
        }

        // scale rows:
        LinearLayout.LayoutParams paramsEnlargeRow = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, SCALE_FACTOR_SINGLE_ROW);
        float scaleOtherRows = (0.4f - SCALE_FACTOR_SINGLE_ROW) / 3;
        LinearLayout.LayoutParams paramsShrinkRow = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, scaleOtherRows);

        if (targetRow == 1) {
            firstLL.setLayoutParams(paramsEnlargeRow);
            secondLL.setLayoutParams(paramsShrinkRow);
            thirdLL.setLayoutParams(paramsShrinkRow);
            fourthLL.setLayoutParams(paramsShrinkRow);
            scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInFirstRow - 1 + 0); // no empty place around
        } else if (targetRow == 2) {
            secondLL.setLayoutParams(paramsEnlargeRow);
            firstLL.setLayoutParams(paramsShrinkRow);
            thirdLL.setLayoutParams(paramsShrinkRow);
            fourthLL.setLayoutParams(paramsShrinkRow);
            scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInSecondRow - 1 + 1); // two empty places that equal one button
        } else if (targetRow == 3) {
            thirdLL.setLayoutParams(paramsEnlargeRow);
            firstLL.setLayoutParams(paramsShrinkRow);
            secondLL.setLayoutParams(paramsShrinkRow);
            fourthLL.setLayoutParams(paramsShrinkRow);
            scalefactorOthers =
                    (1f - SCALE_FACTOR_SINGLE_BUTTON - 0.15f - SCALE_FACTOR_DEL_SHRINKED) /
                            (buttonsInThirdRow - 2); // del width calculated in expression
            scalefactorOthersWhenDelEnlarged = (1f - SCALE_FACTOR_SINGLE_BUTTON - 0.15f) /
                    (buttonsInThirdRow - 1);
        } else if (targetRow == 4) {
            fourthLL.setLayoutParams(paramsEnlargeRow);
            firstLL.setLayoutParams(paramsShrinkRow);
            secondLL.setLayoutParams(paramsShrinkRow);
            thirdLL.setLayoutParams(paramsShrinkRow);
            scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInFourthRow - 1 + 5); // two empty places that equal five buttons

        }

        // scale buttons:
            LinearLayout.LayoutParams paramsEnlargeButton = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, SCALE_FACTOR_SINGLE_BUTTON);
            LinearLayout.LayoutParams paramsShrinkButton = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, scalefactorOthers);
            LinearLayout.LayoutParams paramsShrinkButtonSpecial = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, scalefactorOthersWhenDelEnlarged);


        if (/*hoverEvent.equals("ENTER") ||*/ hoverEvent.equals("MOVE") ){

            for(Button iButton : buttonList) {
                int currIndex = buttonList.indexOf(iButton);
                if ((currIndex > upperBoundButton) || (currIndex < lowerBoundButton) || (currIndex == 27)) {
                    continue;
                }

                if (iButton == mButton){
                    iButton.setLayoutParams(paramsEnlargeButton);
                    iButton.setTextSize(24f);
                    if (buttonIndex == 26)  delEnlarged = true;
                } else {
                    if (!delEnlarged) {
                        if (iButton != buttonList.get(26)) {
                            iButton.setLayoutParams(paramsShrinkButton);
                            iButton.setTextSize(12f);
                        } else {
                            iButton.setLayoutParams(new LinearLayout.LayoutParams(
                                    0, LinearLayout.LayoutParams.MATCH_PARENT,
                                    SCALE_FACTOR_DEL_SHRINKED));
                        }
                    } else {
                        if (iButton != buttonList.get(26)) {
                            iButton.setLayoutParams(paramsShrinkButtonSpecial);
                            iButton.setTextSize(12f);
                        }
                    }
                }
            }
        } else if (hoverEvent.equals("EXIT")){
            /*
            for(Button iButton : buttonList) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);

                iButton.setLayoutParams(params);
            }

            buttonsToEnlarge.clear();
            buttonsToShrink = new ArrayList<>(buttonList);
            */
        }

    }

    // If listener is defined in xml:
    /*
    public void metoda(View v){
        System.out.println("TU SAM U METODI");
        Button butonNaKojiJeStisnuto = (Button)v;
        String s = butonNaKojiJeStisnuto.getText().toString();

        if (s.equals("s")){
            Toast.makeText(this, "S", Toast.LENGTH_LONG).show();
        } else if (s.equals("d")) {
            Toast.makeText(this, "D", Toast.LENGTH_LONG).show();
        }
    }
    */

}
