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

public class AktivneRezervacije extends Fragment {

    String[] naslovi = {"Belle Femme Frizer","Piling lica","Cas matematike"};
    String[] termini ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aktivnerezervacije,container,false);
        ListView lv = (ListView)view.findViewById(R.id.listViewAktivne);
        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);

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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja, null);

            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            Button otkazi = (Button)convertView.findViewById(R.id.otkazi);

            naslov.setText(naslovi[position]);
            termin.setText(termini[position]);


            return convertView;
        }
    }
}
