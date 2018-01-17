package com.example.ivica.ClumsyBox.Scores.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class MyDBHandler extends SQLiteOpenHelper {

    //public static final String TAG = "com.example.ivica.smartclicker";

    private static final int DATABASE_VERSION = 2; // ko nesto dodajem novo u DB treba povecati verziju
    private static final String DATABASE_NAME = "players.db";    // db, data base za android
    public static final String PLAYER_TABLE = "players"; // ima table

    public static final String ID_COLUMN = "id";
    public static final String PLAYERNAME_COLUMN = "playername";
    public static final String SCORE_COLUMN = "playerscore";
    public static final String DATE_COLUMN = "dateplayed";

    public static final String CREATE_PLAYER_TABLE = "CREATE TABLE "
            + PLAYER_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + PLAYERNAME_COLUMN + " TEXT, "
            + SCORE_COLUMN + " INTEGER, "
            + DATE_COLUMN + " TEXT" + ")";


    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        // housekeeping
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // kada se stvori prvi put
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAYER_TABLE);
    }

    // kada se upgrejda verzija
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE); // brišre table, CIJELU
        onCreate(db);
    }

    // dodaj novi red u DATA BASE
    public void addPlayer(Players player){
        ContentValues values =  new ContentValues();    // podešavanje infromacija za redke
        values.put(PLAYERNAME_COLUMN,player.get_playername());
        values.put(SCORE_COLUMN, player.getScore());
        values.put(DATE_COLUMN, (player.getDateOfPlaying()));

        SQLiteDatabase db = getWritableDatabase();  // database kroz koji idemo
        db.insert(PLAYER_TABLE,null,values);
        db.close();
    }

    // Print out the database as a string
    public ArrayList[] databaseToString(){
        ArrayList<String> dbString_name = new ArrayList<>();
        ArrayList<Integer> dbString_score = new ArrayList<>();
        ArrayList<String> dbString_datePlayed = new ArrayList<>();

        String sql = "SELECT "
                + PLAYERNAME_COLUMN + ","
                + SCORE_COLUMN + ","
                + DATE_COLUMN
                + " FROM "
                + PLAYER_TABLE
                + " ORDER BY "
                + SCORE_COLUMN
                + " DESC";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            dbString_name.add(cursor.getString(0));
            dbString_score.add(cursor.getInt(1));
            dbString_datePlayed.add(cursor.getString(2));
        }
        cursor.close();
        db.close();

        return new ArrayList[] {dbString_name, dbString_score, dbString_datePlayed} ;
    }

    public int highestDbScore(){
        int highScore = 0;

        String sql = "SELECT "
                + PLAYERNAME_COLUMN + ","
                + SCORE_COLUMN + ","
                + SCORE_COLUMN
                + " FROM "
                + PLAYER_TABLE
                + " ORDER BY "
                + SCORE_COLUMN
                + " DESC LIMIT 1 ";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        //highScore = cursor.getInt(1); // ne radi ??,  ograničeno s limitom DB

        while (cursor.moveToNext()) {
            highScore = (cursor.getInt(1));
        }

        cursor.close();
        db.close();

        return highScore;
    }
}




