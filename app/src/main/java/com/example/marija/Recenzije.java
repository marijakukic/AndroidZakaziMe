package com.example.marija;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.marija.Models.Recenzija;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Recenzije extends Fragment {

   /* String[] komentari = {"Jako sam zadovoljan","Super usluga","Jako ljubazno osoblje"};
    String[] datumi ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    String[] korisnici={"Pera Peric","Mika Mikic","Ana Anic"};*/
   //int [] slike = {R.drawable.user1,R.drawable.user2,R.drawable.user3};
   private List<Recenzija> lista,lista_lokalna;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;
    private UslugaDatabaseHandler uslugaDatabaseHandler;
    int idUsluge;
    int id_usluge_kliknute;
    Date currentDate;
    ListView lv;
    StorageReference storageReference;
    StorageReference storageReference1;
    RecenzijeDatabaseHandler rdh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recenzije,container,false);
         lv = (ListView)view.findViewById(R.id.listViewRecenzije);
        lista = new ArrayList<>();
        lista_lokalna = new ArrayList<>();
        rdh = new RecenzijeDatabaseHandler(getContext());
        id_usluge_kliknute = getArguments().getInt("ID_usluge");

        storageReference = FirebaseStorage.getInstance().getReference("Korisnici");
        uslugaDatabaseHandler = new UslugaDatabaseHandler(getContext());
        idUsluge = uslugaDatabaseHandler.findUsluga();
        databaseHandler = new DatabaseHandler(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Recenzije");
        if(checkNet()){
           // Toast.makeText(getContext(),"IMA NETA",Toast.LENGTH_SHORT).show();

        } if(checkNet()){
            // Toast.makeText(getContext(),"IMA NETA",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getContext(),"Proverite konekciju s internetom!",Toast.LENGTH_SHORT).show();
            lista_lokalna = rdh.getAllRecenzija();
            try {
                for (Recenzija t : lista_lokalna) {
                    if (t.getIdUsluge() == id_usluge_kliknute)
                        lista_lokalna.add(t);
                }
                CustomAdapter customAdapter = new CustomAdapter();
                lv.setAdapter(customAdapter);
            }catch(Exception e){}
        }


        Query query = FirebaseDatabase.getInstance().getReference("Recenzije")
                .orderByChild("idUsluge").equalTo(idUsluge);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Recenzija r = ds.getValue(Recenzija.class);
                    lista.add(r);

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

    class CustomAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_recenzije, null);

            TextView komentar = (TextView) convertView.findViewById(R.id.komentar);
            TextView korisnik = (TextView) convertView.findViewById(R.id.korisnik);
            TextView datum = (TextView) convertView.findViewById(R.id.datum);
            ImageView slika = (ImageView) convertView.findViewById(R.id.slikaKorisnika);

            komentar.setText(lista.get(position).getOcena() + "    "+lista.get(position).getKomentar());
            korisnik.setText(lista.get(position).getEmailKorinika());
            datum.setText(lista.get(position).getDatum());

            storageReference1 = storageReference.child(lista.get(position).getEmailKorinika()+".jpg");

            GlideApp.with(getContext())
                    .load(storageReference1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(slika);




            return convertView;
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