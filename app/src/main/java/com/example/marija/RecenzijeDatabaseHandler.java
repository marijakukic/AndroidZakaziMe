package com.example.marija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marija.Models.Recenzija;
import com.example.marija.Models.User;

import java.util.ArrayList;
import java.util.List;


public class RecenzijeDatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG ="android";
    private static final String TABLE_NAME="recencije";
    private static final String COL1="ID";
    private static final String COL2="datum";
    private static final String COL3="emailKorisnika";
    private static final String COL4="idUsluge";
    private static final String COL5="komentar";
    private static final String COL6="ocena";



    public RecenzijeDatabaseHandler(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String createTable ="CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+" TEXT,"+COL3+" TEXT,"+COL4+" TEXT,"+COL5+" TEXT,"+COL6+" TEXT)";
            db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE_NAME);

        onCreate(db);
    }

    public boolean addRecenzija(String datum,String email,String idusluge,String komentar,String ocena){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,datum);
        contentValues.put(COL3,email);
        contentValues.put(COL4,idusluge);
        contentValues.put(COL5,komentar);
        contentValues.put(COL6,ocena);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }




    public List<Recenzija> getAllRecenzija() {
        List<Recenzija> recenzije = new ArrayList<>();


        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                       TABLE_NAME);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Recenzija newUser = new Recenzija();
                    newUser.setDatum(cursor.getString(cursor.getColumnIndex(COL2)));
                    newUser.setEmailKorinika(cursor.getString(cursor.getColumnIndex(COL3)));
                    newUser.setIdUsluge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL4))));
                    newUser.setKomentar(cursor.getString(cursor.getColumnIndex(COL5)));
                    newUser.setOcena(cursor.getString(cursor.getColumnIndex(COL6)));
                    recenzije.add(newUser);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Ne moze da se unese recenzija");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return recenzije;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME);
    }


}



