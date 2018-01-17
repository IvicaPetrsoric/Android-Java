package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Marko on 23.5.2016..
 */
public class AddFinanceRecordActivity extends AppCompatActivity {

    DB3helper mDB3helper;

    Button dateButton;
    EditText nameEditor, moneyEditor;
    ListView playersList;

    DatePickerDialog datePickerDialog;

    Calendar dateCalendar;

    FinanceTrack fTrack = null;

    DBhelper mDBhelper;

    ArrayList<Student> students;

    StudentListAdapter mStudentListAdapter;

    private InsertRecordTask task;
    private GetStudentListTask task2;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_finance_record_layout);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("New finance record");

        mDB3helper = new DB3helper(this);
        mDBhelper = new DBhelper(this);

        dateButton = (Button)findViewById(R.id.date_button);
        nameEditor = (EditText)findViewById(R.id.name_txt);
        moneyEditor = (EditText) findViewById(R.id.money_txt);
        playersList = (ListView)findViewById(R.id.listView3);
        updatePlayersView();

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddFinanceRecordActivity.this,
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
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student studentToAdd = (Student)playersList.getItemAtPosition(position);

                if (studentToAdd != null) {
                    String nameOfPlayer = studentToAdd.getName();
                    String surnameOfPlayer = studentToAdd.getSurnname();
                    nameEditor.setText(nameOfPlayer + " " + surnameOfPlayer);
                }
            }
        });

        //Hide soft keyboard when scrolling list view
        playersList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameEditor.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    // Storing finance record in the DB (async task powered):::
    public class InsertRecordTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public InsertRecordTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDB3helper.insertRecord(fTrack);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Finance record successfully saved",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    // Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_finance_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Menu handling:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.save_finance:
                if (nameEditor.getText().length() == 0 || moneyEditor.getText().length() == 0 || dateButton.getText().equals("Date")) {
                    Toast.makeText(AddFinanceRecordActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
                } else {
                    prepareFinanceRecord();
                    task = new InsertRecordTask(AddFinanceRecordActivity.this);
                    task.execute((Void) null);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareFinanceRecord() {
        fTrack = new FinanceTrack();
        fTrack.setName(nameEditor.getText().toString());
        int money = Integer.parseInt(moneyEditor.getText().toString());
        fTrack.setMoney(money);
        if (dateCalendar != null)
            fTrack.setDate(dateCalendar.getTime());
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
                        mStudentListAdapter = new StudentListAdapter(AddFinanceRecordActivity.this, studentList);
                        playersList.setAdapter(mStudentListAdapter);
                    } else {
                        Toast.makeText(AddFinanceRecordActivity.this, "No players records yet...", Toast.LENGTH_LONG).show();
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
}

