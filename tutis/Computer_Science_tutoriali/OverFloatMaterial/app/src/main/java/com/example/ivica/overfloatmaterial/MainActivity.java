package com.example.ivica.overfloatmaterial;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch (id){
            case R.id.menu_red:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);

                //getActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#F44336")));
                Toast.makeText(this,"ActionBar u crveno",Toast.LENGTH_SHORT).show();
                getWindow().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#F44336")));
                return true;
            case R.id.menu_blue:
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);

                Toast.makeText(this,"ActionBar u plavo",Toast.LENGTH_SHORT).show();
                getWindow().setBackgroundDrawable( new ColorDrawable(Color.rgb(20,30,170)));
               // getActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#F03A9F4")));
              //  getWindow().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#F03A9F4")));

                return true;
            case R.id.menu_green:
                Toast.makeText(this,"ActionBar u zeleno",Toast.LENGTH_SHORT).show();
                if (item.isChecked()) item.setChecked(false);
                else  item.setChecked(true);

               // getActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#FCAF50")));
                getWindow().setBackgroundDrawable( new ColorDrawable(Color.rgb(0,200,70)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
