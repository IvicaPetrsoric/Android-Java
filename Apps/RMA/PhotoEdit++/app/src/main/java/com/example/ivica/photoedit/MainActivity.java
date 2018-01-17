package com.example.ivica.photoedit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    String editedImageFileName;
    private Button photoButton;
    private Button saveButton;
    LinearLayout mDrawingPad;
    ProgressBar myBar;
    int OdabranaBoja;
    MenuItem CrvenaBoja, ZelenaBoja,PlavaBoja,Brisalo,Undo1,Redo1;

    public static SeekBar mySeekBar;
    public static TextView myTextView;

    boolean StvorenaSlika;

    //System.out.println(progress);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DrawingView mDrawingView = new DrawingView(this);
        setContentView(R.layout.activity_main);

        //SEEK BAR-potezni bar

        mySeekBar = (SeekBar)findViewById(R.id.DebljinaPrsta);
        myTextView = (TextView)findViewById(R.id.IspisVelicineBojke);
        mySeekBar.setVisibility(View.INVISIBLE);

        /*
        mySeekBar = (SeekBar) findViewById(R.id.DebljinaPrsta);
        myTextView = (TextView)findViewById(R.id.IspisVelicineBojke);
        myTextView.setText(mySeekBar.getProgress());
        */
       // myTextView.setText(0);
        StvorenaSlika = false;

        photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setTransformationMethod(null);
        saveButton = (Button) this.findViewById(R.id.button2);
        saveButton.setTransformationMethod(null);
        saveButton.setEnabled(false);
        mDrawingPad = (LinearLayout) findViewById(R.id.drawing);
        mDrawingPad.addView(mDrawingView);
        myBar = (ProgressBar) this.findViewById(R.id.progressBar);
        myBar.setVisibility(View.INVISIBLE);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Taking picture simply (with thumbnail presentation):
                /*
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                */

                // Taking picture with a storing:
                //set_default_measures();
                saveButton.setEnabled(false);
                dispatchTakePictureIntent();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //  SaveImage();
                new SaveEditedPhotoAsyncTask().execute();
            }
        });
    }
    //  SEEK BAR

    public void Seeker() {
        mySeekBar.setVisibility(View.VISIBLE);
        mySeekBar.setVisibility(View.VISIBLE);
        myTextView.setText("0");

        mySeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
                    {
                        progress_value = progress;
                        myTextView.setText(""+progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekbar)
                    {
                        System.out.println(progress_value);
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {
                         myTextView.setText(""+progress_value);
                        DrawingView.PromjenaDebljine(progress_value);
                    }
                }
        );
    }

    //  MENU
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        CrvenaBoja = menu.findItem(R.id.action_crvena);
        ZelenaBoja = menu.findItem(R.id.action_zelena);
        PlavaBoja = menu.findItem(R.id.action_plava);
        Brisalo = menu.findItem(R.id.action_bijela);
        Undo1 = menu.findItem(R.id.action_undo);
        Redo1 = menu.findItem(R.id.action_redo);

        if (!StvorenaSlika) {
            CrvenaBoja.setEnabled(false);
            ZelenaBoja.setEnabled(false);
            PlavaBoja.setEnabled(false);
            Brisalo.setEnabled(false);
            Undo1.setEnabled(false);
            Redo1.setEnabled(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      // Intent intent = new Intent(MainActivity.this, DrawingView.class);
        switch (item.getItemId()) {

            case R.id.action_crvena:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);
                Toast.makeText(this,"Birana je crvena boja", Toast.LENGTH_LONG).show();
                OdabranaBoja = 0;
                DrawingView.PromjenaBoje(OdabranaBoja);

                //intent.putExtra("Boja",Boja);
                //startActivity(intent);
                //mPaint
                //  BIRAJ CRVENU
                return true;

            case R.id.action_zelena:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);
                Toast.makeText(this,"Birana je zelena boja", Toast.LENGTH_LONG).show();
                OdabranaBoja = 1;
                DrawingView.PromjenaBoje(OdabranaBoja);
                //  BIRAJ ZELENA
                return true;

            case R.id.action_plava:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);
                Toast.makeText(this,"Birana je plava boja", Toast.LENGTH_LONG).show();
                OdabranaBoja = 2;
                DrawingView.PromjenaBoje(OdabranaBoja);
                //  BIRAJ PLAVA
                return true;

            case R.id.action_bijela:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);
                Toast.makeText(this,"Brisač!", Toast.LENGTH_LONG).show();
                OdabranaBoja = 3;
                DrawingView.PromjenaBoje(OdabranaBoja);

