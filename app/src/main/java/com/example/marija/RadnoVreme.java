package com.example.marija;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RadnoVreme extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radnovreme,container,false);

        Button oceni = (Button) view.findViewById(R.id.cenovnik);
        oceni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        return view;
    }

    public void openDialog(){
        DialogCenovnik dialogCenovnik = new DialogCenovnik();
        dialogCenovnik.show(getFragmentManager(),"Cenovnik");

    }
}
