package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateTrainingRecordActivity extends AppCompatActivity {

    EditText playersEditor;
    Button updateButton, defaultButton, dateEditor, startTimeEditor, endTimeEditor;

    String default_date, default_time, default_players, default_startTime, default_endTime;
    String listOfTrainingsFromChosenDate = "";
    int default_id;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    TrainingsTrack inTrack;
    private DB2helper mDB2helper;
    private DBhelper mDBhelper;
    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;
    private UpdateTrainingRecordTask task;

    ListView playersListView;

    private GetStudentListTask task2;
    ArrayList<Student> students;
    StudentListAdapter mStudentListAdapter;

    String players = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_training_record_layout);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Training details");

        mDB2helper = new DB2helper(this);
        mDBhelper = new DBhelper(this);

        // get Training (to be updated) as parcelable object sent through intent:
        inTrack = getIntent().getExtras().getParcelable("trainingParcelableObject");

        default_date = dateFormatter.format(inTrack.getDate());
        default_time = inTrack.getTtime();
        String[] parts = default_time.split("-");
        default_startTime = parts[0];
        default_endTime = parts[1];
        default_players = inTrack.getPlayers();
        default_id = inTrack.getId();

        playersListView = (ListView)findViewById(R.id.playersListView);
        updatePlayersView();

        dateEditor = (Button)findViewById(R.id.dateButton);
        dateEditor.setText(default_date);
        startTimeEditor = (Button)findViewById(R.id.startTimeButton);
        startTimeEditor.setText(default_startTime);
        endTimeEditor = (Button)findViewById(R.id.endTimeButton);
        endTimeEditor.setText(default_endTime);
        playersEditor = (EditText)findViewById(R.id.edittext_players);
        playersEditor.setText(default_players);

        dateEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show currently default date of birth for particular student:
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(inTrack.getDate());

                datePickerDialog = new DatePickerDialog(UpdateTrainingRecordActivity.this,
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

        // Handle click on PlayersList item:
        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student studentToAdd = (Student)playersListView.getItemAtPosition(position);

                if (studentToAdd != null) {
                    String nameOfPlayer = studentToAdd.getName();
                    String surnameOfPlayer = studentToAdd.getSurnname();
                    players = players + nameOfPlayer + ' ' + surnameOfPlayer + ',' + ' ';
                    playersEditor.setText(players);
                }
            }
        });

        //Hide soft keyboard when scrolling list view
        playersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(playersEditor.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    public void timePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Button startTimeButton = (Button) getActivity().findViewById(R.id.startTimeButton);
            Button endTimeButton = (Button) getActivity().findViewById(R.id.endTimeButton);
            String minuteToShow;
            int endHour = hourOfDay + 1;
            String endHourString = "" + endHour;

            if(minute <= 9) {
                minuteToShow = "0" + minute;
            } else minuteToShow = "" + minute;

            startTimeButton.setText(String.valueOf(hourOfDay)+ ":" + minuteToShow);
            endTimeButton.setText(endHourString+ ":" + minuteToShow);

        }
    }

    public void timePicker2(View view) {
        DialogFragment newFragment2 = new TimePickerFragment2();
        newFragment2.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment2 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Button endTimeButton = (Button) getActivity().findViewById(R.id.endTimeButton);
            String minuteToShow;

            if(minute <= 9) {
                minuteToShow = "0" + minute;
            } else minuteToShow = "" + minute;

            endTimeButton.setText(String.valueOf(hourOfDay)+ ":" + minuteToShow);

        }
    }


    private void prepareTrainingRecord() {
        String time = startTimeEditor.getText().toString() + "-" + endTimeEditor.getText().toString();
        inTrack.setTtime(time);
        String playersWithoutComa = playersEditor.getText().toString();
        playersWithoutComa = playersWithoutComa.replaceAll(", $", "");
        inTrack.setPlayers(playersWithoutComa);
                if (dateCalendar != null)
            inTrack.setDate(dateCalendar.getTime());

        System.out.println("Updated record: " + inTrack.toString());
    }


    //Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_training_record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Back support:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_undo:
                dateEditor.setText(default_date);
                String[] parts = default_date.split("-");
                String default_day = parts[0];
                String default_month = parts[1];
                String default_year = parts[2];
                int year = Integer.parseInt(default_year);
                int monthOfYear = Integer.parseInt(default_month);
                int dayOfMonth = Integer.parseInt(default_day);
                dateCalendar = Calendar.getInstance();
                dateCalendar.set(year, monthOfYear, dayOfMonth);
                startTimeEditor.setText(default_startTime);
                endTimeEditor.setText(default_endTime);
                playersEditor.setText(default_players);
                players = "";
                return true;
            case R.id.action_save:
                prepareTrainingRecord();
                task = new UpdateTrainingRecordTask(UpdateTrainingRecordActivity.this);
                task.execute((Void) null);
                return true;
            case R.id.action_sendMessage:
                sendSMS();
                return true;
            case R.id.action_sendEmail:
                sendEMAIL();
                return true;
            case R.id.action_sendMultipleTrainings:
                sendALLinEMAIL();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Getting student list from the DB (asynctask powered):::
    public class GetStudentListTask extends AsyncTask<Void, Void, ArrayList<Student>> {
        private final WeakReference<Activity> activityWeakRef;

        public GetStudentListTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Student> doInBackground(Void... arg0) {
            ArrayList<Student> studentList = mDBhelper.getStudentRecords();
            return studentList;
        }

        @Override
        protected void onPostExecute(ArrayList<Student> studentList) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                students = studentList;
                if (studentList != null) {
                    if (studentList.size() != 0) {
                        mStudentListAdapter = new StudentListAdapter(UpdateTrainingRecordActivity.this, studentList);
                        playersListView.setAdapter(mStudentListAdapter);
                    } else {
                        Toast.makeText(UpdateTrainingRecordActivity.this, "No Student records yet...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


    // Retrieve list from the DB:
    public void updatePlayersView() {
        task2 = new GetStudentListTask(this);
        task2.execute((Void) null);
    }


    // Updating training record in the DB (async task powered):::
    public class UpdateTrainingRecordTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public UpdateTrainingRecordTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDB2helper.updateRecord(inTrack);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Training record successfully updated",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDefaultPhoneFromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences("DefaultValues", MODE_PRIVATE);
        String defaultPhone = prefs.getString("Phone", "Default phone number");
        return defaultPhone;
    }

    private String getDefaultEmailFromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences("DefaultValues", MODE_PRIVATE);
        String defaultEmail = prefs.getString("Email", "Default email");
        return defaultEmail;
    }

    //Send SMS with single training record
    public void sendSMS() {
        dateEditor = (Button) findViewById(R.id.dateButton);
        startTimeEditor = (Button)findViewById(R.id.startTimeButton);
        endTimeEditor = (Button)findViewById(R.id.endTimeButton);
        playersEditor = (EditText)findViewById(R.id.edittext_players);
        String message = dateEditor.getText().toString() + "\n" +
                startTimeEditor.getText().toString() + "-" +
                endTimeEditor.getText().toString() + "\n" +
                playersEditor.getText().toString();
        String phoneNumber = getDefaultPhoneFromSharedPrefs();
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("smsto:"));
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("address" , phoneNumber);
        sendIntent.putExtra("sms_body", message);

        try {
            startActivity(sendIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(UpdateTrainingRecordActivity.this, "SMS failed, please try again later", Toast.LENGTH_LONG).show();
        }
    }


    //Send email with single training record
    public void sendEMAIL () {
        String message = dateEditor.getText().toString() + " " +
                startTimeEditor.getText().toString() + "-" +
                endTimeEditor.getText().toString() + " " +
                playersEditor.getText().toString();
        String emailAdress = getDefaultEmailFromSharedPrefs();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",emailAdress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Training record for: " + dateEditor.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    //Send email with multiple trainings records
    public void sendALLinEMAIL(){
        int id = default_id;

        ArrayList<TrainingsTrack> toSendTrainingList = mDB2helper.getTrainingsRecordFromChosenDate(id);

        for (TrainingsTrack players : toSendTrainingList) {
            listOfTrainingsFromChosenDate += dateFormatter.format(players.getDate())
                    + " " + players.getTtime()
                    + " " + players.getPlayers()
                    + "\n";
        }

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Training record from: " + default_date + " to today");
        emailIntent.putExtra(Intent.EXTRA_TEXT, listOfTrainingsFromChosenDate);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
