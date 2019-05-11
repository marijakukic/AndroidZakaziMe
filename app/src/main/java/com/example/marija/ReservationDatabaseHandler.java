package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.Usluga;

import java.util.ArrayList;
import java.util.List;


public class ReservationDatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG ="android";
    private static final String TABLE_NAME="rezervacije";
    private static final String COL1="IDVESTACKI";
    private static final String COL2="emailKorisnika";
    private static final String COL3="id";
    private static final String COL4="slobodan";
    private static final String COL5="idUsluge";
    private static final String COL6="idTermina";



    public ReservationDatabaseHandler(Context context) {
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

    public boolean addTermin(String emailKorisnika,int id,boolean slobodan,int idUsluge,int idTermina){//fali slobodan ali nigde ne koristim pa sam obrisala

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,emailKorisnika);
        contentValues.put(COL3,id);
        contentValues.put(COL4,slobodan);
        contentValues.put(COL5,idUsluge);
        contentValues.put(COL6,idTermina);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }




    public List<Rezervacija> getAllRezervacija() {
        List<Rezervacija> termini = new ArrayList<>();


        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                       TABLE_NAME);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Rezervacija newUser = new Rezervacija();
                    newUser.setEmailKorisnika(cursor.getString(cursor.getColumnIndex(COL2)));
                    newUser.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL3))));
                    newUser.setAktivna(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COL4))));
                    Usluga u = new Usluga();
                    u.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL5))));
                    newUser.setU(u);
                    Termin t = new Termin();
                    t.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL6))));
                    newUser.setT(t);

                    termini.add(newUser);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Ne moze da se unese rezervacija");
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


}



