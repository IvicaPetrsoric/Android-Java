package hr.rma.sl.mydbexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Marko on 23.5.2016..
 * database with finance
 */
public class DB3helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FinanceDB";
    private static final int DATABASE_VERSION = 1;
    public static final String FINANCE_TRACK_TABLE = "financeTrack";

    public static final String ID_COLUMN = "id";
    public static final String DATE_COLUMN = "date";
    public static final String NAME_COLUMN = "name";
    public static final String MONEY_COLUMN = "money";

    public static final String CREATE_FINANCE_TRACK_TABLE = "CREATE TABLE "
            + FINANCE_TRACK_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + DATE_COLUMN + " TEXT, "
            + NAME_COLUMN + " TEXT, "
            + MONEY_COLUMN + " INTEGER" + ")";

    private static DB3helper instance;

    private static final String WHERE_ID_EQUALS = ID_COLUMN  + " =?";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");



    public static synchronized DB3helper getHelper(Context context) {
        if (instance == null)
            instance = new DB3helper(context);
        return instance;
    }

    // Constructor:
    public DB3helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    // Creating table(s):
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FINANCE_TRACK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FINANCE_TRACK_TABLE);
        onCreate(db);
    }


    // All CRUD(Create, Read, Update, Delete) operations
    // CREATE / INSERT
    public long insertRecord(FinanceTrack financeRecord) {
        ContentValues values = new ContentValues();
        values.put(DATE_COLUMN, dateFormatter.format(financeRecord.getDate()));
        values.put(NAME_COLUMN, financeRecord.getName());
        values.put(MONEY_COLUMN, financeRecord.getMoney());

        // INSERT (CREATE)
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(FINANCE_TRACK_TABLE, null, values);
    }


    // UPDATE
    public long updateRecord(FinanceTrack financeRecord) {
        ContentValues values = new ContentValues();
        values.put(DATE_COLUMN, dateFormatter.format(financeRecord.getDate()));
        values.put(NAME_COLUMN, financeRecord.getName());
        values.put(MONEY_COLUMN, financeRecord.getMoney());

        // UPDATE
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(FINANCE_TRACK_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(financeRecord.getId()) });
        return result;
    }


    // DELETE
    public int deleteRecord(FinanceTrack financeRecord) {
        // DELETE
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FINANCE_TRACK_TABLE, WHERE_ID_EQUALS,
                new String[] { financeRecord.getId() + "" });
    }


    // READ - using rawQuery() method
    public ArrayList<FinanceTrack> getFinanceRecords() {
        ArrayList<FinanceTrack> financeRecords = new ArrayList<FinanceTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + NAME_COLUMN + ","
                + MONEY_COLUMN
                + " FROM "
                + FINANCE_TRACK_TABLE
                + " ORDER BY "
                + ID_COLUMN
                + " DESC";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            FinanceTrack fTrack = new FinanceTrack();
            fTrack.setId(cursor.getInt(0));
            try {
                fTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                fTrack.setDate(null);
            }
            fTrack.setName(cursor.getString(2));
            fTrack.setMoney(cursor.getInt(3));

            financeRecords.add(fTrack);
        }
        return financeRecords;
    }


    //READ - only choosen dates

    public ArrayList<FinanceTrack> getFinanceRecordFromChosenDate(int id) {
        ArrayList<FinanceTrack> financeRecord = new ArrayList<FinanceTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + NAME_COLUMN + ","
                + MONEY_COLUMN
                + " FROM "
                + FINANCE_TRACK_TABLE
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
            FinanceTrack fTrack = new FinanceTrack();
            fTrack.setId(cursor.getInt(0));
            try {
                fTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                fTrack.setDate(null);
            }
            fTrack.setName(cursor.getString(2));
            fTrack.setMoney(cursor.getInt(3));

            financeRecord.add(fTrack);
        }
        return financeRecord;
    }




    // RETRIEVE SINGLE student for a given id
    public FinanceTrack getFinanceRecord(int id) {
        FinanceTrack fTrack = null;

        String sql = "SELECT * FROM "
                + FINANCE_TRACK_TABLE
                + " WHERE "
                + ID_COLUMN
                + " = ?";

        // Read single
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            fTrack = new FinanceTrack();
            fTrack.setId(cursor.getInt(0));
            try {
                fTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                fTrack.setDate(null);
            }
            fTrack.setName(cursor.getString(2));
            fTrack.setMoney(cursor.getInt(3));
        }
        return fTrack;
    }


    // SEARCH for keyword in name OR surname - using rawQuery() method
    public ArrayList<FinanceTrack> getSearchRecords(String keyword) {
        ArrayList<FinanceTrack> financeRecords = new ArrayList<FinanceTrack>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + DATE_COLUMN + ","
                + NAME_COLUMN + ","
                + MONEY_COLUMN
                + " FROM "
                + FINANCE_TRACK_TABLE
                + " WHERE ("
                + DATE_COLUMN + " LIKE ?)";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        String match = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(sql, new String[] { match, match });

        while (cursor.moveToNext()) {
            FinanceTrack fTrack = new FinanceTrack();
            fTrack.setId(cursor.getInt(0));
            try {
                fTrack.setDate(dateFormatter.parse(cursor.getString(1)));
            } catch (ParseException e) {
                fTrack.setDate(null);
            }
            fTrack.setName(cursor.getString(2));
            fTrack.setMoney(cursor.getInt(3));

            financeRecords.add(fTrack);
        }
        return financeRecords;
    }
}

