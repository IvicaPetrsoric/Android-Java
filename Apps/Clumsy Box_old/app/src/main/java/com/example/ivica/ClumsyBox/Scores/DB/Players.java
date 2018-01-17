package com.example.ivica.ClumsyBox.Scores.DB;


public class Players {

    //public static final String TAG = "com.example.ivica.smartclicker";

    private String playername;
    private int score;
    private String dateOfPlaying;

    public Players(String playername, int score, String dateOfPlaying) {
        this.playername = playername;
        this.score = score;
        this.dateOfPlaying = dateOfPlaying;
    }

    public String get_playername() {
        return playername;
    }

    public int getScore() {
        return score;
    }

    public String getDateOfPlaying() {
        return dateOfPlaying;
    }
}