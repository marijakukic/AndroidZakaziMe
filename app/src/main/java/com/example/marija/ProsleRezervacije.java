package com.example.marija;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProsleRezervacije extends Fragment {

    List<Rezervacija> lista = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;
    String datum;
    String vreme;
    ListView lv;
    Date datumDate;
    Date currentTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.proslerezervacije,container,false);
         lv = (ListView)view.findViewById(R.id.listViewProsle);
        lista = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Rezervacije");
        databaseHandler= new DatabaseHandler(getContext());
        //User u = new User("krr","krr","krr@","kkkkk","krr");
        User u = databaseHandler.findUser();
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
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Rezervacija r = ds.getValue(Rezervacija.class);
                    Date datumTermina=null;
                    try {
                        datumTermina= new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(r.getT().getDatum()+" "+r.getT().getVreme());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(datumTermina.compareTo(currentTime)<0) {
                        lista.add(r);
                    }


                }

                CustomAdapter customAdapter =new CustomAdapter();
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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_proslozak, null);

            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            Button oceni = (Button)convertView.findViewById(R.id.recenzija);
            oceni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

            naslov.setText(lista.get(position).getU().getNaziv());
            termin.setText(lista.get(position).getT().getDatum());

            return convertView;
        }
    }

    public void openDialog(){
        DialogOceni dialogOceni = new DialogOceni();
        dialogOceni.show(getFragmentManager(),"Recenzija");

    }



}
