package com.example.marija;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Recenzija;
import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Recenzije extends Fragment {

   /* String[] komentari = {"Jako sam zadovoljan","Super usluga","Jako ljubazno osoblje"};
    String[] datumi ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    String[] korisnici={"Pera Peric","Mika Mikic","Ana Anic"};*/
   int [] slike = {R.drawable.user1,R.drawable.user2,R.drawable.user3};
   private List<Recenzija> lista;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;
    private UslugaDatabaseHandler uslugaDatabaseHandler;
    int idUsluge;
    int id_usluge_kliknute;
    Date currentDate;
    ListView lv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recenzije,container,false);
         lv = (ListView)view.findViewById(R.id.listViewRecenzije);
        lista = new ArrayList<>();

        id_usluge_kliknute = getArguments().getInt("ID_usluge");

        uslugaDatabaseHandler = new UslugaDatabaseHandler(getContext());
        idUsluge = uslugaDatabaseHandler.findUsluga();
        databaseHandler = new DatabaseHandler(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Recenzije");

        Query query = FirebaseDatabase.getInstance().getReference("Recenzije")
                .orderByChild("idUsluge").equalTo(idUsluge);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Recenzija r = ds.getValue(Recenzija.class);
                    lista.add(r);

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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_recenzije, null);

            TextView komentar = (TextView) convertView.findViewById(R.id.komentar);
            TextView korisnik = (TextView) convertView.findViewById(R.id.korisnik);
            TextView datum = (TextView) convertView.findViewById(R.id.datum);
            ImageView slika = (ImageView) convertView.findViewById(R.id.slikaKorisnika);

            komentar.setText(lista.get(position).getOcena() + "    "+lista.get(position).getKomentar());
            korisnik.setText(lista.get(position).getEmailKorinika());
            datum.setText(lista.get(position).getDatum());
            slika.setImageResource(slike[position]);




            return convertView;
        }
    }
}