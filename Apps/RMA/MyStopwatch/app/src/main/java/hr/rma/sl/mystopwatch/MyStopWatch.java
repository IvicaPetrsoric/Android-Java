package hr.rma.sl.mystopwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyStopWatch extends Activity {

	Button start;
	Button stop;
	Intent service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopwatch);
		
		start = (Button)findViewById(R.id.buttonstart);
		stop = (Button)findViewById(R.id.buttonstop);



		
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				service = new Intent(MyStopWatch.this, StopwatchService.class);
				MyStopWatch.this.startService(service);
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				service = new Intent(MyStopWatch.this, StopwatchService.class);
				MyStopWatch.this.stopService(service);
			}
		});
		
		
	}

}
