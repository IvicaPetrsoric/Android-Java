package com.example.ivica.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dodaj();
    }

    private static void dodaj() throws MalformedURLException, IOException {
        //&gameVersion=1.2.5
        URLConnection socket = new URL("http://sanjin.eu/plexer/dodaj.php?player=Test&score=545789&country=hr").openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        JSONObject obj = new JSONObject(br.readLine());
        System.out.println(obj.getString("status"));
    }
}
