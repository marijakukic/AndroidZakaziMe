package com.example.marija;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marija.Models.Usluga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DialogCenovnik extends AppCompatDialogFragment {

    int id_usluge;
    Button btn;
    Usluga selektovana_usluga;
    TextView prva;
    TextView druga;
    TextView treca;

    String[] cenovnik;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cenovnik,null);
        id_usluge = getArguments().getInt("id_usluge");
        btn = (Button)view.findViewById(R.id.cenovnik);
        prva = view.findViewById(R.id.prva);
        druga = view.findViewById(R.id.druga);
        treca = view.findViewById(R.id.treca);

        Query query = FirebaseDatabase.getInstance().getReference("Usluge")
                .orderByChild("id").equalTo(id_usluge);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    selektovana_usluga = new Usluga();
                    selektovana_usluga = ds.getValue(Usluga.class);
                }
                cenovnik = selektovana_usluga.getCenovik().split(":");
                prva.setText(cenovnik[0]);
                druga.setText(cenovnik[1]);
                treca.setText(cenovnik[2]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        builder.setView(view).setTitle("Cenovnik")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nista
            }
        });

        return builder.create();

    }
}