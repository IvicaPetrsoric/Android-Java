package com.example.ivica.mojadb;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView studentsListView;
    ArrayList<Student> students;

    StudentListAdapter mStudentListAdapter;
    DBhelper mDBhelper;

    private GetStudentListTask task;
    private DeleteStudentTaks taskDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBhelper = new DBhelper(this);
        studentsListView = (ListView)findViewById(R.id.listView);
        // Perform student list retrieval from the DB (in asynctask):
        // There is no need to do it here explicitly, as it will be called
        // within onResume!
        //updateView();

        // Handle click on list item (show details for possible update):
        studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Student studentToUpdate = (Student)studentsListView.getItemAtPosition(position);

                if(studentToUpdate != null){
                    Intent intent = new Intent(MainActivity.this,UpdateRecordActivity.class);
                    intent.putExtra("studentParcelableObject",studentToUpdate);
                    startActivity(intent);
                }
            }
        });

        // Handle long click on list item (ask for deletion):
        studentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id ){
                final Student studentToDelete = (Student)parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);   // onemoguci izlaz iz alerta klikom bilo gdje
                builder.setMessage("Delete trhe following DB record:\n" +
                        studentToDelete.getId() +   " -- " +
                        studentToDelete.getName() + " " +
                        studentToDelete.getSurname());

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
    }























}
