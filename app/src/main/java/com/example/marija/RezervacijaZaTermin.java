package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Pomocna;
import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;

import java.util.ArrayList;
import java.util.List;

public class RezervacijaZaTermin extends SQLiteOpenHelper {

    private static final String tabelaRezervacija="novanovaa";
    private static final String ID="ID";
    private static final String datum="datum";
    private static final String vreme="vreme";
    private static final String idUsluge="idUsluge";
    private static final String slobodan = "slobodan";
    private static final String idTermin = "idTermin";


    public RezervacijaZaTermin(Context context) {
        super(context, tabelaRezervacija, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRezervacijaTable="CREATE TABLE "+tabelaRezervacija+" " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +datum+" TEXT,"+vreme+" TEXT, "+idUsluge+" TEXT, "+slobodan+"TEXT, "+idTermin+"TEXT)";
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

    public boolean addRezervacija(Termin t){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("HERE I AM",t.getDatum());
        contentValues.put(datum,t.getDatum());
        contentValues.put(vreme,t.getVreme());
        contentValues.put(idUsluge,t.getIdUsluge());
        contentValues.put(slobodan,t.isSlobodan());
        contentValues.put(idTermin,t.getId());
        long result = db.insert(tabelaRezervacija, null, contentValues);

        return result != -1;

    }

    public Termin findRez(){
      Termin t = new Termin();
        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        tabelaRezervacija);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                     t.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(idTermin))));
                     t.setDatum(cursor.getString(cursor.getColumnIndex(datum)));
                     t.setVreme(cursor.getString(cursor.getColumnIndex(vreme)));
                     t.setSlobodan(Boolean.parseBoolean(String.valueOf(cursor.getColumnIndex(slobodan))));
                     t.setIdUsluge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(idUsluge))));


                } while(cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return t;
    }




}
