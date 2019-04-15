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

import org.w3c.dom.Text;

public class Recenzije extends Fragment {

    String[] komentari = {"Jako sam zadovoljan","Super usluga","Jako ljubazno osoblje"};
    String[] datumi ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    String[] korisnici={"Pera Peric","Mika Mikic","Ana Anic"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recenzije,container,false);
        ListView lv = (ListView)view.findViewById(R.id.listViewRecenzije);
        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
        return view;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return komentari.length;
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

            komentar.setText(komentari[position]);
            korisnik.setText(korisnici[position]);
            datum.setText(datumi[position]);

            return convertView;
        }
    }
}