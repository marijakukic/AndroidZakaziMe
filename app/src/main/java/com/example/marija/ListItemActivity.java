package com.example.marija;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marija.Models.Usluga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ListItemActivity extends AppCompatActivity {

    private SectionPageAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    int ID_usluge;
    Bundle bundle;
    private UslugaDatabaseHandler uslugaDatabaseHandler = new UslugaDatabaseHandler(this);
    ImageView slika;
    Usluga selektovana_usluga;
    private BroadcastReceiver MyReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        MyReceiver = new MyReceiver();
        broadcastIntent();

        sectionsPagerAdapter= new SectionPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.container);
        setViewPager(viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(checkNet()){
           // Toast.makeText(this,"IMA NETA",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"NEMA NETA",Toast.LENGTH_SHORT).show();
        }



        if(bundle!=null)
        {
            ID_usluge = bundle.getInt("ID_usluge");
        }
        Query query = FirebaseDatabase.getInstance().getReference("Usluge")
                .orderByChild("id").equalTo(ID_usluge);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    selektovana_usluga = new Usluga();
                    selektovana_usluga = ds.getValue(Usluga.class);
                }

                setupActionBar(selektovana_usluga.getNaziv());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public boolean checkNet(){
        boolean have_WIFI = false;
        boolean have_mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
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

    private void setViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

        Intent iin= getIntent();
        bundle  = iin.getExtras();

        OpisTab opis = new OpisTab();
        RadnoVreme radnoVreme = new RadnoVreme();
        Termini termini = new Termini();
        Recenzije recenzije = new Recenzije();

        opis.setArguments(bundle);
        radnoVreme.setArguments(bundle);
        termini.setArguments(bundle);
        recenzije.setArguments(bundle);

        adapter.addFragment(opis,"Opis");
        adapter.addFragment(radnoVreme,"Radno vreme");
        adapter.addFragment(termini,"Termini");
        adapter.addFragment(recenzije,"Recenzije");
        viewPager.setAdapter(adapter);
    }

    public void broadcastIntent(){
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(MyReceiver);
        }catch(Exception e){}
    }


    private void setupActionBar(String naslov) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(naslov);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            uslugaDatabaseHandler.deleteAll();
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
