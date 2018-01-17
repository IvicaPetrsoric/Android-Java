package com.example.ivica.customdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.login_customdialog_layout_2);
                dialog.setTitle("AAAAAAAAAAAA");

                dialog.show();

                final EditText editText = (EditText)dialog.findViewById(R.id.editText_pin);
                Button subButton = (Button)dialog.findViewById(R.id.button2);
                Button cancelButton = (Button)dialog.findViewById(R.id.button);

                subButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String text = editText.getText().toString();

                        Toast.makeText(getApplicationContext(),"pin Subbmited is : "+text,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

               /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);

                builder.setMessage("Do you want to buy this world?");
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

                builder.create().show();*/
            }
        });
    }
}
