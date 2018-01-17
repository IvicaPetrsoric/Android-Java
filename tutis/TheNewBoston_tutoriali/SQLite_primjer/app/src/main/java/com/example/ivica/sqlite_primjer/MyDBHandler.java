package com.example.ivica.sqlite_primjer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1; // ko nesto dodajem novo u DB treba povecati verziju
    private static final String DATABASE_NAME = "products.db";    // db, data base za android
    public static final String TABLE_NAME = "products"; // ima table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "_productname";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        // housekeeping
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    // kada se stvori prvi put
    @Override
    public void onCreate(SQLiteDatabase db) {
        // ZA SVAKI ROW, red, stvara novi table
        String query = "CREATE TABLE " + TABLE_NAME + "("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT " + ");";


        db.execSQL(query);
    }

    // kada se upgrejda verzija
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // brišre table, CIJELU
        onCreate(db);
    }

    // dodaj novi red u DATA BASE
    public void addProduct(Products product){

        ContentValues values =  new ContentValues();    // podešavanje infromacija za redke
        values.put(COLUMN_PRODUCTNAME,product.get_productname());
        SQLiteDatabase db = getWritableDatabase();  // database kroz koji idemo
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    // delete product from database
    public void deleteProduct(String productName){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\"");
    }


    // Print out the database as a string
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE 1";//  svaki stupac, svaki red, PO SANJINU NE TREBA !

        // cursor point to a locatin your results
        Cursor c = db.rawQuery(query,null); // imamo pokazivac
        // pomak pokazivaca u prvi red
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex("_productname")) !=null){
                dbString += c.getString(c.getColumnIndex("_productname"));   // aple, bacon, sve redom
                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }




}
