package com.example.marija;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

//TO DO : ostalo je da se doda poredjenje s vremenom to moramo videti sa bazom kako cemo
//bolje mozda da cuvamo odma tu i vreme u jednom polju da ne razdvajamo
public class AktivneRezervacije extends Fragment {



    List<Rezervacija> lista,lista_lokalna;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseHandler databaseHandler;

    String datum;
    String vreme;
    ListView lv;
    Date datumDate;
    Termin t;
    Date currentTime;
    boolean upisan = false;
    ReservationDatabaseHandler rdh;
    TerminiDatabaseHandler tdh;
    Button otkazi;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aktivnerezervacije,container,false);


        lista = new ArrayList<>();
        lista_lokalna = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Rezervacije");
        rdh = new ReservationDatabaseHandler(getContext());
        tdh = new TerminiDatabaseHandler(getContext());
        lv = (ListView)view.findViewById(R.id.listViewAktivne);
        if(checkNet()){
            //Toast.makeText(getContext(),"IMA NETA",Toast.LENGTH_SHORT).show();


        }else{
            //Toast.makeText(getContext(),"NEMA NETA aktivne",Toast.LENGTH_SHORT).show();
            lista_lokalna = rdh.getAllRezervacija();
            currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy. HH:mm");
            String dateString = format.format( currentTime );
            for (Rezervacija r :lista_lokalna) {
                Date datumTermina=null;
                try {
                    Termin t = tdh.findTerminById(r.getT().getId());
                    datumTermina= new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(t.getDatum()+" "+t.getVreme());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(datumTermina.compareTo(currentTime)>0) {

                    lista.add(r);
                }
            }
            CustomAdapter customAdapter = new CustomAdapter();
            lv.setAdapter(customAdapter);

        }



       User u = databaseHandler.findUser();
        Toast.makeText(getContext(),u.getEmail(),Toast.LENGTH_SHORT).show();
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy. HH:mm");
        String dateString = format.format( currentTime );
        String [] datumivreme = dateString.split(" ");
         datum = datumivreme[0];
         vreme = datumivreme[1];



             try {
                 datumDate = new SimpleDateFormat("dd.MM.yyyy.").parse(datum);
             } catch (ParseException e) {
                 e.printStackTrace();
             }


        Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                .orderByChild("emailKorisnika").equalTo(u.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Rezervacija r = ds.getValue(Rezervacija.class);
                    Date datumTermina=null;
                    try {
                        datumTermina= new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(r.getT().getDatum()+" "+r.getT().getVreme());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(datumTermina.compareTo(currentTime)>0) {

                        lista.add(r);
                    }


                }

                CustomAdapter customAdapter = new CustomAdapter();
                lv.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }



    public User findLoggedUser(){
        User u = databaseHandler.findUser();
        return u;
    }


    class CustomAdapter extends BaseAdapter {

        private static final String TAG = "AktivneRFragment";

        private static final int ERROR_DIALOG_REQUEST = 9001;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja, null);
            final TextView nevidljiviID = (TextView) convertView.findViewById(R.id.nevidljivi);
            TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
            TextView termin = (TextView) convertView.findViewById(R.id.termin);
            TextView sati=(TextView)convertView.findViewById(R.id.sati);
            otkazi = (Button)convertView.findViewById(R.id.otkazi);
            if(checkNet())
                otkazi.setVisibility(View.VISIBLE);
            else
                otkazi.setVisibility(View.INVISIBLE);
//            Date datumTermina=null;
//           // Date currTime2 = null;
//            try {
//                datumTermina= new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(lista.get(position).getT().getDatum() + " " + lista.get(position).getT().getVreme());
//               // currTime2 = new SimpleDateFormat("dd.MM.yyyy. HH:mm").parse(currentTime.toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Termin t = lista.get(position).getT();
//            String[] splitovanje = t.getVreme().split(":");
//
//            int satii = Integer.parseInt(splitovanje[0]);
//            int min = Integer.parseInt(splitovanje[1]);
//
//            String[] datumSplit = t.getDatum().split("\\.");
//
//            int dan = Integer.parseInt(datumSplit[0]);
//            int mesec= Integer.parseInt(datumSplit[1]);
//            int godina=Integer.parseInt(datumSplit[2]);
//            if() {
//                otkazi.setVisibility(View.INVISIBLE);
//            }
//            else
//                otkazi.setVisibility(View.VISIBLE);

            naslov.setText(lista.get(position).getU().getNaziv());
            termin.setText(lista.get(position).getT().getDatum());
            sati.setText(lista.get(position).getT().getVreme());
            if(isServicesOK()) {
                Button lokacija = convertView.findViewById(R.id.vidi_lokaciju);
                lokacija.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        intent.putExtra("id_usluge", lista.get(position).getU().getID());
                        startActivity(intent);
                    }
                });

            }

            nevidljiviID.setText(Integer.toString(lista.get(position).getId()));
            otkazi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                            .orderByChild("id").equalTo(Integer.parseInt(nevidljiviID.getText().toString()));

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Rezervacija r = new Rezervacija();
                                r = ds.getValue(Rezervacija.class);

                                t = r.getT();




                                    databaseReference.child(ds.getKey()).removeValue();
                                FirebaseDatabase.getInstance().getReference("Termini").push().setValue(t);



                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            return convertView;
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

    public boolean checkNet(){
        boolean have_WIFI = false;
        boolean have_mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo networkInfo: networkInfos){
            if(networkInfo.getTypeName().equalsIgnoreCase("WIFI")){
                if(networkInfo.isConnected()){
                    have_WIFI = true;
                }
            }
            if(networkInfo.getTypeName().equalsIgnoreCase("MOBILE")){
                if(networkInfo.isConnected()){
                    have_mobile = true;
                }
            }
        }

        return have_mobile || have_WIFI;
    }







}
