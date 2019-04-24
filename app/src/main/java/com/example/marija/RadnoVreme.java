package com.example.marija;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Usluga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RadnoVreme extends Fragment {

    int id_usluge;
    TextView radni_dan;
    TextView subota;
    TextView nedelja;
    Usluga selektovana_usluga;

    String[] radno_vreme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radnovreme,container,false);

        Button oceni = (Button) view.findViewById(R.id.cenovnik);
        oceni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        id_usluge = getArguments().getInt("ID_usluge");
        radni_dan = view.findViewById(R.id.radni_dan);
        subota = view.findViewById(R.id.subota);
        nedelja = view.findViewById(R.id.nedelja);

        Query query = FirebaseDatabase.getInstance().getReference("Usluge")
                .orderByChild("id").equalTo(id_usluge);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    selektovana_usluga = new Usluga();
                    selektovana_usluga = ds.getValue(Usluga.class);
                }
                radno_vreme = selektovana_usluga.getRadnoVreme().split("/");
                radni_dan.setText(radno_vreme[0]);
                subota.setText(radno_vreme[1]);
                nedelja.setText(radno_vreme[2]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void openDialog(){
        DialogCenovnik dialogCenovnik = new DialogCenovnik();
        Bundle args = new Bundle();
        args.putInt("id_usluge", id_usluge);
        dialogCenovnik.setArguments(args);

        dialogCenovnik.show(getFragmentManager(),"Cenovnik");

    }
}
