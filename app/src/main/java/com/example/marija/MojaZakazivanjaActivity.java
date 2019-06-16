package com.example.marija;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowId;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.nio.InvalidMarkException;
import java.util.ArrayList;
import java.util.List;

public class MojaZakazivanjaActivity extends AppCompatActivity {


    private ListView lv;
    String[] naslovi = {"Belle Femme Frizer","Piling lica","Cas matematike"};
    String[] termini ={"13.04.2019. 12:00","14.04.2019. 13:00","15.04.2019. 15:00"};
    private SectionPageAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private BroadcastReceiver MyReceiver = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moja_zakazivanja2);

        sectionsPagerAdapter= new SectionPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.container);
        setViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        MyReceiver = new MyReceiver();
        broadcastIntent();

        setupActionBar();

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);






    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new AktivneRezervacije(),"Aktivne rezervacije");
        adapter.addFragment(new ProsleRezervacije(),"Prosle rezervacije");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_moja_zakazivanja, menu); ako budemo hteli tri tacke
        //ovde odkomentarisemo i otkomentarisemo u menu_moja_zakazivanja
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return termini.length;
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
            convertView = getLayoutInflater().inflate(R.layout.activity_list_item_zakazivanja,null);

            TextView naslov =(TextView)convertView.findViewById(R.id.naslov);
            TextView termin =(TextView)convertView.findViewById(R.id.termin);
            Button btn = (Button)convertView.findViewById(R.id.otkazi);

            naslov.setText(naslovi[position]);
            termin.setText(termini[position]);


            return convertView;
        }
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



}
