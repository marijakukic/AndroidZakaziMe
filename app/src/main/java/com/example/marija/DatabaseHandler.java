package com.example.marija;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import com.example.marija.Models.User;

import java.util.ArrayList;
import java.util.List;

// u ovoj klasi kucamo sve metode koje se ticu pristupa bazi za tabelu user
//mozda u buducnosti mozemo razdvojiti da svaki model ima svoju klasu za pristup bazi
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG ="android";
    private static final String TABLE_NAME="useriRadi";
    private static final String COL1="ID";
    private static final String COL2="Name";
    private static final String COL3="korisnickoIme";
    private static final String COL4="email";
    private static final String COL5="password";
    private static final String COL6="prezime";



    public DatabaseHandler(Context context) {
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

    public boolean addUser(String name,String korname,String email,String pass,String prezime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,korname);
        contentValues.put(COL4,email);
        contentValues.put(COL5,pass);
        contentValues.put(COL6,prezime);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }




    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();


        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                       TABLE_NAME);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.setName(cursor.getString(cursor.getColumnIndex(COL2)));
                    newUser.setKoriscnickoIme(cursor.getString(cursor.getColumnIndex(COL3)));
                    newUser.setEmail(cursor.getString(cursor.getColumnIndex(COL4)));
                    newUser.setPass(cursor.getString(cursor.getColumnIndex(COL5)));
                    newUser.setPrezime(cursor.getString(cursor.getColumnIndex(COL6)));
                    users.add(newUser);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Ne moze da se unese korisnik");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME);
    }

    public User findUser(){
        User u = new User();

        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        TABLE_NAME);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    u.setKoriscnickoIme(cursor.getString(cursor.getColumnIndex(COL3)));
                    u.setEmail(cursor.getString(cursor.getColumnIndex(COL4)));
                    u.setPass(cursor.getString(cursor.getColumnIndex(COL5)));
                    u.setPrezime(cursor.getString(cursor.getColumnIndex(COL6)));

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Vracen korisnik");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return u;
    }
}



