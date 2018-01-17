package hr.rma.sl.airviewkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;

/**
 * Created by Sandi on 11.4.2016..
 */
public class AirViewIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    ArrayList<Button> buttonList;
    Context context;
    LayoutInflater inflater;
    KeyboardLayout layout;

    boolean LOWERCASE = true;
    float KEYBOARD_HEIGHT = 0.285f;
    float SCALE_FACTOR_SINGLE_BUTTON = 0.25f;
    float SCALE_FACTOR_SINGLE_ROW = 0.40f;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        inflater = getLayoutInflater();

        loadSettingsFromSP();

        // no dim support
        getWindow().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // load keyboard layout:
        layout = new KeyboardLayout(inflater, context, this.getCurrentInputConnection(),
                KEYBOARD_HEIGHT,
                LOWERCASE, SCALE_FACTOR_SINGLE_BUTTON, SCALE_FACTOR_SINGLE_ROW);
        initialize_UI_components();

        setInputView(layout);
        updateInputViewShown();
        // all application logic will be performed in Keyboardlayout!!!
    }



    private void loadSettingsFromSP(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        LOWERCASE = sp.getBoolean("lettercase", true);
        KEYBOARD_HEIGHT = Float.valueOf(sp.getString("keyboard_height_percentage", "0.285"));
        SCALE_FACTOR_SINGLE_BUTTON = Float.valueOf(sp.getString("enlarge_key_percentage", "0.25"));
        SCALE_FACTOR_SINGLE_ROW = Float.valueOf(sp.getString("enlarge_row_percentage", "0.40"));
        System.out.println("KB_SCALE=" + KEYBOARD_HEIGHT);
    }



    private void initialize_UI_components() {

        buttonList = new ArrayList(layout.buttonList);
        for (Button iButton : buttonList) {

            // If we would like to consume touch event (after interception):
            /*
            iButton.setOnTouchListener(new View.OnTouchListener() {
                   @Override
                   public boolean onTouch(View v, MotionEvent event) {

                       if (event.getAction() == MotionEvent.ACTION_UP) {
                           System.out.println("EVENT TRANSLATED HERE! " + iButton.getText());
                       }

                       return true;
                   }
           });
           */
        }
    }



    @Override
    public View onCreateInputView() {
        return null;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onWindowShown() {
        loadSettingsFromSP();
        layout = new KeyboardLayout(inflater, context, this.getCurrentInputConnection(),
                KEYBOARD_HEIGHT,
                LOWERCASE, SCALE_FACTOR_SINGLE_BUTTON, SCALE_FACTOR_SINGLE_ROW);
        setInputView(layout);
        updateInputViewShown();
    }
}
