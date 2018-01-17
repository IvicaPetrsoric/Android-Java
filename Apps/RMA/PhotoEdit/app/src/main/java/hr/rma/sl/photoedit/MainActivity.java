package hr.rma.sl.photoedit;

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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DrawingView mDrawingView=new DrawingView(this);
        setContentView(R.layout.activity_main);

        photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setTransformationMethod(null);
        saveButton = (Button) this.findViewById(R.id.button2);
        saveButton.setTransformationMethod(null);
        saveButton.setEnabled(false);
        mDrawingPad=(LinearLayout)findViewById(R.id.drawing);
        mDrawingPad.addView(mDrawingView);
        myBar = (ProgressBar) this.findViewById(R.id.progressBar);
        myBar.setVisibility(View.INVISIBLE);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveImage();
                new SaveEditedPhotoAsyncTask().execute();
            }
        });
    }


    protected void saveImage(){
        mDrawingPad.setDrawingCacheEnabled(true);
        Bitmap bitmap = mDrawingPad.getDrawingCache();

        File photoFile = null;
        try {
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(editedImageFileName, ".png", storageDir);
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            try {
                FileOutputStream ostream = new FileOutputStream(photoFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
            } catch (Exception ex) {}
        }
    }


    protected void set_default_measures(){
        mDrawingPad.setVisibility(View.INVISIBLE);
        //int targetW = mDrawingPad.getMeasuredWidth();
        //int targetH = mDrawingPad.getMeasuredHeight();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(0, 0, 0, 0);
        mDrawingPad.setLayoutParams(params);
    }


    // Obtaining thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //myImageView.setImageBitmap(imageBitmap);
            mDrawingPad.setVisibility(View.VISIBLE);
            setPic();
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        editedImageFileName = "PNG_" + timeStamp + "_edited_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = /*"file:" +*/ image.getAbsolutePath();
        return image;
    }


    private void setPic() {
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
