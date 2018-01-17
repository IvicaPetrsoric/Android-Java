package com.example.ivica.clumsybox.Scores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Ivica on 2.5.2017..
 */

public class Server {

    // Local
    ArrayList<Integer> local_playerPosition = new ArrayList<>();
    ArrayList<String> local_playerFlag = new ArrayList<>();
    ArrayList<String> local_playerName = new ArrayList<>();
    ArrayList<Integer> local_playerScore = new ArrayList<>();
    int local_totalPlayers = 50;
    int local_playerPos = 5000;

    // global
    ArrayList<Integer> global_playerPosition = new ArrayList<>();
    ArrayList<String> global_playerFlag = new ArrayList<>();
    ArrayList<String> global_playerName = new ArrayList<>();
    ArrayList<Integer> global_playerScore = new ArrayList<>();

    int global_totalPlayers = 2;
    int global_playerPos = 2000;

    public Server(){
        test_populate();
    }

    // testiranje popunjavanja listi
    private void test_populate(){
        local_playerPosition.add(1);
        local_playerPosition.add(2);
        local_playerPosition.add(3);

        local_playerFlag.add("HR");
        local_playerFlag.add("HR");
        local_playerFlag.add("HR");

        local_playerName.add("miro");
        local_playerName.add("miro");
        local_playerName.add("miro");

        local_playerScore.add(1000);
        local_playerScore.add(500);
        local_playerScore.add(500);
    }

    public void getScores() throws IOException, JSONException {
        //https://sanjin.eu/plexer/rma_projekt/rma.php?tip=
        URLConnection socket = new URL("https://sanjin.eu/plexer/rma_projekt/rma.php?tip=").openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        JSONObject obj = new JSONObject(bufferedReader.readLine());
        socket.setConnectTimeout(5000);

        if(obj.has("status")){
            System.out.println("STATUS SA SERVERA!"+obj.getString("status"));
        }

        JSONArray polje = obj.getJSONArray("igraci");

        int brojIgraca = polje.length();
        System.out.println("Broj igrca na serveru: "+ brojIgraca);

        for(int i = 0; i < brojIgraca; i++){
            JSONObject obj1 = polje.getJSONObject(i);

            //playerNames.add(obj1.getString("user"));
            //playerScores.add(obj1.getInt("score"));

            //playerPositions.add(obj1.getInt("position"));
            //playerFlags.add(obj1.getString("flag"));
        }

        /*
        System.out.println("Positions: " + playerPositions);
        System.out.println("Flags: " + playerFlags);
        System.out.println("Names: " + playerNames);
        System.out.println("Scores" + playerScores);
        */


        //return new ArrayList[] {playerPositions, playerFlags, playerNames, playerScores} ;
    }


    public void postScore(String uuid, String player, String score, String date) throws IOException, JSONException {
        URLConnection socket = new URL("https://sanjin.eu/plexer/rma_projekt/rma.php?tip=dodaj&player="+player+"&score="+score+"&date="+date+"&uuid="+uuid).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        JSONObject obj = new JSONObject(br.readLine());
        System.out.println("Status: "+ obj.getString("status")); //greska, ok ... manji veci
        // Log.i(TAG,"Status: "+ obj.getString("status"));
        socket.setConnectTimeout(5000);
    }


}
