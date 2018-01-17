package com.example.ivica.takephoto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mojImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mojBOTTUN = (Button) findViewById(R.id.mojButton);
        mojImageView = (ImageView) findViewById(R.id.mojImageView);

        // ako nema kameru onemogući dugme
       if(!hasCamera()){
            mojBOTTUN.setEnabled(false);
        }
    }

    // check if usser has camera
    private boolean hasCamera(){

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY); // provjera da li postji bilo koja kamera
    }

    // launchin camera
    public void launchCamera(View view){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // take a picture and pass ressult along to onActivityResult
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);   // povratak slike

    }

    // if you wanrt to return this image taken


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { /// automatski se zove kada krene startActivityResult
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            // get the photo
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap)extras.get("data");  // spremanje u bitmap
            mojImageView.setImageBitmap(photo); // stavljanje na zaslon

        }
    }






}
