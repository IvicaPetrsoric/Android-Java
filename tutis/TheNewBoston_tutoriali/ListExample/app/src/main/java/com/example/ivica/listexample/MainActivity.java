package com.example.ivica.listexample;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] foods = {"Bacon","Ham","Tuna","Banana","Meatball","Potato","Karim","111","222","333","444","555","asdasd"};    // preko adaptera za konverziju java koda u list item
        ListAdapter mojAdapter = new CustomAdapter(this,foods);



      //  ListAdapter mojAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);
        ListView mojListView = (ListView)findViewById(R.id.buckListView);
        mojListView.setAdapter(mojAdapter);

        mojListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String food = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this,food,Toast.LENGTH_SHORT).show();
            }
        });


    }





}
