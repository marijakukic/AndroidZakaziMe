package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Usluga;

import java.util.ArrayList;
import java.util.List;

public class UslugaDatabaseHandler extends SQLiteOpenHelper {

    private static final String tabelaRezervacija="kliknutaUslugaaa";

    private static final String ID="ID";
    private static final String idUsluge="idUsluge";


    public UslugaDatabaseHandler(Context context) {
        super(context, tabelaRezervacija, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRezervacijaTable="CREATE TABLE "+tabelaRezervacija+" " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+idUsluge+" TEXT)";
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

    public boolean addUsluga(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("USLUGA",Integer.toString(id));
        contentValues.put(idUsluge,id);
        long result = db.insert(tabelaRezervacija, null, contentValues);

        return result != -1;

    }

    public int findUsluga(){
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

                    ret = Integer.parseInt(cursor.getString(cursor.getColumnIndex(idUsluge)));

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







}
