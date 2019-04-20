package com.example.marija;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;
    private DatabaseHandler mDataBaseHelper = new DatabaseHandler(this);
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private TextView name;
    private TextView description;
    private ArrayList<Usluga> list;
    private ArrayAdapter<Usluga> adapter;
    //String[] data = {"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
    //int[] images = {R.drawable.frizerski_salon, R.drawable.skolajezika,R.drawable.privatnicasovi};
    //String[] opisi ={"Partizanskih baza 2","Gogoljeva 15","Balzakova 18"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<Usluga>();

        Usluga u1 = new Usluga("Belle Femme Frizer",R.drawable.frizerski_salon,"Partizanskih baza 2","Novi Sad","Lepota");
        Usluga u2 = new Usluga("Work and friends skola jezika", R.drawable.skolajezika,"Gogoljeva 15","Novi Sad","Obrazovanje");
        Usluga u3 = new Usluga("Privatni casovi matematike",R.drawable.privatnicasovi,"Balzakova 18","Novi Sad","Obrazovanje");
        List<Usluga> usluge = new ArrayList<Usluga>();
        usluge.add(u1);
        usluge.add(u2);
        usluge.add(u3);
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
        CustomAdapter customAdapter = new CustomAdapter();
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

        String[] array_spinner = new String[3];
        array_spinner[0]="Novi Sad";
        array_spinner[1]="Beograd";
        array_spinner[2]="Nis";
        Spinner s = findViewById(R.id.Spinner01);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);

        String[] array_spinner1 = new String[3];
        array_spinner1[0]="Sve";
        array_spinner1[1]="Lepota";
        array_spinner1[2]="Obrazovanje";
        Spinner ss = findViewById(R.id.Spinner02);
        ArrayAdapter adapter1 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner1);
        ss.setAdapter(adapter1);
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

    @SuppressWarnings("StatementWithEmptyBody")
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


    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return list.size();
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



                        imageView.setImageResource(list.get(position).getSlika());
                        name.setText(list.get(position).getNaziv());
                        description.setText(list.get(position).getOpis());


            return convertView;
        }
    }




}
