package hr.rma.sl.mydbexample;

/**
 * Created by Marko on 14.4.2016..
 * Database with players
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studentdb";
    private static final int DATABASE_VERSION = 2;
    public static final String STUDENT_TABLE = "student";

    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String SURNAME_COLUMN = "surname";
    public static final String DATE_COLUMN = "dateofbirth";
    public static final String RMA_COLUMN = "rmamark";
    public static final String PICTURE_COLUMN = "picture";
    public static final String MONEY_COLUMN = "money";

    public static final String CREATE_STUDENT_TABLE = "CREATE TABLE "
            + STUDENT_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + NAME_COLUMN + " TEXT, "
            + SURNAME_COLUMN + " TEXT, "
            + PICTURE_COLUMN + " TEXT, "
            + RMA_COLUMN + " INTEGER, "
            + DATE_COLUMN + " DATE" + ")";

    private static DBhelper instance;

    private static final String WHERE_ID_EQUALS = ID_COLUMN  + " =?";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");



    public static synchronized DBhelper getHelper(Context context) {
        if (instance == null)
            instance = new DBhelper(context);
        return instance;
    }

    // Constructor:
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    // Creating table(s):
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        onCreate(db);
    }


    // All CRUD(Create, Read, Update, Delete) operations
    // CREATE / INSERT
    public long insertRecord(Student studentRecord) {
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, studentRecord.getName());
        values.put(SURNAME_COLUMN, studentRecord.getSurnname());
        values.put(RMA_COLUMN, studentRecord.getRmaMark());
        values.put(PICTURE_COLUMN, studentRecord.getPicture());
        values.put(DATE_COLUMN, dateFormatter.format(studentRecord.getDateOfBirth()));

        // INSERT (CREATE)
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(STUDENT_TABLE, null, values);
    }


    // UPDATE
    public long updateRecord(Student studentRecord) {
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, studentRecord.getName());
        values.put(SURNAME_COLUMN, studentRecord.getSurnname());
        values.put(RMA_COLUMN, studentRecord.getRmaMark());
        values.put(PICTURE_COLUMN, studentRecord.getPicture());
        values.put(DATE_COLUMN, dateFormatter.format(studentRecord.getDateOfBirth()));

        // UPDATE
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(STUDENT_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(studentRecord.getId()) });
        return result;
    }


    // DELETE
    public int deleteRecord(Student studentRecord) {
        // DELETE
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(STUDENT_TABLE, WHERE_ID_EQUALS,
                new String[] { studentRecord.getId() + "" });
    }


    // READ - using rawQuery() method
    public ArrayList<Student> getStudentRecords() {
        ArrayList<Student> studentRecords = new ArrayList<Student>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + NAME_COLUMN + ","
                + SURNAME_COLUMN + ","
                + DATE_COLUMN + ","
                + RMA_COLUMN + ","
                + PICTURE_COLUMN
                + " FROM "
                + STUDENT_TABLE
                + " ORDER BY "
                + SURNAME_COLUMN
                + " ASC";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Student mStudent = new Student();
            mStudent.setId(cursor.getInt(0));
            mStudent.setName(cursor.getString(1));
            mStudent.setSurname(cursor.getString(2));
            try {
                mStudent.setDateOfBirth(dateFormatter.parse(cursor.getString(3)));
            } catch (ParseException e) {
                mStudent.setDateOfBirth(null);
            }
            mStudent.setRmaMark(cursor.getInt(4));
            mStudent.setPicture(cursor.getString(5));

            studentRecords.add(mStudent);
        }
        return studentRecords;
    }


    // RETRIEVE SINGLE student for a given id
    public Student getStudentRecord(int id) {
        Student mStudent = null;

        String sql = "SELECT * FROM "
                + STUDENT_TABLE
                + " WHERE "
                + ID_COLUMN
                + " = ?";

        // Read single
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] { id + "" });

        if (cursor.moveToNext()) {
            mStudent = new Student();
            mStudent.setId(cursor.getInt(0));
            mStudent.setName(cursor.getString(1));
            mStudent.setSurname(cursor.getString(2));
            try {
                mStudent.setDateOfBirth(dateFormatter.parse(cursor.getString(3)));
            } catch (ParseException e) {
                mStudent.setDateOfBirth(null);
            }
            mStudent.setRmaMark(cursor.getInt(4));
            mStudent.setPicture(cursor.getString(5));
        }
        return mStudent;
    }


    // SEARCH for keyword in name OR surname - using rawQuery() method
    public ArrayList<Student> getSearchRecords(String keyword) {
        ArrayList<Student> studentRecords = new ArrayList<Student>();

        String sql = "SELECT "
                + ID_COLUMN + ","
                + NAME_COLUMN + ","
                + SURNAME_COLUMN + ","
                + DATE_COLUMN + ","
                + RMA_COLUMN + ","
                + PICTURE_COLUMN
                + " FROM "
                + STUDENT_TABLE
                + " WHERE ("
                + NAME_COLUMN + " LIKE ?)"
                + " OR ("
                + SURNAME_COLUMN + " LIKE ?)";

        // READ
        SQLiteDatabase db = this.getWritableDatabase();
        String match = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(sql, new String[] { match, match });

        while (cursor.moveToNext()) {
            Student mStudent = new Student();
            mStudent.setId(cursor.getInt(0));
            mStudent.setName(cursor.getString(1));
            mStudent.setSurname(cursor.getString(2));
            try {
                mStudent.setDateOfBirth(dateFormatter.parse(cursor.getString(3)));
            } catch (ParseException e) {
                mStudent.setDateOfBirth(null);
            }
            mStudent.setRmaMark(cursor.getInt(4));
            mStudent.setPicture(cursor.getString(5));

            studentRecords.add(mStudent);
        }
        return studentRecords;
    }
}