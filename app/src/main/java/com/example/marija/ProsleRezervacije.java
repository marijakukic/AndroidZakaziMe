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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class ProsleRezervacije extends Fragment {

    String[] naslovi = {"Masaza","Cas srpskog"};
    String[] termini ={"13.03.2019. 12:00","14.03.2019. 13:00"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.proslerezervacije,container,false);
        ListView lv = (ListView)view.findViewById(R.id.listViewProsle);
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
            naslov.setText(naslovi[position]);
            termin.setText(termini[position]);


            return convertView;
        }
    }

    public void openDialog(){
        DialogOceni dialogOceni = new DialogOceni();
        dialogOceni.show(getFragmentManager(),"Recenzija");

    }



}
