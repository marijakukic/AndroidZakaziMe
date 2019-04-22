package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marija.Models.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class RezervacijeDatabaseHandler extends SQLiteOpenHelper {

    private static final String tabelaRezervacija="rezervacija";
    private static final String idKorisnika="idKorisnika";
    private static final String ID="ID";

    public RezervacijeDatabaseHandler(Context context) {
        super(context, tabelaRezervacija, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRezervacijaTable="CREATE TABLE "+tabelaRezervacija+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+idKorisnika+" TEXT)";
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

    public boolean addRezervacija(String korisnik){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(idKorisnika,korisnik);
        long result = db.insert(tabelaRezervacija, null, contentValues);

        return result != -1;

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



    public List<Rezervacija> findRezervacijeForUser(String email){
        List<Rezervacija> lista = new ArrayList<>();

        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE emailKorisnika = %s;",
                        tabelaRezervacija,email);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Rezervacija r = new Rezervacija();
                    r.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                    r.setEmailKorisnika(cursor.getString(cursor.getColumnIndex(idKorisnika)));

                    lista.add(r);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return lista;


    }
}
