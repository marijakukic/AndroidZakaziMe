package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Termin;

import java.util.ArrayList;
import java.util.List;


public class TerminiDatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG ="android";
    private static final String TABLE_NAME="termini";
    private static final String COL1="IDVESTACKI";
    private static final String COL2="datum";
    private static final String COL3="id";
    private static final String COL4="idUsluge";
    private static final String COL5="slobodan";
    private static final String COL6="vreme";



    public TerminiDatabaseHandler(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String createTable ="CREATE TABLE "+TABLE_NAME+" (IDVESTACKI INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+" TEXT,"+COL3+" TEXT,"+COL4+" TEXT,"+COL5+" TEXT,"+COL6+" TEXT)";
            db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE_NAME);

        onCreate(db);
    }

    public boolean addTermin(String datum,String id,String idUsluge,String vreme){//fali slobodan ali nigde ne koristim pa sam obrisala

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,datum);
        contentValues.put(COL3,id);
        contentValues.put(COL4,idUsluge);
        contentValues.put(COL6,vreme);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }




    public List<Termin> getAllTermini() {
        List<Termin> termini = new ArrayList<>();


        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                       TABLE_NAME);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Termin newUser = new Termin();
                    newUser.setDatum(cursor.getString(cursor.getColumnIndex(COL2)));
                    newUser.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL3))));
                    newUser.setIdUsluge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL4))));

                    newUser.setVreme(cursor.getString(cursor.getColumnIndex(COL6)));
                    termini.add(newUser);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Ne moze da se unese termin");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return termini;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME);
    }

    public Termin findTerminById(int id){
        Termin t = new Termin();
        String id1= String.valueOf(id);
        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE id = %s;",
                        TABLE_NAME,id1);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
                if (cursor.moveToFirst()) {
                do {

                    t.setDatum(cursor.getString(cursor.getColumnIndex(COL2)));
                    t.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL3))));
                    t.setIdUsluge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL4))));

                    t.setVreme(cursor.getString(cursor.getColumnIndex(COL6)));

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Ne moze da se unese termin");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return t;


    }


}



