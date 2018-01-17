package hr.rma.sl.navigationdrawergoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

public class ImageShowActivity extends Activity {

    Bitmap tempBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        ImageView myIV = (ImageView)findViewById(R.id.imageViewFull);

        Intent intent_in = getIntent();
        Bundle myBundle = intent_in.getExtras();

        if(myBundle != null)
        {
            String imageID = (String)myBundle.get("IMAGE_LOCATION");
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageID);
            System.out.println("uri: " + uri.toString());

            // We have URI, but filepath is more convenient:
            String imagePath = getRealPathFromURI(this, uri);
            System.out.println("uri-path: " + imagePath);

            // Now we can rotate image according to source orientation (from PhotoEdit project):
            int rotationNeeded = 0;

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            tempBitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            try {
                ExifInterface exif = new ExifInterface(imagePath);
                int imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                System.out.println("orientation: " + imageOrientation);

                Matrix matrix = new Matrix();
                if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_90){
                    rotationNeeded = 90;
                } else if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_270){
                    rotationNeeded = 270;
                }  else if (imageOrientation == ExifInterface.ORIENTATION_ROTATE_180){
                    rotationNeeded = 180;
                } else {
                    // do nothing
                }

                if (rotationNeeded > 0) {
                    matrix.postRotate(rotationNeeded);
                }

                //Bitmap bitmapToShow = Bitmap.createBitmap(tempBitmap, 0, 0, photoW, photoH, matrix, true);
                tempBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, photoW, photoH, matrix, true);

            } catch (Exception ex) {
                // rotation error
            }

            myIV.setImageBitmap(tempBitmap);
        }
    }


    // Get image path for a given URI:
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (tempBitmap != null && !tempBitmap.isRecycled()) {
            tempBitmap.recycle();
            tempBitmap = null;
        }
        finish();
        return;
    }


}
