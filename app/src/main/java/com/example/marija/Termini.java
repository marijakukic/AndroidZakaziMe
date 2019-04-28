package com.example.marija;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Pomocna;
import com.example.marija.Models.Recenzija;
import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;
import com.example.marija.Models.Usluga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Termini extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;
    private UslugaDatabaseHandler uslugaDatabaseHandler;
    User ulogovani;
    Usluga u;
    int id_usluge;
    List<Termin> listaTermina;
    List<Rezervacija> listaRez;
    ListView lv;
    Termin t = new Termin();
    int idRez;
    int idTermina;
    Rezervacija r;
    int inkrement;
    private  RezervacijaZaTermin rzt;
    Pomocna p;
    int brojac=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.termini,container,false);
        r = new Rezervacija();
        lv = (ListView)view.findViewById(R.id.listViewTermini);
        databaseReference =  FirebaseDatabase.getInstance().getReference("Termini");
        id_usluge = getArguments().getInt("ID_usluge");
        databaseHandler = new DatabaseHandler(getContext());
        rzt = new RezervacijaZaTermin(getContext());
        ulogovani = databaseHandler.findUser();
        listaTermina = new ArrayList<Termin>();
        listaRez = new ArrayList<Rezervacija>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Termini");


        Query q =  FirebaseDatabase.getInstance().getReference("Usluge")
                .orderByChild("id").equalTo(id_usluge);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    u = ds.getValue(Usluga.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = databaseReference
                .orderByChild("idUsluge").equalTo(id_usluge);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTermina.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Termin t = ds.getValue(Termin.class);
                    listaTermina.add(t);

                }
                CustomAdapter customAdapter = new CustomAdapter();
                lv.setAdapter(customAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

     class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listaTermina.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja_termini, null);

            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            Button zakazi = (Button)convertView.findViewById(R.id.zakazi);


            zakazi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idTermina = listaTermina.get(position).getId();
                     p = new Pomocna();

                    Query query = databaseReference
                            .orderByChild("id").equalTo(idTermina);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                t = ds.getValue(Termin.class);


                                    databaseReference.child(ds.getKey()).removeValue();

                                    r.setT(t);
                                    r.setU(u);
                                    r.setAktivna(true);
                                    r.setEmailKorisnika(ulogovani.getEmail());
                                    Random rr = new Random();
                                    r.setId(rr.nextInt());
                                    brojac++;

                                    if (brojac == 1) {
                                        FirebaseDatabase.getInstance().getReference("Rezervacije").push().setValue(r);

                                    }





                            }




                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }


            });



            naslov.setText(listaTermina.get(position).getDatum());
            termin.setText(listaTermina.get(position).getVreme());
            brojac = 0;


            return convertView;
        }
    }


}