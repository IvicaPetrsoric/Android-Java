package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity {

    EditText nameEditor, surnameEditor, markEditor;
    Button dateButton;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 3;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    String GivenName = "";
    String FamilyName = "";

    Student mStudent = null;
    private InsertRecordTask task;
    private DBhelper mDBhelper;

    private static int RESULT_LOAD_IMAGE = 1;

    ImageButton contactPhoto;
    String picturePath = "";
    String picturePath2 = "";
    Bitmap loadedBitmap;

    int REQUEST_CAMERA = 2;
    private String userChoosenTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_layout);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add new player");

        mDBhelper = new DBhelper(this);

        nameEditor = (EditText)findViewById(R.id.edittext_name);
        surnameEditor = (EditText)findViewById(R.id.edittext_surname);
        dateButton = (Button)findViewById(R.id.edittext_date);
        dateButton.setTransformationMethod(null);
        markEditor = (EditText)findViewById(R.id.edittext_rmamark);

        contactPhoto = (ImageButton)findViewById(R.id.contactPhoto);



        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateCalendar = Calendar.getInstance();
                                dateCalendar.set(year, monthOfYear, dayOfMonth);
                                dateButton.setText(dateFormatter.format(dateCalendar.getTime()));
                            }

                        },
                        newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        /*contactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });*/
    }

    /*public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if(reqCode == 1) {
                contactPhoto.setImageURI(data.getData());
            }
        }
    }*/


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("MypicturePath", picturePath);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        picturePath = savedInstanceState.getString("MypicturePath");
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


    //Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_record_menu, menu);
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
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
                //Intent intent = new Intent(this, ContactListActivity.class);
                //startActivity(intent);
                return true;
            case R.id.action_save:
                if (nameEditor.getText().length() == 0 || surnameEditor.getText().length() == 0 || dateButton.getText().length() == 0 || markEditor.getText().length() == 0) {
                    Toast.makeText(AddRecordActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
                } else {
                    prepareStudentRecord();
                    task = new InsertRecordTask(AddRecordActivity.this);
                    task.execute((Void) null);
                    return true;
                }
            case R.id.action_cancel:
                resetAllFields();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void prepareStudentRecord() {
        mStudent = new Student();
        mStudent.setName(nameEditor.getText().toString());
        mStudent.setSurname(surnameEditor.getText().toString());
        mStudent.setPicture(picturePath);
        mStudent.setRmaMark(Integer.parseInt(markEditor.getText().toString()));
        if (dateCalendar != null)
            mStudent.setDateOfBirth(dateCalendar.getTime());
    }


    protected void resetAllFields() {
        nameEditor.setText("");
        surnameEditor.setText("");
        dateButton.setText("");
        markEditor.setText("");
        contactPhoto.setImageResource(R.drawable.ic_contact_picture_holo_light);
    }


    // Storing student record in the DB (async task powered):::
    public class InsertRecordTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public InsertRecordTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDBhelper.insertRecord(mStudent);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Player record successfully saved",
                            Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    //Add contact photo method

    public void addPhoto(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddRecordActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddRecordActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),RESULT_LOAD_IMAGE);
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            //retrieveContactPhoto();
            retrieveContactName();

        } else if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE && null != data) {
            selectFromGallery(data);
        }
        else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            onCaptureImageResult(data);
        }
    }


    private void retrieveContactName() {
        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);
        String[] parts = contactName.split(" ");
        int n = parts.length;
        if (n==1) {
            GivenName = parts[0];
        } else if (n==2) {
            GivenName = parts[0];
            FamilyName = parts[1];
        } else if (n==3) {
            GivenName = parts[0];
            String famName1 = parts[1];
            String famName2 = parts[2];
            FamilyName = famName1 + " " + famName2;
        }
        nameEditor.setText(GivenName);
        surnameEditor.setText(FamilyName);
    }


    private void retrieveContactPhoto() {
        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                //ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                contactPhoto.setImageBitmap(photo);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void selectFromGallery(Intent data) {

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


    private void onCaptureImageResult(Intent data) {

        Uri takenPicture = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(takenPicture, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();

        /*ExifInterface exif = null;
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
        contactPhoto.setImageBitmap(loadedBitmap);*/

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        assert thumbnail != null;
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        contactPhoto.setImageBitmap(thumbnail);
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}