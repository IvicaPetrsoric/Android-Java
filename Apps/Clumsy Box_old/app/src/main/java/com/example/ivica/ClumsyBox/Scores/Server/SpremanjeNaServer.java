package com.example.ivica.ClumsyBox.Scores.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SpremanjeNaServer {

    //public static final String TAG = "com.example.ivica.smartclicker";

    public ArrayList[]  refresh() throws IOException, JSONException{
        ArrayList<String> name_server = new ArrayList<>();
        ArrayList<String> score_server = new ArrayList<>();
        ArrayList<String> date_server = new ArrayList<>();

        //  "http://sanjin.eu/plexer/rma_projekt/rma.php?tip=dohvati"   -- ne radi, ZAŠTO??
        //https://sanjin.eu/plexer/rma_projekt/rma.php?tip=
        URLConnection socket = new URL("https://sanjin.eu/plexer/rma_projekt/rma.php?tip=").openConnection();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        JSONObject obj = new JSONObject(br2.readLine());

        socket.setConnectTimeout(5000);

        if(obj.has("status")){
            System.out.println("STATUS SA SERVERA!"+obj.getString("status"));
        }

        JSONArray polje = obj.getJSONArray("igraci");

        int brojIgraca = polje.length();
        System.out.println("Broj igrca na serveru: "+ brojIgraca);

        for(int i = 0; i < brojIgraca; i++){
            JSONObject obj1 = polje.getJSONObject(i);
            name_server.add(obj1.getString("user"));
            score_server.add(obj1.getString("score"));
            date_server.add(obj1.getString("date"));
        }

        //System.out.println(name_server);
        //System.out.println(score_server);
        //System.out.println(date_server);

        return new ArrayList[] {name_server, score_server, date_server} ;
    }

    public void dodaj(String uuid, String player, String score, String date) throws IOException, JSONException {
        URLConnection socket = new URL("https://sanjin.eu/plexer/rma_projekt/rma.php?tip=dodaj&player="+player+"&score="+score+"&date="+date+"&uuid="+uuid).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        JSONObject obj = new JSONObject(br.readLine());
        System.out.println("Status: "+ obj.getString("status")); //greska, ok ... manji veci
       // Log.i(TAG,"Status: "+ obj.getString("status"));
        socket.setConnectTimeout(5000);
    }
}