/*            case R.id.action_undo:
                Toast.makeText(this,"Undo!", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_redo:
                Toast.makeText(this,"BRedo!", Toast.LENGTH_LONG).show();    */

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Toggle_Menu() {
        if (StvorenaSlika) {
            CrvenaBoja.setEnabled(true);
            ZelenaBoja.setEnabled(true);
            PlavaBoja.setEnabled(true);
            Brisalo.setEnabled(true);
            Undo1.setEnabled(true);
            Redo1.setEnabled(true);
        }
        else {
            CrvenaBoja.setEnabled(false);
            ZelenaBoja.setEnabled(false);
            PlavaBoja.setEnabled(false);
            Brisalo.setEnabled(false);
            Undo1.setEnabled(false);
            Redo1.setEnabled(false);
        }
    }

    protected void saveImage() {
        StvorenaSlika = false;
       // Toggle_Menu();

        mDrawingPad.setDrawingCacheEnabled(true);
        Bitmap bitmap = mDrawingPad.getDrawingCache();

        File photoFile = null;
        try{
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(editedImageFileName,".png",storageDir);
        }
        catch (IOException ex){
            //  ERRORI
        }
        // Continue only if this file was succesfully created
        if (photoFile != null)
        {
            try{
                FileOutputStream ostream = new FileOutputStream(photoFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
            } catch (Exception ex) {}
        }
    }

    protected  void set_default_measures() {
        mDrawingPad.setVisibility(View.INVISIBLE);
        mDrawingPad.setVisibility(View.INVISIBLE);
        //int targetW = mDrawingPad.getMeasuredWidth();
        //int targetH = mDrawingPad.getMeasuredHeight();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(0, 0, 0, 0);
        mDrawingPad.setLayoutParams(params);
    }

    //  Obtaining thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //myImageView.setImageBitmap(imageBitmap);
            mDrawingPad.setVisibility(View.VISIBLE);
            setPic();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //  Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //  Create the file where the photo should go
            File photoFile = null;
            StvorenaSlika = false;
            try {
                photoFile = createImageFile();
            } catch (IOException ex)
            {
                // Error occurred while creating the file
            }
            //  Continue only if the File was successfully created
            if (photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,   REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        //  Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        editedImageFileName = "PNG_" + timeStamp + "_edited_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = /*"file:" +*/ image.getAbsolutePath();
        return image;
    }

    private void setPic() {
       // Toast.makeText(this,"SLIKA PRIMLJJENA", Toast.LENGTH_LONG).show();
        StvorenaSlika = true;
        Toggle_Menu();
        Seeker();

        Bitmap tempBitmap;
        int offsetY = 0;
        int offsetX = 0;
        int rotationNeeded = 0;
        int realW = 0;
        int realH = 0;

        // Get the dimensions of the View
        int targetW = mDrawingPad.getMeasuredWidth();
        int targetH = mDrawingPad.getMeasuredHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        tempBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        try {
            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            int imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_90){
                rotationNeeded = 90;
                realW = photoH;
                realH = photoW;
            } else if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_270){
                rotationNeeded = 270;
                realW = photoH;
                realH = photoW;
            }  else if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_180){
                rotationNeeded = 180;
                realW = photoW;
                realH = photoH;
            } else {
                realW = photoW;
                realH = photoH;
            }

            float scaleWidth = ((float)targetW) / realW;
            float scaleHeight = ((float)targetH) / realH;
            float scaleFactor = Math.min(scaleWidth, scaleHeight);
            matrix.postScale(scaleFactor, scaleFactor);

            if (rotationNeeded > 0) {
                matrix.postRotate(rotationNeeded);
            }

            Bitmap bitmapToShow = Bitmap.createBitmap(tempBitmap, 0, 0, photoW, photoH, matrix, true);
            tempBitmap.recycle();

            Drawable myD = new BitmapDrawable(getResources(), bitmapToShow);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            int h = (int)(realH * scaleFactor);
            offsetY = (targetH - h) / 2;
            int w = (int)(realW * scaleFactor);
            offsetX = (targetW - w) / 2;

            params.setMargins(offsetX,offsetY,offsetX,offsetY);
            mDrawingPad.setLayoutParams(params);
            mDrawingPad.setBackground(myD);
            saveButton.setEnabled(true);
            Toast.makeText(this, "You can draw now...", Toast.LENGTH_SHORT).show();


        } catch (Exception ex) {
        }
    }

    private class SaveEditedPhotoAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            saveImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myBar.setVisibility(View.INVISIBLE);
            photoButton.setEnabled(true);
            saveButton.setEnabled(true);
            Toast.makeText(MainActivity.this, "Edited file successfully saved.", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            photoButton.setEnabled(false);
            saveButton.setEnabled(false);
            myBar.setVisibility(View.VISIBLE);
        }
    }

}
