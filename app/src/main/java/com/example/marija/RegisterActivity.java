package com.example.marija;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;
import com.example.marija.Models.Usluga;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmedView;
    private EditText mImeView;
    private EditText mPrezimeView;
    private EditText mKorisnickoImeView;
    private View registerFormView;
    private Button btnAdd;
    private DatabaseHandler mDataBaseHelper = new DatabaseHandler(this);
    private RezervacijeDatabaseHandler rezDataBaseHelper = new RezervacijeDatabaseHandler(this);
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String entryname ;
    String entrykorname ;
    String entryMail ;
    String entryPass ;
    String entryPrezime ;
    private boolean korisnikPostoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBar();
        mEmailView = findViewById(R.id.emailRegister);
        btnAdd = findViewById(R.id.registrujSeBtn);

        mPasswordView = findViewById(R.id.password);
        mPasswordConfirmedView = findViewById(R.id.confirmedPass);
        mImeView = findViewById(R.id.ime);
        mPrezimeView = findViewById(R.id.prezime);
        mKorisnickoImeView= findViewById(R.id.korisnickoIme);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        registerFormView = findViewById(R.id.registerForm);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Korisnici");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               entryname = mImeView.getText().toString();
               entrykorname = mKorisnickoImeView.getText().toString();
               entryMail = mEmailView.getText().toString();
               entryPass = mPasswordView.getText().toString();
               entryPrezime = mPrezimeView.getText().toString();

                if (attemptLogin()) {
                    korisnikPostoji = true;
                    Query query = FirebaseDatabase.getInstance().getReference("Korisnici")
                            .orderByChild("email").equalTo(entryMail);

                    query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if( dataSnapshot.getChildrenCount() == 0) {
                                                            addUserToFirebase();
                                                            korisnikPostoji = AddData(entryname, entrykorname, entryMail, entryPass, entryPass);
                                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                        }
                                                            if(korisnikPostoji){
                                                                Toast.makeText(RegisterActivity.this,"Korisnik postoji", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                                                            }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                    }

                    //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            });




    }




    private void addUserToFirebase() {
        String entryname = mImeView.getText().toString();
        String entrykorname = mKorisnickoImeView.getText().toString();
        String entryMail = mEmailView.getText().toString();
        String entryPass = mPasswordView.getText().toString();
        String entryPrezime = mPrezimeView.getText().toString();

        if(entryMail.equals("ghh@")) {
            Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.frizerski_salon,"Partizanskih baza 2","Novi Sad","Lepota");
            Termin t = new Termin("22.04.2018.", "15:00", true);
            Rezervacija r = new Rezervacija(6, u1, t, true, "m@");
            AddRez(r.getEmailKorisnika());
            firebaseDatabase.getReference("Rezervacije").push().setValue(r);

        }/*
            Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.frizerski_salon,"Partizanskih baza 2","Novi Sad","Lepota");
            Termin t1 = new Termin("24.05.2018.","12:00",true);
            Rezervacija r1 = new Rezervacija(1,u1,t1,false,"sjj@");
            AddRez(r1.getEmailKorisnika());
            firebaseDatabase.getReference("Rezervacije").push().setValue(r1);


        }*/




        User u = new User(entryname,entrykorname,entryMail,entryPass,entryPrezime);

        firebaseDatabase.getReference("Korisnici").push().setValue(u);
        //ne postavljamo id




    }

    private boolean attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        boolean dozvoli = true;

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confimedPass = mPasswordConfirmedView.getText().toString();
        String ime = mImeView.getText().toString();
        String prezime = mPrezimeView.getText().toString();
        String korisnickoIme = mKorisnickoImeView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
            dozvoli = false;

        }

        if(TextUtils.isEmpty(ime)){
            mImeView.setError(getString(R.string.error_field_required));
            focusView = mImeView;
            cancel = true;
            dozvoli = false;

        }

        if(TextUtils.isEmpty(prezime)){
            mPrezimeView.setError(getString(R.string.error_field_required));
            focusView = mPrezimeView;
            cancel = true;
            dozvoli = false;

        }

        if(TextUtils.isEmpty(korisnickoIme)){
            mKorisnickoImeView.setError(getString(R.string.error_field_required));
            focusView = mKorisnickoImeView;
            cancel = true;
            dozvoli = false;

        }

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
            dozvoli = false;

        }else if(!isPassConfirmed(password,confimedPass)){
            mPasswordView.setError(getString(R.string.poklapanje_pass));
            focusView = mPasswordView;
            cancel = true;
            dozvoli = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
            dozvoli = false;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            dozvoli = false;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


        }

        return dozvoli;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPassConfirmed(String password, String confirmedPass){

        return password.equals(confirmedPass);
    }

    //ova metoda proverava da li korisnik sa tim korisnickimImenom postoji
    //i sa tim mailom
    //vraca false ako ne postoji
    //vraca true ako postoji
    public boolean isExsistingUser(String email){

            List<User> korisnici = new ArrayList<User>();
            korisnici = mDataBaseHelper.getAllUsers();
            for(int i=0;i<korisnici.size();i++){
                if(korisnici.get(i).getEmail().equals(email)){
                    return true;
                }
            }

            return false;
    }

    public boolean AddData(String newime,String newKorIme,String email,String pass,String prezime){
            boolean insert = false;
                if(!isExsistingUser(email)){
                     insert = mDataBaseHelper.addUser(newime, newKorIme, email, pass, prezime);
                }
            if (insert) {

                Toast.makeText(this, "Korisnik uspesno unet", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(this, "Nesto nije u redu sa bazom na telefonu", Toast.LENGTH_SHORT).show();
            }

            return false;

    }

    public boolean AddRez(String korisnik){
        boolean insert = false;

            //insert = rezDataBaseHelper.addRezervacija(korisnik);

        if (insert) {

            //Toast.makeText(this, "Rez uspesno uneta", Toast.LENGTH_SHORT).show();
            return false;
        } else {
           // Toast.makeText(this, "Nesto nije u redu sa bazom na telefonu", Toast.LENGTH_SHORT).show();
        }

        return insert;

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
