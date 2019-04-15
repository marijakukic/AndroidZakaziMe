package com.example.marija;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DialogOceni extends AppCompatDialogFragment {
    Button btn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_oceni,null);
        btn = (Button)view.findViewById(R.id.recenzija);
        builder.setView(view).setTitle("Recenzija")
                .setNegativeButton("Otkazi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            /*URADI*/
                    }
                }).setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    /*dodati u listu komentara*/
                /*da nestane dugme*/

            }
        });

        return builder.create();

    }
}
