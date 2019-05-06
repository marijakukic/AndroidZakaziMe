package com.example.marija;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;
import com.example.marija.Models.Usluga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

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
    private Button btnAdd,btnChoose;
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
    private StorageReference mStorageRef;
    private ImageView profilna;
    public Uri imguri;
    private StorageTask uploadTask;
    StorageReference riversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBar();
        if(checkNet()){
            Toast.makeText(this,"IMA NETA",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"NEMA NETA",Toast.LENGTH_SHORT).show();
        }
        mEmailView = findViewById(R.id.emailRegister);

        btnAdd = findViewById(R.id.registrujSeBtn);

        mStorageRef = FirebaseStorage.getInstance().getReference("Korisnici");
        profilna = findViewById(R.id.profilnaSlika);
        btnChoose = findViewById(R.id.izaberiSliku);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });

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
                                                            User novi = addUserToFirebase();
                                                            korisnikPostoji = AddData(entryname, entrykorname, entryMail, entryPass, entryPass);
                                                            if(uploadTask != null && uploadTask.isInProgress()) {
                                                                Toast.makeText(RegisterActivity.this,"Registracija je u toku!",Toast.LENGTH_SHORT).show();
                                                            } else {
                    // mora neka validacijaa!
                                                                Fileuploader(novi.getEmail());
                                                            }
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

                }
            });

    }
    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }


    private void Fileuploader(String email) {
        if (Uri.EMPTY == imguri || imguri == null) {

            imguri = Uri.parse(getURLForResource(R.drawable.defaultuser));
            profilna.setImageURI(imguri);
            riversRef = mStorageRef.child(email+".jpg");
        } else {
            riversRef = mStorageRef.child(email+"."+getExtention(imguri));
        }

        uploadTask = riversRef.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(RegisterActivity.this,"Slika je uploadovana uspesno!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private String getExtention(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));


    }

    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imguri=data.getData();
            profilna.setImageURI(imguri);
        }
    }

    private User addUserToFirebase() {
        String entryname = mImeView.getText().toString();
        String entrykorname = mKorisnickoImeView.getText().toString();
        String entryMail = mEmailView.getText().toString();
        String entryPass = mPasswordView.getText().toString();
        String entryPrezime = mPrezimeView.getText().toString();

        if(entryMail.equals("pera@")) {
            Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.cool_pic,"Najpovoljnije sisanje u gradu","Novi Sad","Lepota",
                    "Partizanskih baza 2 Novi Sad","Feniranje 100 din","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                    "Gotovina,kartica");
            Termin t = new Termin("22.04.2018.", "15:00", true);
            Rezervacija r = new Rezervacija(0, u1, t, true, "m@");
            AddRez(r.getEmailKorisnika());

            Rezervacija r2 = new Rezervacija(1,new Usluga(0,"Belle Femme Frizer",R.drawable.cool_pic,"Najpovoljnije sisanje u gradu","Novi Sad","Lepota",
                    "Partizanskih baza 2 Novi Sad","Feniranje 100 din","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                    "Gotovina,kartica"),new Termin("29.05.2019.","10:00",false),true,"m@");
            firebaseDatabase.getReference("Rezervacije").push().setValue(r);
            firebaseDatabase.getReference("Rezervacije").push().setValue(r2);

        }/*
            Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.frizerski_salon,"Partizanskih baza 2","Novi Sad","Lepota");
            Termin t1 = new Termin("24.05.2018.","12:00",true);
            Rezervacija r1 = new Rezervacija(1,u1,t1,false,"sjj@");
            AddRez(r1.getEmailKorisnika());
            firebaseDatabase.getReference("Rezervacije").push().setValue(r1);


        }*/


        //DODATI DA SE DODAJE SLIKA ZA KORISNIKA

        User u = new User(entryname,entrykorname,entryMail,entryPass,entryPrezime);

        firebaseDatabase.getReference("Korisnici").push().setValue(u);
        //ne postavljamo id

        return u;


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

    public boolean checkNet(){
        boolean have_WIFI = false;
        boolean have_mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo networkInfo: networkInfos){
            if(networkInfo.getTypeName().equalsIgnoreCase("WIFI")){
                if(networkInfo.isConnected()){
                    have_WIFI = true;
                }
            }
            if(networkInfo.getTypeName().equalsIgnoreCase("MOBILE")){
                if(networkInfo.isConnected()){
                    have_mobile = true;
                }
            }
        }

        return have_mobile || have_mobile;
    }






}
