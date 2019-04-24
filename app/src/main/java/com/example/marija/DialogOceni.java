package com.example.marija;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.marija.Models.Recenzija;
import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogOceni extends AppCompatDialogFragment {
    Button btn;
    RatingBar ratingBar;
    EditText komentarView;
    DatabaseHandler databaseHandler;
    RezervacijeDatabaseHandler rezervacijeDatabaseHandler;

    String rating;
    String komentar;
    String korisnik;
    Button dugmeZaRecenciju;
    int idUsluge;
    int idRez;
    Date currentDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_oceni,null);
        dugmeZaRecenciju=(Button)view.findViewById(R.id.recenzija);
        databaseHandler= new DatabaseHandler(getContext());
        rezervacijeDatabaseHandler= new RezervacijeDatabaseHandler(getContext());
        final User ulogovaniKorisnik = databaseHandler.findUser();
        btn = (Button)view.findViewById(R.id.recenzija);
        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        komentarView=(EditText)view.findViewById(R.id.komentar);
        builder.setView(view).setTitle("Recenzija")
                .setNegativeButton("Otkazi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            /*URADI*/
                    }
                }).setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 rating = String.valueOf(ratingBar.getRating());
                 komentar = komentarView.getText().toString();
                 korisnik = ulogovaniKorisnik.getEmail();
                idRez  = rezervacijeDatabaseHandler.findRez();
                Toast.makeText(getContext(),"REZERVACIJA"+Integer.toString(idRez),Toast.LENGTH_SHORT).show();
                Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                        .orderByChild("id").equalTo(idRez);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            Rezervacija r = ds.getValue(Rezervacija.class);
                            idUsluge = r.getU().getID();
                            Toast.makeText(getContext(),Integer.toString(idUsluge),Toast.LENGTH_SHORT).show();
                            currentDate = Calendar.getInstance().getTime();
                            SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy. HH:mm");
                            String dateString = format.format( currentDate );
                            Recenzija recenzija = new Recenzija(rating,komentar,korisnik,idUsluge,dateString);
                            FirebaseDatabase.getInstance().getReference("Recenzije").push().setValue(recenzija);
                            Toast.makeText(getContext(),"Dodali ste recenziju",Toast.LENGTH_SHORT).show();
                            rezervacijeDatabaseHandler.deleteAll();
                            //dugmeZaRecenciju.setVisibility(View.INVISIBLE);//kad jednom oceni to je to

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

        return builder.create();

    }
}
