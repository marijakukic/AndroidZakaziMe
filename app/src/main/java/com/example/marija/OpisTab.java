package com.example.marija;

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Usluga;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OpisTab extends Fragment {

    private static final String TAG = "OpisFragment";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    TextView nazivUsluge;
    TextView adresaUsluge;
    TextView nacinPlacanjaUsluge;
    TextView nevidljiviIDusluge;
    ImageView slikaUsluge;
    int id_usluge;
    Usluga selektovana_usluga;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.opis,container,false);

        id_usluge = getArguments().getInt("ID_usluge");
        nazivUsluge = view.findViewById(R.id.naslovUsluge);
        adresaUsluge = view.findViewById(R.id.adresaUsluge);
        nacinPlacanjaUsluge = view.findViewById(R.id.nacinPlacanjaUsluge);
        slikaUsluge = view.findViewById(R.id.slikaUsluge);


        Query query = FirebaseDatabase.getInstance().getReference("Usluge")
                .orderByChild("id").equalTo(id_usluge);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    selektovana_usluga = new Usluga();
                    selektovana_usluga = ds.getValue(Usluga.class);
                }
                nazivUsluge.setText(selektovana_usluga.getNaziv());
                adresaUsluge.setText(selektovana_usluga.getAdresa());
                nacinPlacanjaUsluge.setText(selektovana_usluga.getNacinPlacanja());
               // slikaUsluge.setImageDrawable(R.drawable.);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(isServicesOK()){
            Button btnMap = (Button) view.findViewById(R.id.lokacijaDugme);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }


    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }




}
