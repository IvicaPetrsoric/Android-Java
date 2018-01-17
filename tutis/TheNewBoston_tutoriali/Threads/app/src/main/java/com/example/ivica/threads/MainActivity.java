package com.example.ivica.threads;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler() {    //chiling u main thread

        @Override
        public void handleMessage(Message msg) {
           // super.handleMessage(msg);


            TextView texter = (TextView)findViewById(R.id.textView_view);
            texter.setText("Odraden posao!");

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBucksButton(View view){

        // sada se dugme nece freezati
        Runnable r = new Runnable() {   //THREAD U POZADINI, tu ne stavljamo interface!!!   Preko handlera mjenjamo interface
            @Override
            public void run() {

                long futureTime = System.currentTimeMillis()+10000;
                while(System.currentTimeMillis() <futureTime){
                    synchronized (this) { // kdo vise thredova da ne skacu jedan dgugome
                        try{
                            wait(futureTime-System.currentTimeMillis());
                        }catch (Exception e){}
                    }
                }
                handler.sendEmptyMessage(0);//pozivamo handlera
            }
        };

        Thread mojThread = new Thread(r);
        mojThread.start();  // tu pocinje izvrsavati thread, naravno ne mozemo puno threda jer ovisimo o procesoru


    }
}
