package com.example.marija;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ListItemActivity extends AppCompatActivity {

    private SectionPageAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    int ID_usluge;
    Bundle bundle;
    private UslugaDatabaseHandler uslugaDatabaseHandler = new UslugaDatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setupActionBar();
        sectionsPagerAdapter= new SectionPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.container);
        setViewPager(viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        if(bundle!=null)
        {
            ID_usluge = bundle.getInt("ID_usluge");
        }


        //bundle.putInt("ID_usluge", ID_usluge);

    }

    private void setViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        OpisTab opis = new OpisTab();
        Intent iin= getIntent();
        bundle  = iin.getExtras();
        opis.setArguments(bundle);
        adapter.addFragment(opis,"Opis");
        adapter.addFragment(new RadnoVreme(),"Radno vreme");
        adapter.addFragment(new Termini(),"Termini");
        adapter.addFragment(new Recenzije(),"Recenzije");
        viewPager.setAdapter(adapter);
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
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
