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

import com.example.marija.Models.Recenzija;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Recenzije extends Fragment {

   /* String[] komentari = {"Jako sam zadovoljan","Super usluga","Jako ljubazno osoblje"};
    String[] datumi ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    String[] korisnici={"Pera Peric","Mika Mikic","Ana Anic"};*/
   private List<Recenzija> lista;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;

    Date currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recenzije,container,false);
        ListView lv = (ListView)view.findViewById(R.id.listViewRecenzije);
        lista = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Recenzije");
        currentDate = Calendar.getInstance().getTime();
        /*OVDE NAPRAVITI PRISTUP PRIVREMENOJ TABELI GDE CUVAM TU JEDNU
        ULSUGU NA KOJU JE KLIKNUTO I NAPUNIM LISTU KAO U AKTIVNIM REZ I BRISEM PRI VRACANJU NA MAIN WINDOW,
        VEROVATNO OPET NEVIDLJIVI ID NEGDE*/

        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
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

            komentar.setText(lista.get(position).getKomentar());
            korisnik.setText(lista.get(position).getEmailKorinika());
            datum.setText(currentDate.toString());



            return convertView;
        }
    }
}