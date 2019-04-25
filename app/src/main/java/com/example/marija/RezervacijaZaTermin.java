package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Pomocna;
import com.example.marija.Models.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class RezervacijaZaTermin extends SQLiteOpenHelper {

    private static final String tabelaRezervacija="ajproradi";
    private static final String ID="ID";
    private static final String datum="datum";
    private static final String vreme="vreme";
    private static final String idRez="idRez";
    //private static final String idTermina="idTermina";


    public RezervacijaZaTermin(Context context) {
        super(context, tabelaRezervacija, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRezervacijaTable="CREATE TABLE "+tabelaRezervacija+" " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +datum+" TEXT,"+vreme+" TEXT, "+idRez+" TEXT)";
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

    public boolean addRezervacija(Pomocna p,int idrez){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        idrez++;
        Log.d("VREDNOSTTTTT",Integer.toString(idrez));
        contentValues.put(datum,p.getDatum());
        contentValues.put(vreme,p.getVreme());
        contentValues.put(idRez,idrez);
        long result = db.insert(tabelaRezervacija, null, contentValues);

        return result != -1;

    }

    public Pomocna findRez(){
        Pomocna p = new Pomocna();
        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        tabelaRezervacija);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                     p.setIdRez(Integer.parseInt(cursor.getString(cursor.getColumnIndex(idRez))));
                     p.setDatum(cursor.getString(cursor.getColumnIndex(datum)));
                     p.setVreme(cursor.getString(cursor.getColumnIndex(vreme)));
                     p.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));


                } while(cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return p;
    }




}
