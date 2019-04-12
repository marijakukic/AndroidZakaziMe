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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] data = new String[]{"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
        // ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,
        //      android.R.id.text1,data);
        lv = (ListView)findViewById(R.id.listView);
        //final String[] values = new String[]{"Belle Femme Frizer","Work and friends skola jezika","Privatni casovi matematike"};
       // ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,
          //      android.R.id.text1,data);
        //lv.setAdapter(listAdapter);
        lv.setAdapter(new MyClassAdapter(this,R.layout.activity_list_item,data));
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
        Spinner s = (Spinner) findViewById(R.id.Spinner01);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);

        String[] array_spinner1 = new String[3];
        array_spinner1[0]="Lepota";
        array_spinner1[1]="Obrazovanje";
        array_spinner1[2]="Zdravlje";
        Spinner ss = (Spinner) findViewById(R.id.Spinner02);
        ArrayAdapter adapter1 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner1);
        ss.setAdapter(adapter1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     /*   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ViewHolder {

        private ImageView iv;
        private TextView tv;
        private Button b;
    }
    private class MyClassAdapter extends ArrayAdapter<String>{
        private int layout;
        public MyClassAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            layout = resource;
        }
    @Override
    public View getView(final int position, @Nullable View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(layout,parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView)convertView.findViewById(R.id.imageView2);
            viewHolder.tv = (TextView)convertView.findViewById(R.id.editText);
            viewHolder.b = (Button)convertView.findViewById(R.id.button);
            viewHolder.b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Pritisnuto dugme na poziciji" +position,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ListItemActivity.class));
                }
            });
            convertView.setTag(viewHolder);
        }else{
            mainViewHolder = (ViewHolder)convertView.getTag();
            mainViewHolder.tv.setText(getItem(position));


        }
        return convertView;
    }}


}
