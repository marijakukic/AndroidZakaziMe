package com.example.marija;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;
    String[] data = {"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
    int[] images = {R.drawable.frizerski_salon, R.drawable.skolajezika,R.drawable.privatnicasovi};
    String[] opisi ={"Partizanskih baza 2","Gogoljeva 15","Balzakova 18"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,
        //      android.R.id.text1,data);
        lv = findViewById(R.id.listView);
        //final String[] values = new String[]{"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
       // ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,
          //      android.R.id.text1,data);
        //lv.setAdapter(listAdapter);

        //lv.setAdapter(new MyClassAdapter(this,R.layout.activity_list_item,data));
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
            return images.length;
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

            imageView.setImageResource(images[position]);
            name.setText(data[position]);
            description.setText(opisi[position]);


            return convertView;
        }
    }


}
