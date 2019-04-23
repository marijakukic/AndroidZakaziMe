package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.User;

import java.util.ArrayList;
import java.util.List;

public class RezervacijeDatabaseHandler extends SQLiteOpenHelper {

    private static final String tabelaRezervacija="rezervacijaOcena";
    private static final String idKorisnika="idKorisnika";
    private static final String ID="ID";
    private static final String idRez="idRez";
    //private static final String idTermina="idTermina";


    public RezervacijeDatabaseHandler(Context context) {
        super(context, tabelaRezervacija, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRezervacijaTable="CREATE TABLE "+tabelaRezervacija+" " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+idKorisnika+" TEXT," +idRez+" TEXT)";
        db.execSQL(createRezervacijaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE "+tabelaRezervacija);

        onCreate(db);

    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+tabelaRezervacija);
    }

    public boolean addRezervacija(Rezervacija r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("REZERVACIJA",Integer.toString(r.getId()));
        contentValues.put(idKorisnika,r.getEmailKorisnika());
        contentValues.put(idRez,Integer.toString(r.getId()));
        long result = db.insert(tabelaRezervacija, null, contentValues);

        return result != -1;

    }

    public int findRez(){
        int ret = -1;

        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        tabelaRezervacija);
        Log.d("USOOOOOOOO",Integer.toString(ret));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    ret = Integer.parseInt(cursor.getString(cursor.getColumnIndex(idRez)));

                    Log.d("RADII",Integer.toString(ret));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return ret;
    }



    public List<Rezervacija> getAllRezervacija(){
        List<Rezervacija> rezervacija = new ArrayList<>();
        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        tabelaRezervacija);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Rezervacija r = new Rezervacija();
                    r.setEmailKorisnika(cursor.getString(cursor.getColumnIndex(idKorisnika)));

                    rezervacija.add(r);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return rezervacija;
    }




}
