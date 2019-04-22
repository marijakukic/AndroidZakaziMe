package com.example.marija;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marija.Models.Kategorija;
import com.example.marija.Models.Lokacija;
import com.example.marija.Models.Usluga;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.toIntExact;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;
    private DatabaseHandler mDataBaseHelper = new DatabaseHandler(this);
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private ImageView imageView;
    private TextView name;
    private TextView description;

    private ArrayList<Usluga> list,konacnaListaUsluga = new ArrayList<Usluga>();
    private ArrayList<Usluga> novaListaUsluga = new ArrayList<Usluga>();
    private ArrayAdapter<Usluga> adapter;
    String[] array_spinner,array_spinner1;
    Spinner s,ss;
    ArrayAdapter adapterLokacije,adapter1;
    String  izabranaLokacija,izabranaKategorija;
    Listener listener;
    private boolean mSpinnerInitialized;
    CustomAdapter customAdapter;
    //String[] data = {"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
    //int[] images = {R.drawable.frizerski_salon, R.drawable.skolajezika,R.drawable.privatnicasovi};
    //String[] opisi ={"Partizanskih baza 2","Gogoljeva 15","Balzakova 18"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<Usluga>();
        listener = new Listener();

        Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.frizerski_salon,"Partizanskih baza 2","Novi Sad","Lepota");
        Usluga u2 = new Usluga(1,"Work and friends skola jezika", R.drawable.skolajezika,"Gogoljeva 15","Novi Sad","Obrazovanje");
        Usluga u3 = new Usluga(2,"Privatni casovi matematike",R.drawable.privatnicasovi,"Balzakova 18","Beograd","Obrazovanje");
        Usluga u4 = new Usluga(3,"Izbeljivanje zuba",R.drawable.privatnicasovi,"Balzakova 18","Novi Sad","Zdravlje");
        List<Usluga> usluge = new ArrayList<Usluga>();
        usluge.add(u1);
        usluge.add(u2);
        usluge.add(u3);
        usluge.add(u4);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Usluge");
        FirebaseDatabase.getInstance().getReference("Usluge").removeValue();
        addUslugaToFireBase(usluge);
        Query query = FirebaseDatabase.getInstance().getReference("Usluge");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Usluga u = ds.getValue(Usluga.class);
                    /*imageView.setImageResource(u.getSlika());
                    name.setText(u.getNaziv());
                    description.setText(u.getOpis());*/
                    list.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv = findViewById(R.id.listView);
        customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent myIntent = new Intent(view.getContext(),ListItemActivity.class);
                    startActivityForResult(myIntent,0);
                }
            }
        });
        // UPIS U BAZU

        databaseReference1=firebaseDatabase.getReference("Lokacije");
        databaseReference1.removeValue();

        Lokacija l0= new Lokacija("Sve",0);
        Lokacija l1= new Lokacija("Novi Sad",1);
        Lokacija l2= new Lokacija("Beograd",2);
        Lokacija l3= new Lokacija("Nis",3);
        ArrayList<Lokacija> lokacijeZaBazu = new ArrayList<Lokacija>();
        lokacijeZaBazu.add(l0);
        lokacijeZaBazu.add(l1);
        lokacijeZaBazu.add(l2);
        lokacijeZaBazu.add(l3);

        for (Lokacija lok : lokacijeZaBazu) {
            databaseReference1.push().setValue(lok);
        }

        // CITANJE IZ BAZE

        Query query1 = firebaseDatabase.getReference("Lokacije");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array_spinner = new String[(int)dataSnapshot.getChildrenCount()];
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //String duzinaNiza =  Integer.toString((int)dataSnapshot.getChildrenCount());
                    //Toast.makeText(MainActivity.this,duzinaNiza,Toast.LENGTH_LONG).show();
                    Lokacija k = ds.getValue(Lokacija.class);
                    array_spinner[k.getId()] = k.getNaziv();

                }
                s = findViewById(R.id.Spinner01);
                adapterLokacije = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, array_spinner);
                s.setAdapter(adapterLokacije);
                s.setOnItemSelectedListener(listener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

    });
        // UPIS U BAZU
        databaseReference2=firebaseDatabase.getReference("Kategorije");
        databaseReference2.removeValue();

        Kategorija k1= new Kategorija("Sve",0);
        Kategorija k2= new Kategorija("Lepota",1);
        Kategorija k3= new Kategorija("Obrazovanje",2);
        Kategorija k4= new Kategorija("Zdravlje",3);
        ArrayList<Kategorija> katZaBazu = new ArrayList<Kategorija>();
        katZaBazu.add(k1);
        katZaBazu.add(k2);
        katZaBazu.add(k3);
        katZaBazu.add(k4);

        for (Kategorija kat : katZaBazu) {
            databaseReference2.push().setValue(kat);
        }

        // CITANJE IZ BAZE

        Query query2 = firebaseDatabase.getReference("Kategorije");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array_spinner1 = new String[(int)dataSnapshot.getChildrenCount()];
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Kategorija k = ds.getValue(Kategorija.class);
                    array_spinner1[k.getId()] = k.getNaziv();

                }
                ss = findViewById(R.id.Spinner02);
                adapter1 = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_spinner_item, array_spinner1);
                ss.setAdapter(adapter1);
                ss.setOnItemSelectedListener(listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void addUslugaToFireBase(List<Usluga> listaUsluga){
        for(Usluga u : listaUsluga) {
            databaseReference.push().setValue(u);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }else if(id == R.id.odjava){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pocetna) {
            // Handle the camera action
        } else if (id == R.id.nav_zakazivanja) {
            startActivity(new Intent(getApplicationContext(),MojaZakazivanjaActivity.class));
        } else if (id == R.id.nav_podesavaja) {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }

         /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
            */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class Listener  implements
            AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Spinner spin = (Spinner)parent;
            Spinner spin2 = (Spinner)parent;

            if (!mSpinnerInitialized) {
                mSpinnerInitialized = true;
                lv.setAdapter(new CustomAdapter(list));

                Toast.makeText(MainActivity.this,"PRVI PUT",Toast.LENGTH_LONG).show();

                return;
            }
            novaListaUsluga.clear();
            if(spin.getId() == R.id.Spinner01) //lokacije
            {
                izabranaLokacija = array_spinner[position];
                Toast.makeText(MainActivity.this,izabranaLokacija,Toast.LENGTH_LONG).show();
                if(izabranaLokacija.equals("Sve") ) {
                    //for()
                    Toast.makeText(MainActivity.this,"Usao u lok SVE",Toast.LENGTH_LONG).show();
                    lv.setAdapter(new CustomAdapter(list));

                    return;

                } else {

                    for (Usluga u : list) {
                        Toast.makeText(MainActivity.this,izabranaLokacija,Toast.LENGTH_LONG).show();
                        if (izabranaLokacija.equals(u.getLokacija()) && !novaListaUsluga.contains(u)) {
                            novaListaUsluga.add(u);
                        }
                    }
                    Toast.makeText(MainActivity.this,Integer.toString(novaListaUsluga.size()),Toast.LENGTH_LONG).show();
                    lv.setAdapter(new CustomAdapter(novaListaUsluga));

                    return;
                }

            }
            else if(spin2.getId() == R.id.Spinner02) //kategorije
            {
                izabranaKategorija = array_spinner1[position];
                Toast.makeText(MainActivity.this,izabranaKategorija,Toast.LENGTH_LONG).show();
                if(izabranaKategorija.equals("Sve")) {
                    Toast.makeText(MainActivity.this,"Usao u kat SVE",Toast.LENGTH_LONG).show();
                    lv.setAdapter(new CustomAdapter(list));
                    return;
                }
                else {

                    for (Usluga u : list) {
                        if (izabranaKategorija.equals(u.getKategorija()) && !novaListaUsluga.contains(u) ) {
                            novaListaUsluga.add(u);
                        }

                    }
                    lv.setAdapter(new CustomAdapter(novaListaUsluga));
                    return;
                }

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    class CustomAdapter extends BaseAdapter{

        public CustomAdapter(ArrayList<Usluga> x) {
            konacnaListaUsluga = x;
        }
        public CustomAdapter() {
        }

        @Override
        public int getCount() {

            return konacnaListaUsluga.size();
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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item,null);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
            TextView name =(TextView)convertView.findViewById(R.id.name);
            TextView description =(TextView)convertView.findViewById(R.id.description);

            imageView.setImageResource(konacnaListaUsluga.get(position).getSlika());
            name.setText(konacnaListaUsluga.get(position).getNaziv());
            description.setText(konacnaListaUsluga.get(position).getOpis());


            return convertView;
        }


        private CustomAdapter updateList(ArrayList<Usluga> novalista){

            Toast.makeText(MainActivity.this,Integer.toString(novalista.size()),Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,Integer.toString(konacnaListaUsluga.size()),Toast.LENGTH_LONG).show();
            konacnaListaUsluga.clear();
            konacnaListaUsluga.addAll(list);
            this.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();

            return this;
        }
    }
}
