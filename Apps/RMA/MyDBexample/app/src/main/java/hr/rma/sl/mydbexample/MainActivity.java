package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ListView playersListView;
    ListView trainingsListView;
    ListView financeListView;
    ArrayList<Student> students;
    ArrayList<TrainingsTrack> trainings;
    ArrayList<FinanceTrack> finances;
    EditText enterDefaultPhone;
    EditText enterDefaultEmail;
    Button workingHours;
    Button saveDefaultValues;

    StudentListAdapter mStudentListAdapter;
    TrainingsListAdapter mTrainingsListAdapter;
    FinanceListAdapter mFinancesListAdapter;

    DBhelper mDBhelper;
    DB2helper mDB2helper;
    DB3helper mDB3helper;

    private GetStudentListTask task;
    private GetTrainingListTask task2;
    private GetFinancesListTask task3;

    private DeleteStudentTask taskDelete;
    private DeleteTrainingTask taskDeleteTraining;
    private DeleteFinanceTask taskDeleteFinance;

    ImageButton button1, button2, button3, button4;

    int menuItemToHide = -1;
    int secondMenuItemToHide = -1;
    int thirdMenuItemToHide = -1;
    int fourthMenuItemToHide = -1;
    int search = 0;
    int addPlayer = 1;
    int addTraining = 2;
    int addFinance = 3;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBhelper = new DBhelper(this);
        mDB2helper = new DB2helper(this);
        mDB3helper = new DB3helper(this);

        playersListView = (ListView)findViewById(R.id.playersList);
        trainingsListView = (ListView)findViewById(R.id.listView);
        financeListView = (ListView)findViewById(R.id.financeList);
        enterDefaultPhone = (EditText)findViewById(R.id.editText2);
        enterDefaultEmail = (EditText)findViewById(R.id.editText3);
        workingHours = (Button)findViewById(R.id.button);
        saveDefaultValues = (Button)findViewById(R.id.button2);


        // Perform student list retrieval from the DB (in asynctask):
        // There is no need to do it here explicitly, as it will be called
        // within onResume!
        //updateView();

        // Handle click on PlayersList item (show details for possible update):
        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student studentToUpdate = (Student)playersListView.getItemAtPosition(position);

                if (studentToUpdate != null) {
                    Intent intent = new Intent(MainActivity.this, UpdateRecordActivity.class);
                    intent.putExtra("studentParcelableObject", studentToUpdate);
                    startActivity(intent);
                }
            }
        });

        trainingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainingsTrack trainingToUpdate = (TrainingsTrack) trainingsListView.getItemAtPosition(position);

                if (trainingToUpdate != null) {
                    //Toast.makeText(MainActivity.this, "Stisnuo si na listu i prdnuo u vjetar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UpdateTrainingRecordActivity.class);
                    intent.putExtra("trainingParcelableObject", trainingToUpdate);
                    startActivity(intent);
                }
            }
        });

        // Handle long click on PlayersList item (ask for deletion):
        playersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Student studentToDelete = (Student)parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Delete the following player:\n" +
                        studentToDelete.getName() + " " +
                        studentToDelete.getSurnname());

                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                taskDelete =
                                        new DeleteStudentTask(MainActivity.this, studentToDelete);
                                taskDelete.execute((Void) null);
                            }
                        });

                builder.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();
                return true;
            }
        });

        // Handle long click on TrainingsList item (ask for deletion):
        trainingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TrainingsTrack trainingToDelete = (TrainingsTrack) parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                String date = dateFormatter.format(trainingToDelete.getDate());
                builder.setMessage("Delete the following training:\n" +
                        "Date: " + date + "\n" +
                        "Time: " + trainingToDelete.getTtime());

                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                taskDeleteTraining =
                                        new DeleteTrainingTask(MainActivity.this, trainingToDelete);
                                taskDeleteTraining.execute((Void) null);
                            }
                        });

                builder.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();
                return true;
            }
        });


        // Handle long click on FinancesList item (ask for deletion):
        financeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final FinanceTrack financeToDelete = (FinanceTrack) parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                String date = dateFormatter.format(financeToDelete.getDate());
                builder.setMessage("Delete the following finance record:\n" +
                        date + "\n" +
                        financeToDelete.getName() + "\n" +
                        financeToDelete.getMoney() + " kn");

                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                taskDeleteFinance =
                                        new DeleteFinanceTask(MainActivity.this, financeToDelete);
                                taskDeleteFinance.execute((Void) null);
                            }
                        });

                builder.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();
                return true;
            }
        });
    }



    @Override
    public void onResume(){
        super.onResume();
        setTitle("All trainings");
        button1 = (ImageButton)findViewById(R.id.Button1);
        button2 = (ImageButton)findViewById(R.id.Button2);
        button3 = (ImageButton)findViewById(R.id.Button3);
        button4 = (ImageButton)findViewById(R.id.Button4);
        button1.setImageResource(R.drawable.checked);
        button2.setImageResource(R.drawable.unchecked);
        button3.setImageResource(R.drawable.unchecked);
        button4.setImageResource(R.drawable.unchecked);
        trainingsListView.setVisibility(View.VISIBLE);
        playersListView.setVisibility(View.INVISIBLE);
        financeListView.setVisibility(View.INVISIBLE);
        enterDefaultPhone.setVisibility(View.INVISIBLE);
        enterDefaultEmail.setVisibility(View.INVISIBLE);
        workingHours.setVisibility(View.INVISIBLE);
        saveDefaultValues.setVisibility(View.INVISIBLE);
        menuItemToHide = addPlayer;
        secondMenuItemToHide = search;
        thirdMenuItemToHide = addFinance;
        updatePlayersView();
        updateTrainingsView();
        updateFinanceView();
        System.out.println("View updated...");
    }

    // Retrieve list from the DB:
    public void updatePlayersView() {
        task = new GetStudentListTask(this);
        task.execute((Void) null);
    }

    public void updateTrainingsView() {
        task2 = new GetTrainingListTask(this);
        task2.execute((Void) null);
    }

    public void updateFinanceView() {
        task3 = new GetFinancesListTask(this);
        task3.execute((Void) null);
    }


    // Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (menuItemToHide >= 0 && secondMenuItemToHide >= 0 && thirdMenuItemToHide >= 0 && fourthMenuItemToHide < 0) {
            menu.getItem(menuItemToHide).setVisible(false);
            menu.getItem(secondMenuItemToHide).setVisible(false);
            menu.getItem(thirdMenuItemToHide).setVisible(false);
        } else if (menuItemToHide >= 0 && secondMenuItemToHide >= 0 && thirdMenuItemToHide < 0) {
            menu.getItem(menuItemToHide).setVisible(false);
            menu.getItem(secondMenuItemToHide).setVisible(false);
            //menu.getItem(thirdMenuItemToHide).setVisible(true);
        } else if (menuItemToHide >= 0 && secondMenuItemToHide < 0 && thirdMenuItemToHide >= 0) {
            menu.getItem(menuItemToHide).setVisible(false);
            //menu.getItem(secondMenuItemToHide).setVisible(true);
            menu.getItem(thirdMenuItemToHide).setVisible(false);
        } else if (menuItemToHide >= 0 && secondMenuItemToHide < 0 && thirdMenuItemToHide < 0) {
            menu.getItem(menuItemToHide).setVisible(false);
            //menu.getItem(secondMenuItemToHide).setVisible(true);
            //menu.getItem(thirdMenuItemToHide).setVisible(true);
        } else if (menuItemToHide < 0 && secondMenuItemToHide >= 0 && thirdMenuItemToHide >= 0) {
            //menu.getItem(menuItemToHide).setVisible(true);
            menu.getItem(secondMenuItemToHide).setVisible(false);
            menu.getItem(thirdMenuItemToHide).setVisible(false);
        } else if (menuItemToHide < 0 && secondMenuItemToHide >= 0 && thirdMenuItemToHide < 0) {
            //menu.getItem(menuItemToHide).setVisible(true);
            menu.getItem(secondMenuItemToHide).setVisible(false);
            //menu.getItem(thirdMenuItemToHide).setVisible(true);
        } else if (menuItemToHide < 0 && secondMenuItemToHide < 0 && thirdMenuItemToHide >= 0) {
            //menu.getItem(menuItemToHide).setVisible(true);
            //menu.getItem(secondMenuItemToHide).setVisible(true);
            menu.getItem(thirdMenuItemToHide).setVisible(false);
        } else if (menuItemToHide >= 0 && secondMenuItemToHide >= 0 && thirdMenuItemToHide >= 0 && fourthMenuItemToHide >= 0) {
            menu.getItem(menuItemToHide).setVisible(false);
            menu.getItem(secondMenuItemToHide).setVisible(false);
            menu.getItem(thirdMenuItemToHide).setVisible(false);
            menu.getItem(fourthMenuItemToHide).setVisible(false);
        } else {
            return super.onCreateOptionsMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    // Menu handling
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddRecordActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;

            case R.id.action_add_training:
                Intent trainingsIntent = new Intent(this, AddTrainingRecordActivity.class);
                startActivity(trainingsIntent);
                return true;

            case R.id.action_add_finance:
                Intent financeIntent = new Intent(this, AddFinanceRecordActivity.class);
                startActivity(financeIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    // Show dialog on/before app exit (ask for confirmation):
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setMessage("Do You Want To Quit App?");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
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
                        mStudentListAdapter = new StudentListAdapter(MainActivity.this, studentList);
                        playersListView.setAdapter(mStudentListAdapter);
                    } else {
                        //Toast.makeText(MainActivity.this, "No Student records yet...", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    // Getting training list from the DB (asynctask powered):::
    public class GetTrainingListTask extends AsyncTask<Void, Void, ArrayList<TrainingsTrack>> {
        private final WeakReference<Activity> activityWeakRef;

        public GetTrainingListTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<TrainingsTrack> doInBackground(Void... arg0) {
            ArrayList<TrainingsTrack> trainingList = mDB2helper.getTrainingsRecord();
            return trainingList;
        }

        @Override
        protected void onPostExecute(ArrayList<TrainingsTrack> trainingList) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                trainings = trainingList;
                if (trainingList != null) {
                    if (trainingList.size() != 0) {
                        mTrainingsListAdapter = new TrainingsListAdapter(MainActivity.this, trainingList);
                        trainingsListView.setAdapter(mTrainingsListAdapter);
                    } else {
                        //Toast.makeText(MainActivity.this, "No trainings record...", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    // Getting finance list from the DB (asynctask powered):::
    public class GetFinancesListTask extends AsyncTask<Void, Void, ArrayList<FinanceTrack>> {
        private final WeakReference<Activity> activityWeakRef;

        public GetFinancesListTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<FinanceTrack> doInBackground(Void... arg0) {
            ArrayList<FinanceTrack> financeList = mDB3helper.getFinanceRecords();
            return financeList;
        }

        @Override
        protected void onPostExecute(ArrayList<FinanceTrack> financeList) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                finances = financeList;
                if (financeList != null) {
                    if (financeList.size() != 0) {
                        mFinancesListAdapter = new FinanceListAdapter(MainActivity.this, financeList);
                        financeListView.setAdapter(mFinancesListAdapter);
                    } else {
                        //Toast.makeText(MainActivity.this, "No finance record...", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }


    // Delete particular record from the players DB (asynctask powered):::
    public class DeleteStudentTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;
        private Student mStudent;

        public DeleteStudentTask(Activity context, Student mStudent) {
            this.activityWeakRef = new WeakReference<Activity>(context);
            this.mStudent = mStudent;
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDBhelper.deleteRecord(mStudent);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    Toast.makeText(activityWeakRef.get(), "Player record successfully deleted",
                            Toast.LENGTH_LONG).show();
                    mStudentListAdapter.remove(mStudent);
                }

            }
        }
    }

    // Delete particular record from the Trainings DB (asynctask powered):::
    public class DeleteTrainingTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;
        private TrainingsTrack mTrack;

        public DeleteTrainingTask(Activity context, TrainingsTrack mTrack) {
            this.activityWeakRef = new WeakReference<Activity>(context);
            this.mTrack = mTrack;
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDB2helper.deleteRecord(mTrack);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    Toast.makeText(activityWeakRef.get(), "Training record successfully deleted",
                            Toast.LENGTH_LONG).show();
                    mTrainingsListAdapter.remove(mTrack);
                }

            }
        }
    }


    // Delete particular record from the Finances DB (asynctask powered):::
    public class DeleteFinanceTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;
        private FinanceTrack fTrack;

        public DeleteFinanceTask(Activity context, FinanceTrack fTrack) {
            this.activityWeakRef = new WeakReference<Activity>(context);
            this.fTrack = fTrack;
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = mDB3helper.deleteRecord(fTrack);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    Toast.makeText(activityWeakRef.get(), "Finance record successfully deleted",
                            Toast.LENGTH_LONG).show();
                    mFinancesListAdapter.remove(fTrack);
                }

            }
        }
    }

    public static class MonthYearPickerDialog extends DialogFragment {

        private static final int MAX_YEAR = 2099;
        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.custom_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDateSet(null, 0, monthPicker.getValue(), yearPicker.getValue());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearPickerDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    public void workingHours(View view) {
        MonthYearPickerDialog pd = new MonthYearPickerDialog();
        //pd.setListener(MainActivity.this);
        pd.show(getFragmentManager(), "MonthYearPickerDialog");
    }

    public void SaveDefaultValuesToSharedPreferences(View view) {
        String phoneNumber = enterDefaultPhone.getText().toString();
        String email = enterDefaultEmail.getText().toString();
        SharedPreferences prefs = getSharedPreferences("DefaultValues", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Phone", phoneNumber);
        editor.putString("Email", email);
        editor.commit();
        Toast.makeText(MainActivity.this, "Default values are successfully saved", Toast.LENGTH_SHORT).show();
    }

    private String getDefaultPhoneFromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences("DefaultValues", MODE_PRIVATE);
        String defaultPhone = prefs.getString("Phone", "phone");
        return defaultPhone;
    }

    private String getDefaultEmailFromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences("DefaultValues", MODE_PRIVATE);
        String defaultEmail = prefs.getString("Email", "email");
        return defaultEmail;
    }

    public void Button1(View view) {

        button1 = (ImageButton)findViewById(R.id.Button1);
        button2 = (ImageButton)findViewById(R.id.Button2);
        button3 = (ImageButton)findViewById(R.id.Button3);
        button4 = (ImageButton)findViewById(R.id.Button4);
        button1.setImageResource(R.drawable.checked);
        button2.setImageResource(R.drawable.unchecked);
        button3.setImageResource(R.drawable.unchecked);
        button4.setImageResource(R.drawable.unchecked);

        playersListView.setVisibility(View.INVISIBLE);
        trainingsListView.setVisibility(View.VISIBLE);
        financeListView.setVisibility(View.INVISIBLE);
        enterDefaultPhone.setVisibility(View.INVISIBLE);
        enterDefaultEmail.setVisibility(View.INVISIBLE);
        workingHours.setVisibility(View.INVISIBLE);
        saveDefaultValues.setVisibility(View.INVISIBLE);

        setTitle("All trainings");
        menuItemToHide = addPlayer;
        secondMenuItemToHide = search;
        thirdMenuItemToHide = addFinance;
        invalidateOptionsMenu();
    }

    public void Button2(View view) {

        button1 = (ImageButton)findViewById(R.id.Button1);
        button2 = (ImageButton)findViewById(R.id.Button2);
        button3 = (ImageButton)findViewById(R.id.Button3);
        button4 = (ImageButton)findViewById(R.id.Button4);
        button1.setImageResource(R.drawable.unchecked);
        button2.setImageResource(R.drawable.checked);
        button3.setImageResource(R.drawable.unchecked);
        button4.setImageResource(R.drawable.unchecked);

        playersListView.setVisibility(View.VISIBLE);
        trainingsListView.setVisibility(View.INVISIBLE);
        financeListView.setVisibility(View.INVISIBLE);
        enterDefaultPhone.setVisibility(View.INVISIBLE);
        enterDefaultEmail.setVisibility(View.INVISIBLE);
        workingHours.setVisibility(View.INVISIBLE);
        saveDefaultValues.setVisibility(View.INVISIBLE);

        setTitle("All players");
        menuItemToHide = addTraining;
        secondMenuItemToHide = -1;
        thirdMenuItemToHide = addFinance;
        invalidateOptionsMenu();
    }

    public void Button3(View view) {
        button1 = (ImageButton)findViewById(R.id.Button1);
        button2 = (ImageButton)findViewById(R.id.Button2);
        button3 = (ImageButton)findViewById(R.id.Button3);
        button4 = (ImageButton)findViewById(R.id.Button4);
        button1.setImageResource(R.drawable.unchecked);
        button2.setImageResource(R.drawable.unchecked);
        button3.setImageResource(R.drawable.checked);
        button4.setImageResource(R.drawable.unchecked);

        playersListView.setVisibility(View.INVISIBLE);
        trainingsListView.setVisibility(View.INVISIBLE);
        financeListView.setVisibility(View.VISIBLE);
        enterDefaultPhone.setVisibility(View.INVISIBLE);
        enterDefaultEmail.setVisibility(View.INVISIBLE);
        workingHours.setVisibility(View.INVISIBLE);
        saveDefaultValues.setVisibility(View.INVISIBLE);

        setTitle("Finance");
        menuItemToHide = addTraining;
        secondMenuItemToHide = addPlayer;
        thirdMenuItemToHide = search;
        fourthMenuItemToHide =  -1;
        invalidateOptionsMenu();
    }

    public void Button4(View view) {
        button1 = (ImageButton)findViewById(R.id.Button1);
        button2 = (ImageButton)findViewById(R.id.Button2);
        button3 = (ImageButton)findViewById(R.id.Button3);
        button4 = (ImageButton)findViewById(R.id.Button4);
        button1.setImageResource(R.drawable.unchecked);
        button2.setImageResource(R.drawable.unchecked);
        button3.setImageResource(R.drawable.unchecked);
        button4.setImageResource(R.drawable.checked);

        playersListView.setVisibility(View.INVISIBLE);
        trainingsListView.setVisibility(View.INVISIBLE);
        financeListView.setVisibility(View.INVISIBLE);
        enterDefaultPhone.setVisibility(View.VISIBLE);
        enterDefaultEmail.setVisibility(View.VISIBLE);
        workingHours.setVisibility(View.VISIBLE);
        saveDefaultValues.setVisibility(View.VISIBLE);

        String defaultPhone = getDefaultPhoneFromSharedPrefs();
        String defaultEmail = getDefaultEmailFromSharedPrefs();
        enterDefaultPhone.setText(defaultPhone);
        enterDefaultEmail.setText(defaultEmail);

        setTitle("Settings");
        menuItemToHide = addTraining;
        secondMenuItemToHide = addPlayer;
        thirdMenuItemToHide = search;
        fourthMenuItemToHide = addFinance;
        invalidateOptionsMenu();
    }
}
