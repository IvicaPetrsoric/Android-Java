package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ListView studentsListView;
    StudentListAdapter mStudentListAdapter;
    ArrayList<Student> students;
    DBhelper mDBhelper;
    private GetStudentListTask task;
    private GetSearchListTask searchTask;

    EditText searchText;
    ImageButton searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Provide back to parent activity option:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Search players");

        mDBhelper = new DBhelper(this);
        studentsListView = (ListView)findViewById(R.id.listView2);

        searchText = (EditText)findViewById(R.id.editText);
        searchButton = (ImageButton)findViewById(R.id.imageButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_search();
                // force closing soft keyboard:
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                make_search();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }


    private void make_search(){
        if (searchText.getText().toString().equals("")){
            task = new GetStudentListTask(SearchActivity.this);
            task.execute((Void) null);
        } else {
            searchTask =
                    new GetSearchListTask(SearchActivity.this, searchText.getText().toString());
            searchTask.execute((Void) null);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        task = new GetStudentListTask(this);
        task.execute((Void) null);
        System.out.println("View (in search activity) updated...");
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
                        mStudentListAdapter =
                                new StudentListAdapter(SearchActivity.this, studentList);
                        studentsListView.setAdapter(mStudentListAdapter);
                    } else {
                        Toast.makeText(SearchActivity.this, "No Student records yet...",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }


    // Getting result of the search (keywords in name OR surname):::
    public class GetSearchListTask extends AsyncTask<Void, Void, ArrayList<Student>> {
        private final WeakReference<Activity> activityWeakRef;
        private final String keyword;

        public GetSearchListTask(Activity context, String keyword) {
            this.activityWeakRef = new WeakReference<Activity>(context);
            this.keyword = keyword;
        }

        @Override
        protected ArrayList<Student> doInBackground(Void... arg0) {
            ArrayList<Student> searchList = mDBhelper.getSearchRecords(keyword);
            return searchList;
        }

        @Override
        protected void onPostExecute(ArrayList<Student> studentList) {
            if ((activityWeakRef.get() != null) && !activityWeakRef.get().isFinishing()) {

                students = studentList;
                if (studentList != null) {
                    // Show list, no matter if it is empty!
                    mStudentListAdapter =
                            new StudentListAdapter(SearchActivity.this, studentList);
                    studentsListView.setAdapter(mStudentListAdapter);
                    if (studentList.size() == 0) {
                        Toast.makeText(SearchActivity.this, "No match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
}
