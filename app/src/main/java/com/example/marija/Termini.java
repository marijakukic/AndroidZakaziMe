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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Termini extends Fragment {

    String[] naslovi = {"Ponedeljak","Utorak","Sreda"};
    String[] termini ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    int id_usluge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.termini,container,false);
        ListView lv = (ListView)view.findViewById(R.id.listViewTermini);
        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
        id_usluge = getArguments().getInt("ID_usluge");

        return view;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return naslovi.length;
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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja_termini, null);

            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            Button zakazi = (Button)convertView.findViewById(R.id.zakazi);
            zakazi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

            naslov.setText(naslovi[position]);
            termin.setText(termini[position]);


            return convertView;
        }
    }

    public void openDialog(){

    }
}