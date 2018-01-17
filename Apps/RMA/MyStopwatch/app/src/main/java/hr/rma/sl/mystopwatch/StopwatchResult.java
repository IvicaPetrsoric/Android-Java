package hr.rma.sl.mystopwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class StopwatchResult extends Activity {
	
	TextView start;
	TextView end;
	TextView time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		start = (TextView)findViewById(R.id.textView1);
		end = (TextView)findViewById(R.id.textView2);
		time = (TextView)findViewById(R.id.textView3);
		
		Intent f = getIntent();
		String[] timestamp = f.getStringExtra("time").split("/");
		
		start.setText(timestamp[0]);
		end.setText(timestamp[1]);
		time.setText(timestamp[2]);
	}

}
