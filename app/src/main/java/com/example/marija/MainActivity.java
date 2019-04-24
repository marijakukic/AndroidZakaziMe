package com.example.marija;

import android.content.Intent;
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
import com.example.marija.Models.User;
import com.example.marija.Models.Usluga;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;
    private DatabaseHandler mDataBaseHelper = new DatabaseHandler(this);
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private UslugaDatabaseHandler uslugaDatabaseHandler = new UslugaDatabaseHandler(this);
    private ImageView imageView;
    private TextView name;
    private TextView description;

    private ArrayList<Usluga> list;
    private ArrayList<Usluga> konacnaListaUsluga = new ArrayList<Usluga>();
    private ArrayList<Usluga> novaListaUsluga = new ArrayList<Usluga>();
    private ArrayAdapter<Usluga> adapter;
    String[] array_spinner,array_spinner1;
    Spinner s,ss;
    ArrayAdapter adapterLokacije,adapter1;
    String  izabranaLokacija,izabranaKategorija;
    Listener listener;
    private boolean mSpinnerInitialized;
    CustomAdapter customAdapter;
    TextView nevidljivi;

    TextView nevidljiviIDusluge;
     int ID_usluge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<Usluga>();
        listener = new Listener();
        // PROVERITI KAKO SE SLIKE CUVAJU U BAZI, NE SME DIREKT IZ APLIKACIJE!
        Usluga u1 = new Usluga(0,"Belle Femme Frizer",R.drawable.frizerski_salon,"Najpovoljnije sisanje u gradu","Novi Sad","Lepota",
                "Partizanskih baza 2 Novi Sad","Feniranje 100 din","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                "Gotovina,kartica");
        Usluga u2 = new Usluga(1,"Work and friends skola jezika", R.drawable.skolajezika,"Gogoljeva 15","Novi Sad","Obrazovanje",
                "Gogoljeva 15 Novi Sad","Cas engleskog 1000 din","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                "Gotovina,kartica");
        Usluga u3 = new Usluga(2,"Privatni casovi matematike",R.drawable.privatnicasovi,"Balzakova 18","Beograd","Obrazovanje",
                "Balzakova 18 Beograd","Cas matematike 1000 din ","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                "Gotovina,kartica");
        Usluga u4 = new Usluga(3,"Izbeljivanje zuba",R.drawable.privatnicasovi,"Balzakova 18","Novi Sad","Zdravlje",
                "Balzakova 18 Novi Sad","Izbeljivanje 1000 din","Ponedeljak - Subota 09:00 - 17:00 Nedelja neradna",
                "Gotovina,kartica");
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

                    Intent myIntent = new Intent(view.getContext(),ListItemActivity.class);
                    ID_usluge = konacnaListaUsluga.get(position).getID();
                    myIntent.putExtra("ID_usluge",ID_usluge);
                    startActivity(myIntent);
                    uslugaDatabaseHandler.addUsluga(position);
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
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }else if(id == R.id.odjava){
            mDataBaseHelper.deleteAll();
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

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class Listener  implements
            AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!mSpinnerInitialized) {
                mSpinnerInitialized = true;
                lv.setAdapter(new CustomAdapter(list));
                izabranaLokacija = array_spinner[position];
                izabranaKategorija = array_spinner1[position];
                return;
            }
            switch (parent.getId()) {
                    case R.id.Spinner01:  //LOKACIJA
                        izabranaLokacija = array_spinner[position];
                        //Toast.makeText(MainActivity.this,izabranaLokacija,Toast.LENGTH_LONG).show();
                        if(izabranaLokacija.equals("Sve")) {
                            spiner_kategorije();
                            break;
                        }
                        else {
                                novaListaUsluga.clear();
                                for(Usluga u : list) {
                                    if(izabranaLokacija == u.getLokacija() && izabranaKategorija==u.getKategorija()) {
                                        novaListaUsluga.add(u);
                                    }else if(izabranaLokacija == u.getLokacija() && izabranaKategorija=="Sve") {
                                        spiner_lokacije();
                                        break;
                                    }
                                }
                                lv.setAdapter(new CustomAdapter(novaListaUsluga));
                                break;
                        }

                    case R.id.Spinner02:  //KATEGORIJA
                        izabranaKategorija = array_spinner1[position];
                       // Toast.makeText(MainActivity.this,izabranaKategorija,Toast.LENGTH_LONG).show();
                        if(izabranaKategorija == "Sve" ) {
                            spiner_lokacije();
                            break;
                        }
                        else {
                                novaListaUsluga.clear();
                                for(Usluga u : list) {
                                    if(izabranaKategorija == u.getKategorija() && izabranaLokacija==u.getLokacija()) {
                                        novaListaUsluga.add(u);
                                    } else if(izabranaKategorija == u.getKategorija() && izabranaLokacija=="Sve") {
                                        spiner_kategorije();
                                        break;
                                    }
                                }
                                lv.setAdapter(new CustomAdapter(novaListaUsluga));
                                break;
                        }
                    default:
                        break;
            }

        }
        public void spiner_lokacije(){
            if(!izabranaLokacija.equals("Sve")) {
                novaListaUsluga.clear();
                for (Usluga u : list) {
                    if(izabranaLokacija.equals(u.getLokacija())) {
                        novaListaUsluga.add(u);
                    }
                }
                lv.setAdapter(new CustomAdapter(novaListaUsluga));
                return;
            } else {
                lv.setAdapter(new CustomAdapter(list));
                return;
            }
        }

        public void spiner_kategorije(){
            if(!izabranaKategorija.equals("Sve")) {
                    novaListaUsluga.clear();
                    for (Usluga u : list) {
                        if(izabranaKategorija.equals(u.getKategorija())) {
                            novaListaUsluga.add(u);
                        }
                    }
                    lv.setAdapter(new CustomAdapter(novaListaUsluga));
                    return;
            } else {
                lv.setAdapter(new CustomAdapter(list));
                return;
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
            nevidljivi =(TextView)convertView.findViewById(R.id.nevidljiviIdUsluge);
            imageView.setImageResource(konacnaListaUsluga.get(position).getSlika());
            name.setText(konacnaListaUsluga.get(position).getNaziv());
            description.setText(konacnaListaUsluga.get(position).getOpis());
            nevidljivi.setText(Integer.toString(konacnaListaUsluga.get(position).getID()));

            User u = mDataBaseHelper.findUser();
            TextView navigation = (TextView)findViewById(R.id.korisnikNavigation1);
            TextView navigation1 = (TextView)findViewById(R.id.korImeNav1);
            ImageView slikaUsera= (ImageView)findViewById(R.id.slikaUsera1);
            navigation.setText(u.getEmail());
            navigation1.setText(u.getKoriscnickoIme());
            slikaUsera.setImageResource(R.drawable.user);


            return convertView;
        }
        public ArrayList<Usluga> listaAdaptera() {
            return konacnaListaUsluga;
        }



        private void updateList(ArrayList<Usluga> novalista){

            Toast.makeText(MainActivity.this,Integer.toString(novalista.size()),Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,Integer.toString(konacnaListaUsluga.size()),Toast.LENGTH_LONG).show();
            konacnaListaUsluga.clear();
            konacnaListaUsluga.addAll(list);
            //this.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();

            //return this;
        }
    }
}
