package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTrainingRecordActivity extends AppCompatActivity {

    EditText playersEditor;
    Button startTimeButton, endTimeButton, dateButton;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;

    TrainingsTrack mTrack = null;
    private InsertRecordTask task;
    private DB2helper mDB2helper;
    DBhelper mDBhelper;

    ListView playersListView;
    ArrayList<Student> students;
    StudentListAdapter mStudentListAdapter;

    private GetStudentListTask task2;

    String players = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_training_record_layout);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add new training");

        mDB2helper = new DB2helper(this);
        mDBhelper = new DBhelper(this);

        playersListView = (ListView)findViewById(R.id.playersListView);
        updatePlayersView();

        dateButton = (Button)findViewById(R.id.dateButton);
        playersEditor = (EditText)findViewById(R.id.edittext_players);
        startTimeButton = (Button)findViewById(R.id.startTimeButton);
        endTimeButton = (Button)findViewById(R.id.endTimeButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddTrainingRecordActivity.this,
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

    // Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_training_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Menu handling:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.add_training_2:
                if (dateButton.getText().equals("Date") || startTimeButton.getText().equals("Start time") || playersEditor.getText().length() == 0) {
                    Toast.makeText(AddTrainingRecordActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
                } else {
                    prepareTrainingsRecord();
                    task = new InsertRecordTask(AddTrainingRecordActivity.this);
                    task.execute((Void) null);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareTrainingsRecord() {
        mTrack = new TrainingsTrack();
        String time = startTimeButton.getText().toString() + "-" + endTimeButton.getText().toString();
        mTrack.setTtime(time);
        String playersWithoutComa = playersEditor.getText().toString();
        playersWithoutComa = playersWithoutComa.replaceAll(", $", "");
        mTrack.setPlayers(playersWithoutComa);
        if (dateCalendar != null)
            mTrack.setDate(dateCalendar.getTime());
    }


    protected void resetAllFields() {
        dateButton.setText("");
        startTimeButton.setText("");
        playersEditor.setText("");
    }

    // Getting players list from the DB (asynctask powered):::
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
                        mStudentListAdapter = new StudentListAdapter(AddTrainingRecordActivity.this, studentList);
                        playersListView.setAdapter(mStudentListAdapter);
                    } else {
                        Toast.makeText(AddTrainingRecordActivity.this, "No players records yet...", Toast.LENGTH_LONG).show();
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


    // Storing training record in the DB (async task powered):::
    public class InsertRecordTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public InsertRecordTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDB2helper.insertRecord(mTrack);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Training record successfully saved",
                            Toast.LENGTH_LONG).show();
            }
        }
    }
}
