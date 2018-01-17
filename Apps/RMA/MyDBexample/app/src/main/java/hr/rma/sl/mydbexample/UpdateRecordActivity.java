package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateRecordActivity extends AppCompatActivity {

    EditText nameEditor, surnameEditor, dateEditor, markEditor;
    Button updateButton, defaultButton;
    ImageView contactPhoto;

    String default_name, default_surname, default_date, default_mark, default_picture;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    Student inStudent;
    private DBhelper mDBhelper;
    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;
    private UpdateRecordTask task;


    private static int RESULT_LOAD_IMAGE = 1;
    Bitmap loadedBitmap;
    String picturePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_record_layout);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Edit player details");

        mDBhelper = new DBhelper(this);

        // get Student (to be updated) as parcelable object sent through intent:
        inStudent = getIntent().getExtras().getParcelable("studentParcelableObject");

        default_name = inStudent.getName();
        default_surname = inStudent.getSurnname();
        default_mark = Integer.toString(inStudent.getRmaMark());
        default_date = dateFormatter.format(inStudent.getDateOfBirth());
        default_picture = inStudent.getPicture();
        picturePath = default_picture;

        nameEditor = (EditText)findViewById(R.id.edittext_name);
        nameEditor.setText(default_name);
        surnameEditor = (EditText)findViewById(R.id.edittext_surname);
        surnameEditor.setText(default_surname);
        dateEditor = (EditText)findViewById(R.id.edittext_date);
        dateEditor.setText(default_date);
        markEditor = (EditText)findViewById(R.id.edittext_rmamark);
        markEditor.setText(default_mark);

        //Defaul Photo
        if (picturePath.length() == 0) {
            contactPhoto = (ImageView) findViewById(R.id.imageContact);
            contactPhoto.setImageResource(R.drawable.ic_contact_picture_holo_light);
        } else {
            contactPhoto = (ImageView) findViewById(R.id.imageContact);
            ExifInterface exif = null;
            try {
                File pictureFile = new File(default_picture);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
                //Get thumbnail of picture from gallery
                byte[] imageData = exif.getThumbnail();

                try{
                    loadedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                }
                catch (Exception e){
                    loadedBitmap = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            contactPhoto.setImageBitmap(loadedBitmap);
        }

        dateEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show currently default date of birth for particular student:
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(inStudent.getDateOfBirth());

                datePickerDialog = new DatePickerDialog(UpdateRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateCalendar = Calendar.getInstance();
                                dateCalendar.set(year, monthOfYear, dayOfMonth);
                                dateEditor.setText(dateFormatter.format(dateCalendar.getTime()));
                            }

                        },
                        newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Back support:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_read_contacts:
                Intent intent = new Intent(this, ContactListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_save:
                prepareStudentRecord();
                task = new UpdateRecordTask(UpdateRecordActivity.this);
                task.execute((Void) null);
                return true;
            case R.id.action_undo:
                backToDefault();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void backToDefault() {
        nameEditor.setText(default_name);
        surnameEditor.setText(default_surname);
        dateEditor.setText(default_date);
        markEditor.setText(default_mark);
        picturePath = default_picture;

        ExifInterface exif = null;
        try {
            File pictureFile = new File(default_picture);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
            //Get thumbnail of picture from gallery
            byte[] imageData = exif.getThumbnail();
            loadedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                loadedBitmap = rotateBitmap(loadedBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                loadedBitmap = rotateBitmap(loadedBitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                loadedBitmap = rotateBitmap(loadedBitmap, 270);
                break;
        }
        contactPhoto.setImageBitmap(loadedBitmap);
    }


    private void prepareStudentRecord() {
        inStudent.setName(nameEditor.getText().toString());
        inStudent.setSurname(surnameEditor.getText().toString());
        inStudent.setRmaMark(Integer.parseInt(markEditor.getText().toString()));
        inStudent.setPicture(picturePath);
        if (dateCalendar != null)
            inStudent.setDateOfBirth(dateCalendar.getTime());

        System.out.println("Updated record: " + inStudent.toString());
    }


    // Updating student record in the DB (async task powered):::
    public class UpdateRecordTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public UpdateRecordTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDBhelper.updateRecord(inStudent);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Player record successfully updated",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    public void changePhoto(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE && null != data) {

            // Get selected gallery image
            Uri selectedPicture = data.getData();
            // Get and resize profile image
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
                //Get thumbnail of picture from gallery
                byte[] imageData = exif.getThumbnail();
                loadedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            contactPhoto.setImageBitmap(loadedBitmap);
        }
    }
}
