package hr.rma.sl.mydbexample;

/*
* database with trainings
*
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DB2helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trackingdb";
    private static final int DATABASE_VERSION = 1;
    public static final String TRAININGS_TRACK_TABLE = "track";

    public static final String ID_COLUMN = "id";
    public static final String DATE_COLUMN = "date";
    public static final String TIME_COLUMN = "time";
    public static final String PLAYERS_COLUMN = "players";

    public static final String CREATE_TRAININGS_TRACK_TABLE = "CREATE TABLE "
            + TRAININGS_TRACK_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + DATE_COLUMN + " TEXT, "
            + TIME_COLUMN + " TEXT, "
            + PLAYERS_COLUMN + " TEXT" + ")";

    private static DB2helper instance;

    private static final String WHERE_ID_EQUALS = ID_COLUMN  + " =?";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");



    public static synchronized DB2helper getHelper(Context context) {
        if (instance == null)
            instance = new DB2helper(context);
        return instance;
    }

    // Constructor:
    public DB2helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    // Creating table(s):
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRAININGS_TRACK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRAININGS_TRACK_TABLE);
        onCreate(db);
    }


    // All CRUD(Create, Read, Update, Delete) operations
    // CREATE / INSERT
    public long insertRecord(TrainingsTrack trainingsRecord) {
        ContentValues values = new ContentValues();
        values.put(DATE_COLUMN, dateFormatter.format(trainingsRecord.getDate()));
        values.put(TIME_COLUMN, trainingsRecord.getTtime());
        values.put(PLAYERS_COLUMN, trainingsRecord.getPlayers());

        // INSERT (CREATE)
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TRAININGS_TRACK_TABLE, null, values);
    }


    // UPDATE
    public long updateRecord(TrainingsTrack trainingsRecord) {
        ContentValues values = new ContentValues();
        values.put(DATE_COLUMN, dateFormatter.format(trainingsRecord.getDate()));
        values.put(TIME_COLUMN, trainingsRecord.getTtime());
        values.put(PLAYERS_COLUMN, trainingsRecord.getPlayers());

        // UPDATE
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(TRAININGS_TRACK_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(trainingsRecord.getId()) });
        return result;
    }


    // DELETE
    public int deleteRecord(TrainingsTrack trainingsRecord) {
        // DELETE
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TRAININGS_TRACK_TABLE, WHERE_ID_EQUALS,
                new String[] { trainingsRecord.getId() + "" });
    }


    // READ - using rawQuery() method
    public ArrayList<TrainingsTrack> getTrainingsRecord() {
        ArrayList<TrainingsTrack> trainingsRecords = new ArrayList<TrainingsTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + TIME_COLUMN + ","
                + PLAYERS_COLUMN
                + " FROM "
                + TRAININGS_TRACK_TABLE
                + " ORDER BY "
                + ID_COLUMN
                + " DESC";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            TrainingsTrack mTrack = new TrainingsTrack();
            mTrack.setId(cursor.getInt(0));
            try {
                mTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                mTrack.setDate(null);
            }
            mTrack.setTtime(cursor.getString(2));
            mTrack.setPlayers(cursor.getString(3));

            trainingsRecords.add(mTrack);
        }
        return trainingsRecords;
    }


    //READ - only choosen dates

    public ArrayList<TrainingsTrack> getTrainingsRecordFromChosenDate(int id) {
        ArrayList<TrainingsTrack> trainingsRecords = new ArrayList<TrainingsTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + TIME_COLUMN + ","
                + PLAYERS_COLUMN
                + " FROM "
                + TRAININGS_TRACK_TABLE
                + " WHERE "
                + ID_COLUMN
                + " >= "
                + id
                + " ORDER BY "
                + ID_COLUMN
                + " DESC";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            TrainingsTrack mTrack = new TrainingsTrack();
            mTrack.setId(cursor.getInt(0));
            try {
                mTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                mTrack.setDate(null);
            }
            mTrack.setTtime(cursor.getString(2));
            mTrack.setPlayers(cursor.getString(3));

            trainingsRecords.add(mTrack);
        }
        return trainingsRecords;
    }




    // RETRIEVE SINGLE student for a given id
    public TrainingsTrack getTrainingRecord(int id) {
        TrainingsTrack mTrack = null;

        String sql = "SELECT * FROM "
                + TRAININGS_TRACK_TABLE
                + " WHERE "
                + ID_COLUMN
                + " = ?";

        // Read single
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            mTrack = new TrainingsTrack();
            mTrack.setId(cursor.getInt(0));
            try {
                mTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                mTrack.setDate(null);
            }
            mTrack.setTtime(cursor.getString(2));
            mTrack.setPlayers(cursor.getString(3));
        }
        return mTrack;
    }


    // SEARCH for keyword in name OR surname - using rawQuery() method
    public ArrayList<TrainingsTrack> getSearchRecords(String keyword) {
        ArrayList<TrainingsTrack> trainingsRecords = new ArrayList<TrainingsTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + TIME_COLUMN + ","
                + PLAYERS_COLUMN
                + " FROM "
                + TRAININGS_TRACK_TABLE
                + " WHERE ("
                + DATE_COLUMN + " LIKE ?)";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        String match = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(sql, new String[] { match, match });

        while (cursor.moveToNext()) {
            TrainingsTrack mTrack = new TrainingsTrack();
            mTrack.setId(cursor.getInt(0));
            try {
                mTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                mTrack.setDate(null);
            }
            mTrack.setTtime(cursor.getString(2));
            mTrack.setPlayers(cursor.getString(3));

            trainingsRecords.add(mTrack);
        }
        return trainingsRecords;
    }
}
