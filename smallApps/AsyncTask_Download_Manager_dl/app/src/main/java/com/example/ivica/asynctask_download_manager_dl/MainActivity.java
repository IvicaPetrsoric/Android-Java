package com.example.ivica.asynctask_download_manager_dl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    Button btnTrad, btnDM;
    String myHTTPUrl = "http://www.wingnity.com/files/wingnity_logo.png";
    //String MyHTTPUrl = "http://www.wingnity.com/files/Hall_of_the_Mountain_King.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // trebamo vise dretvi za rad

                new MyTask().execute(); // počinjera rad u pozadini asyncTaska

            }
        });

        btnDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class MyTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL myurl = new URL(myHTTPUrl);

                HttpURLConnection connection = (HttpURLConnection)myurl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.connect();

                //File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
               // File image = File.createTempFile(imageFileName,".jpg",storageDir);

                File rootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"My pictures");

                if (!rootDirectory.exists()){
                    rootDirectory.mkdirs();
                }
                // dat ce file name, moglo se zvati bilo kako
                String nameOfFile = URLUtil.guessFileName(myHTTPUrl,null, MimeTypeMap.getFileExtensionFromUrl(myHTTPUrl));

                File file = new File(rootDirectory,nameOfFile);
                file.createNewFile();

                InputStream inputStream = connection.getInputStream();

                FileOutputStream output = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int byteCount = 0;

                while ((byteCount = inputStream.read(buffer)) >0){
                    output.write(buffer,0,byteCount);
                }

                output.close();

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                getActivity().sendBroadcast(intent);

            // za url
            } catch (MalformedURLException e) {
                e.printStackTrace();

            // za HttpURL
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this,"COMPLETED",Toast.LENGTH_SHORT).show();
        }
    }

}




















