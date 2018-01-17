package com.example.ivica.photofilter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {



    ImageView mojImageView;
    Drawable mojFace;
    Bitmap bitmapImage;    // mozemo dirati sve pixele na slici !!! zato treba konvertati jpg, i png u bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mojImageView = (ImageView)findViewById(R.id.mojImageView);


        //  INVERTIRANJE
        mojFace = getResources().getDrawable(R.drawable.lik);
        bitmapImage = ((BitmapDrawable) mojFace).getBitmap();  // jpeg u bitmap
        Bitmap newPhoto = invertImage(bitmapImage); // invertirtanje slike
        mojImageView.setImageBitmap(newPhoto);



        /*
        // stavljanje jednog iznad drugoga
        Drawable[] layers = new Drawable[2];    // veličina polja
        layers[0] = getResources().getDrawable(R.drawable.lik);
        layers[1] = getResources().getDrawable(R.drawable.okvir);
        LayerDrawable layerDrawable = new LayerDrawable(layers);    // konverzija u drawable slike
        mojImageView.setImageDrawable(layerDrawable);
        */



        //  SPREMANJE SLIKA

        MediaStore.Images.Media.insertImage(getContentResolver(),newPhoto, "title","description");

    }

    // INVERT as bitmap image
    public static Bitmap invertImage(Bitmap original){

        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());   // slika stvarana istih dimenzija te background konfiguracija

        int A,R,G,B;
        int pixelColor;
        int height = original.getHeight();  // pixel u visini, za for petlju
        int width = original.getWidth();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){

                pixelColor = original.getPixel(x,y);
                A = Color.alpha(pixelColor);    // nove vrijednosti pixela
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor); // vracaju boje pixela! na svaku x,y točku

                finalImage.setPixel(x,y,Color.argb(A,R,G,B));
            }
        }

        return finalImage;
    }








}
