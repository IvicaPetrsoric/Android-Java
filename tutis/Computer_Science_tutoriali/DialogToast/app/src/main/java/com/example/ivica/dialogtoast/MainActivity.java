package com.example.ivica.dialogtoast;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button toastButton;
    private Button alertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toastButton = (Button) findViewById(R.id.show_toast);
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast toast = Toast.makeText(getApplicationContext(),"TOSTERRRR",Toast.LENGTH_SHORT);
                toast.show();
               // Toast.makeText(MainActivity.this,"Birana je crvena boja", Toast.LENGTH_LONG).show();

            }
        });


        alertButton = (Button) findViewById(R.id.show_alert);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
                builder.setMessage("ALLLLEEERTT")
                        .setTitle("Dialog alert ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}
