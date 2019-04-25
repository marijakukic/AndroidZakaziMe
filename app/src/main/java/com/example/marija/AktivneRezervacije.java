package com.example.marija;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

//TO DO : ostalo je da se doda poredjenje s vremenom to moramo videti sa bazom kako cemo
//bolje mozda da cuvamo odma tu i vreme u jednom polju da ne razdvajamo
public class AktivneRezervacije extends Fragment {



    List<Rezervacija> lista = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;
    private RezervacijeDatabaseHandler rezdatabaseHandler;
    String datum;
    String vreme;
    ListView lv;
    Date datumDate;
    Termin t;
    Date currentTime;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aktivnerezervacije,container,false);


        lista = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getContext());
        rezdatabaseHandler = new RezervacijeDatabaseHandler(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Rezervacije");
       lv = (ListView)view.findViewById(R.id.listViewAktivne);
       User u = databaseHandler.findUser();
        Toast.makeText(getContext(),u.getEmail(),Toast.LENGTH_SHORT).show();
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy. HH:mm");
        String dateString = format.format( currentTime );
        String [] datumivreme = dateString.split(" ");
         datum = datumivreme[0];
         vreme = datumivreme[1];



             try {
                 datumDate = new SimpleDateFormat("dd.MM.yyyy.").parse(datum);
             } catch (ParseException e) {
                 e.printStackTrace();
             }


        Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                .orderByChild("emailKorisnika").equalTo(u.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Rezervacija r = ds.getValue(Rezervacija.class);
                    Date datumTermina=null;
                    try {
                        datumTermina= new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(r.getT().getDatum()+" "+r.getT().getVreme());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(datumTermina.compareTo(currentTime)>0) {

                        lista.add(r);
                    }


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

    public User findLoggedUser(){
        User u = databaseHandler.findUser();
        return u;
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return lista.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja, null);
            final TextView nevidljiviID = (TextView) convertView.findViewById(R.id.nevidljivi);
            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            TextView sati=(TextView)convertView.findViewById(R.id.sati);
            Button otkazi = (Button)convertView.findViewById(R.id.otkazi);
            naslov.setText(lista.get(position).getU().getNaziv());
            termin.setText(lista.get(position).getT().getDatum());
            sati.setText(lista.get(position).getT().getVreme());
            nevidljiviID.setText(Integer.toString(lista.get(position).getId()));
            otkazi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ovo ce trebati da ide iz lokalne baze
                       // Rezervacija r = rezdatabaseHandler.findOneReservation(Integer.parseInt(nevidljiviID.getText().toString()));
                    Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                            .orderByChild("id").equalTo(Integer.parseInt(nevidljiviID.getText().toString()));

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Rezervacija r = new Rezervacija();
                                r = ds.getValue(Rezervacija.class);

                               // t = r.getT();
                               // dodajUFirebase(t);//ovo ne doda dont know why


                                FirebaseDatabase.getInstance().getReference("Rezervacije").child(ds.getKey()).removeValue();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });






            return convertView;
        }
    }

    public void dodajUFirebase(Termin t){

            firebaseDatabase.getReference("Termini").push().setValue(t);
    }
}
