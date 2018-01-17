package com.example.ivica.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;


public class MyTask extends AsyncTask<Void,Integer,String> {

    Context context;
    TextView textView;
    Button button;
    ProgressDialog progressDialog;

    MyTask(Context context, TextView textView, Button button){
        this.context = context;
        this.textView = textView;
        this.button = button;
    }

    @Override
    protected String doInBackground(Void... params) {
        int i = 0;
        synchronized (this){
            while (i<10){
                try {
                    wait(1500);
                    i++;
                    publishProgress(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return "Download complete ...";
    }

    @Override
    protected void onPreExecute() {
       // super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Download in Progress ...");
        progressDialog.setMax(10);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);
        textView.setText(result);
        button.setEnabled(true);
        progressDialog.hide();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values);
        int progress = values[0];
        progressDialog.setProgress(progress);
        textView.setText("Download in pPogress ....");
    }
}
