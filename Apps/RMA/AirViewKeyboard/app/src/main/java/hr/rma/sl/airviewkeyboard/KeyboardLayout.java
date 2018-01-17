package hr.rma.sl.airviewkeyboard;

// KEYBOARD WITH ONHOVER ENLARGEMENT

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class KeyboardLayout extends LinearLayout {

	int margin = 0;
	DisplayMetrics dm;
	Context context;
	LayoutInflater inflater;
	InputConnection inputConn;
	int availableHeight;
	int availableWidth;
	public LinearLayout basicLL;
	LinearLayout firstLL, secondLL, thirdLL, fourthLL;
	public ArrayList<Button> buttonList;

	int buttonsInFirstRow = 10;
	int buttonsInSecondRow = 9;
	int buttonsInThirdRow = 8;
	int buttonsInFourthRow = 1;
	float KEYBOARD_HEIGHT_SCALE_FACTOR = 0.285f;
	float SCALE_FACTOR_SINGLE_BUTTON = 0.25f;
	float SCALE_FACTOR_DEL_SHRINKED = 0.15f;
	float SCALE_FACTOR_SINGLE_ROW = 0.40f;
	boolean LOWERCASE = true;


	public KeyboardLayout(LayoutInflater inflater, Context ctx, InputConnection inputConn,
						  float kbHeight, boolean lettercase, float scaleButton, float scaleRow) {

		super(ctx);
		this.context = ctx;
		this.inflater = inflater;
		this.inputConn = inputConn;

		this.SCALE_FACTOR_SINGLE_BUTTON = scaleButton;
		this.SCALE_FACTOR_SINGLE_ROW = scaleRow;
		this.LOWERCASE = lettercase;
		this.KEYBOARD_HEIGHT_SCALE_FACTOR = kbHeight;

		this.margin = (int) (context.getResources().getDisplayMetrics().density + 0.5f);
		this.dm = ctx.getResources().getDisplayMetrics();
		// this.availableHeight = dm.heightPixels * 2 / 7;
		float kbScale = KEYBOARD_HEIGHT_SCALE_FACTOR * dm.heightPixels;
		this.availableHeight = (int)kbScale;
		this.availableWidth = dm.widthPixels;

		inflater.inflate(R.layout.keyboard_layout, this);
		basicLL = (LinearLayout) this.findViewById(R.id.basicView);
		//basicLL.setBackgroundColor(Color.LTGRAY);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
				basicLL.getLayoutParams();
		params.height = this.availableHeight;
		basicLL.setLayoutParams(params);

		Button button0 = (Button)basicLL.findViewById(R.id.button0);
		Button button1 = (Button)basicLL.findViewById(R.id.button1);
		Button button2 = (Button)basicLL.findViewById(R.id.button2);
		Button button3 = (Button)basicLL.findViewById(R.id.button3);
		Button button4 = (Button)basicLL.findViewById(R.id.button4);
		Button button5 = (Button)basicLL.findViewById(R.id.button5);
		Button button6 = (Button)basicLL.findViewById(R.id.button6);
		Button button7 = (Button)basicLL.findViewById(R.id.button7);
		Button button8 = (Button)basicLL.findViewById(R.id.button8);
		Button button9 = (Button)basicLL.findViewById(R.id.button9);

		Button button10 = (Button) basicLL.findViewById(R.id.button10);
		Button button11 = (Button) basicLL.findViewById(R.id.button11);
		Button button12 = (Button) basicLL.findViewById(R.id.button12);
		Button button13 = (Button) basicLL.findViewById(R.id.button13);
		Button button14 = (Button) basicLL.findViewById(R.id.button14);
		Button button15 = (Button) basicLL.findViewById(R.id.button15);
		Button button16 = (Button) basicLL.findViewById(R.id.button16);
		Button button17 = (Button) basicLL.findViewById(R.id.button17);
		Button button18 = (Button) basicLL.findViewById(R.id.button18);

		Button button19 = (Button) basicLL.findViewById(R.id.button19);
		Button button20 = (Button) basicLL.findViewById(R.id.button20);
		Button button21 = (Button) basicLL.findViewById(R.id.button21);
		Button button22 = (Button) basicLL.findViewById(R.id.button22);
		Button button23 = (Button) basicLL.findViewById(R.id.button23);
		Button button24 = (Button) basicLL.findViewById(R.id.button24);
		Button button25 = (Button) basicLL.findViewById(R.id.button25);
		Button button26 = (Button) basicLL.findViewById(R.id.button26); // DEL

		Button button27 = (Button) basicLL.findViewById(R.id.button27); // SPACE

		buttonList = new ArrayList<Button>();
		buttonList.add(button0); buttonList.add(button1); buttonList.add(button2);
		buttonList.add(button3); buttonList.add(button4); buttonList.add(button5);
		buttonList.add(button6); buttonList.add(button7); buttonList.add(button8);
		buttonList.add(button9); buttonList.add(button10); buttonList.add(button11);
		buttonList.add(button12); buttonList.add(button13); buttonList.add(button14);
		buttonList.add(button15); buttonList.add(button16); buttonList.add(button17);
		buttonList.add(button18); buttonList.add(button19); buttonList.add(button20);
		buttonList.add(button21); buttonList.add(button22); buttonList.add(button23);
		buttonList.add(button24); buttonList.add(button25); buttonList.add(button26);
		buttonList.add(button27);

		for (Button iButton : buttonList) {
			iButton.setPaddingRelative(0, 0, 0, 0);
			iButton.setTransformationMethod(null);
			if (LOWERCASE){
				iButton.setText(iButton.getText().toString().toLowerCase());
			} else {
				iButton.setText(iButton.getText().toString().toUpperCase());
			}
		}

	}



	public void scaleRows(int targetRow, float SCALE_FACTOR_SINGLE_ROW){
		firstLL = (LinearLayout) basicLL.findViewById(R.id.firstView);
		secondLL = (LinearLayout) basicLL.findViewById(R.id.secondView);
		thirdLL = (LinearLayout) basicLL.findViewById(R.id.thirdView);
		fourthLL = (LinearLayout) basicLL.findViewById(R.id.fourthView);

		LinearLayout.LayoutParams paramsEnlargeRow = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				0,
				SCALE_FACTOR_SINGLE_ROW);
		float scaleOtherRows = (1f - SCALE_FACTOR_SINGLE_ROW) / 3;
		LinearLayout.LayoutParams paramsShrinkRow = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				0,
				scaleOtherRows);

		if (targetRow == 1) {
			firstLL.setLayoutParams(paramsEnlargeRow);
			secondLL.setLayoutParams(paramsShrinkRow);
			thirdLL.setLayoutParams(paramsShrinkRow);
			fourthLL.setLayoutParams(paramsShrinkRow);
		} else if (targetRow == 2) {
			secondLL.setLayoutParams(paramsEnlargeRow);
			firstLL.setLayoutParams(paramsShrinkRow);
			thirdLL.setLayoutParams(paramsShrinkRow);
			fourthLL.setLayoutParams(paramsShrinkRow);
		} else if (targetRow == 3) {
			thirdLL.setLayoutParams(paramsEnlargeRow);
			firstLL.setLayoutParams(paramsShrinkRow);
			secondLL.setLayoutParams(paramsShrinkRow);
			fourthLL.setLayoutParams(paramsShrinkRow);
		} else if (targetRow == 4) {
			fourthLL.setLayoutParams(paramsEnlargeRow);
			firstLL.setLayoutParams(paramsShrinkRow);
			secondLL.setLayoutParams(paramsShrinkRow);
			thirdLL.setLayoutParams(paramsShrinkRow);
		}
	}


	private int checkInsideButton(int pointerX, int pointerY){

		for (Button iButton : buttonList) {
			int[] loc = new int[2];
			iButton.getLocationOnScreen(loc);
			if (isInside(pointerX, pointerY,
					loc[0], loc[1], loc[0] + iButton.getWidth(), loc[1] + iButton.getHeight())){
				return buttonList.indexOf(iButton);
			}
		}
		return -1;
	}


	private boolean isInside(int pointerX, int pointerY,
							 int boundLeft, int boundTop, int boundRight, int boundBottom){

		if ((pointerX >= boundLeft) && (pointerX <= boundRight)
				&& (pointerY >= boundTop) && (pointerY <= boundBottom)) {
			return true;
		} else {
			return false;
		}
	}



	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		final int action = MotionEventCompat.getActionMasked(event);

		if (action == MotionEvent.ACTION_HOVER_MOVE) {
			int insideIndex = checkInsideButton((int)event.getRawX(), (int)event.getRawY());

			if (insideIndex != -1){
				scaleButtons(insideIndex, "MOVE");
			}
			return false;
		}
		return false;
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		System.out.println("INTERCEPTING...");

		final int action = MotionEventCompat.getActionMasked(event);

		if (action == MotionEvent.ACTION_MOVE) {
			int insideIndex = checkInsideButton((int)event.getRawX(), (int)event.getRawY());
			//System.out.println("BUTTONINDEX: " + insideIndex);
			if (insideIndex != -1){
				scaleButtons(insideIndex, "MOVE");
			}
			return false;
		}

		if (action == MotionEvent.ACTION_DOWN) {
			return false; // Do not intercept touch event, let the child handle it
		}

		if (action == MotionEvent.ACTION_UP) {
			int insideIndex = checkInsideButton((int)event.getRawX(), (int)event.getRawY());

			if (insideIndex == 26){ // DEL
				inputConn.deleteSurroundingText(1, 0);
			} else if (insideIndex == 27) { // SPACE
				inputConn.commitText(" ", 1);
			} else if (insideIndex != -1) { // REGULAR CHAR
				Button target = (Button)buttonList.get(insideIndex);
				inputConn.commitText(target.getText(), 1);
			}

			// This could help if we want to pass touch event to button-onTouchListener in AirViewIME
			if (insideIndex!=-1)
				return false;
			else
				return true;
		}
		return false;
	}



	public void scaleButtons(int targetButton, String hoverEvent) {
		Button mButton = buttonList.get(targetButton);
		int buttonIndex = targetButton;
		System.out.println("scale buttons inside, button index = " + buttonIndex);
		int lowerBoundButton = 0, upperBoundButton = 0;
		int targetRow = 0;
		float scalefactorOthers = 0f;
		float scalefactorOthersWhenDelEnlarged = 0f;
		boolean delEnlarged = false;

		// Which row are we handling right now?
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

		System.out.println("TARGETROW: " + targetRow);

		// calculate scale factors for buttons in a particular row:
		if (targetRow == 1) {
			// In Row#1:: no empty place around ( + 0)
			scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInFirstRow - 1 + 0);
		} else if (targetRow == 2) {
			// In Row#2:: two empty places that equal one button ( + 1)
			scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInSecondRow - 1 + 1);
		} else if (targetRow == 3) {
			scalefactorOthers =
					(1f - SCALE_FACTOR_SINGLE_BUTTON - 0.15f - SCALE_FACTOR_DEL_SHRINKED) /
							(buttonsInThirdRow - 2); // del width calculated in expression
			scalefactorOthersWhenDelEnlarged = (1f - SCALE_FACTOR_SINGLE_BUTTON - 0.15f) /
					(buttonsInThirdRow - 1);
		} else if (targetRow == 4) {
			// In Row#4:: two empty places that equal five buttons ( + 5)
			scalefactorOthers = (1f - SCALE_FACTOR_SINGLE_BUTTON) / (buttonsInFourthRow - 1 + 5);
		}

		// Scale rows:
		scaleRows(targetRow, SCALE_FACTOR_SINGLE_ROW);

		// Scale buttons:
		LinearLayout.LayoutParams paramsEnlargeButton = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, SCALE_FACTOR_SINGLE_BUTTON);
		LinearLayout.LayoutParams paramsShrinkButton = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, scalefactorOthers);
		LinearLayout.LayoutParams paramsShrinkButtonSpecial = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, scalefactorOthersWhenDelEnlarged);

		for (Button iButton : buttonList) {
			int currIndex = buttonList.indexOf(iButton);

			// If button not in a target row, or if is space, do not scale:
			if ((currIndex > upperBoundButton) || (currIndex < lowerBoundButton) ||
					(currIndex == 27)) {
				continue;
			}

			// actual scaling:
			if (iButton == mButton) {
				iButton.setLayoutParams(paramsEnlargeButton);
				iButton.setTextSize(24f);
				if (buttonIndex == 26) delEnlarged = true;
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
	}


}