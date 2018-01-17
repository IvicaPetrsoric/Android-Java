package com.example.ivica.sqlite_primjer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mojInput;
    TextView mojTextView;
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mojInput = (EditText)findViewById(R.id.mojInput);
        mojTextView = (TextView)findViewById(R.id.mojText);
        dbHandler = new MyDBHandler(this, null,null,1);

        printDatabase();
    }

    // dodaj product u database
    public void addButtonClicked(View view){
        Products product = new Products(mojInput.getText().toString());
        dbHandler.addProduct(product);
        printDatabase();
    }

    // delete items
    public void deleteButtonClicked(View view){
        String inputText = mojInput.getText().toString();
        dbHandler.deleteProduct(inputText);
        printDatabase();
    }


    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        mojTextView.setText(dbString);
        mojInput.setText("");   // samo isprazni upisni kontent jer se seli dole , refresh

    }
}
