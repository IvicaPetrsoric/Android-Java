package com.example.ivica.progressbar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private Button myButton;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();    // promjena progress bara

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.myProgressBar);   //INSTANCIRANJE
        progressBar.setProgress(00);
        textView = (TextView)findViewById(R.id.myTextView);
        myButton = (Button)findViewById(R.id.Button1);

        myButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View B){

                if (progressStatus == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (progressStatus < 100) {
                                progressStatus += 5;
                                handler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressStatus);
                                        textView.setText("Stanje: " + progressStatus + "/" + progressBar.getMax() + " %");
                                    }
                                });

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            while (progressStatus > 0) {
                                progressStatus -= 2;
                                handler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressStatus);
                                        textView.setText("Stanje: " + progressStatus + "/" + progressBar.getMax() + " %");
                                    }
                                });

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();
                }
            }
        });

    }
}
