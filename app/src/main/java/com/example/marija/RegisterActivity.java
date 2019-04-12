package com.example.marija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailRegister);
        btnAdd = (Button)findViewById(R.id.registrujSeBtn);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfirmedView = (EditText) findViewById(R.id.confirmedPass);
        mImeView = (EditText)findViewById(R.id.ime) ;
        mPrezimeView =(EditText)findViewById(R.id.prezime);
        mKorisnickoImeView=(EditText)findViewById(R.id.korisnickoIme);
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entryname = mImeView.getText().toString();
                String entrykorname=mKorisnickoImeView.getText().toString();
                String entryMail = mEmailView.getText().toString();
                String entryPass = mPasswordView.getText().toString();
                AddData(entryname,entrykorname,entryMail,entryPass);
            }
        });

       // Button mEmailSignInButton = (Button) findViewById(R.id.registrujSeBtn);
       /* mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });*/

        registerFormView = findViewById(R.id.registerForm);
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

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
        }

        if(TextUtils.isEmpty(ime)){
            mImeView.setError(getString(R.string.error_field_required));
            focusView = mImeView;
            cancel = true;

        }

        if(TextUtils.isEmpty(prezime)){
            mPrezimeView.setError(getString(R.string.error_field_required));
            focusView = mPrezimeView;
            cancel = true;

        }

        if(TextUtils.isEmpty(korisnickoIme)){
            mKorisnickoImeView.setError(getString(R.string.error_field_required));
            focusView = mKorisnickoImeView;
            cancel = true;

        }

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        }else if(!isPassConfirmed(password,confimedPass)){
            mPasswordView.setError(getString(R.string.poklapanje_pass));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
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

        if(password.equals(confirmedPass)){
            return true;
        }else{
            return false;
        }
    }

    //ova metoda proverava da li korisnik sa tim korisnickimImenom postoji
    //i sa tim mailom
    //vraca false ako ne postoji
    //vraca true ako postoji
    public boolean isExsistingUser(String newKorIme){

            List<User> korisnici = new ArrayList<User>();
            korisnici = mDataBaseHelper.getAllUsers();
            for(int i=0;i<korisnici.size();i++){
                if(korisnici.get(i).getKoriscnickoIme().equals(newKorIme)){
                    return true;
                }
            }

            return false;
    }

    public void AddData(String newime,String newKorIme,String email,String pass){
        if(isExsistingUser(newKorIme)){

            Toast.makeText(this,"korisnik postoji",Toast.LENGTH_SHORT).show();

        }else {
            boolean insert = mDataBaseHelper.addUser(newime, newKorIme,email,pass);

            if (insert) {

                Toast.makeText(this, "Korisnik uspesno unet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nesto nije u redu sa bazom", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
