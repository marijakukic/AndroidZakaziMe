package com.example.marija;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marija.Models.NotificationHelper;
import com.example.marija.Models.Pomocna;
import com.example.marija.Models.Rezervacija;
import com.example.marija.Models.Termin;
import com.example.marija.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;


public class SettingsActivity extends AppCompatPreferenceActivity {


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setupActionBar();


    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }


    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                //|| DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        DatabaseHandler databaseHandler;
        User u;
        EditTextPreference etp2;
        EditTextPreference etp;
        int brojac;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            databaseHandler = new DatabaseHandler(getActivity());
                u = databaseHandler.findUser();

             etp2=(EditTextPreference)findPreference("example2");
             etp = (EditTextPreference)findPreference("example1");

            Preference button = findPreference(getString(R.string.izaberiSliku));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), ChangeProfileImageActivity.class);
                    startActivity(intent);

                    return true;
                }
            });


           try{

                etp.setText(u.getKoriscnickoIme());
            }catch (Exception e){}

            etp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Query q = FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("email").equalTo(u.getEmail());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                User u = ds.getValue(User.class);
                                FirebaseDatabase.getInstance().getReference("Korisnici").child(ds.getKey()).removeValue();
                                u.setKoriscnickoIme(etp.getText());
                                FirebaseDatabase.getInstance().getReference("Korisnici").push().setValue(u);
                                etp.setText(u.getKoriscnickoIme());


                                //mozda dodati da li je korisnicko ime jednistveno


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
            });

           etp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {
                   Query q = FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("email").equalTo(u.getEmail());
                   q.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           for(DataSnapshot ds : dataSnapshot.getChildren()){
                               brojac++;
                               if(brojac==1) {
                                   User u = ds.getValue(User.class);
                                   Toast.makeText(getActivity(),"Promenili ste lozinku sa "+u.getPass()+" na "+etp2.getText(),Toast.LENGTH_LONG).show();
                                   FirebaseDatabase.getInstance().getReference("Korisnici").child(ds.getKey()).removeValue();
                                   u.setPass(etp2.getText());
                                   FirebaseDatabase.getInstance().getReference("Korisnici").push().setValue(u);
                               }

                       }}

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               return true;}
           });


        brojac=0;

        }





        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }

            return super.onOptionsItemSelected(item);

        }


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        SharedPreferences sharedPreferences;
        ArrayList<Rezervacija> lista;
        Date currentTime;
        Context c11;
        int brojac;
        String userSelectedValue;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            ListPreference listPreference = (ListPreference) findPreference("obavestenjaKeys");
            listPreference.setValue("tad");

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            SwitchPreference switchPreference = (SwitchPreference)findPreference("notifications_new_message");
            switchPreference.setChecked(false);
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final boolean isChecked = sharedPreferences.getBoolean("notifications_new_message",false);
                    if(!isChecked){
                        /*NotificationHelper notificationHelper = new NotificationHelper(getActivity());
                          Notification.Builder builder = notificationHelper.getNotification("Podsetnik");
                          notificationHelper.getManager().notify(new Random().nextInt(), builder.build());*/
                               //setAlarm(getActivity());

                        Preference listPreference = getPreferenceManager().findPreference("obavestenjaKeys");

                        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                userSelectedValue = (String) newValue;
                                Toast.makeText(getActivity(),"SELEKTOVANO:"+userSelectedValue,Toast.LENGTH_SHORT).show();
                                setAlarmForReservations(getActivity(),userSelectedValue);

                                return true;
                            }
                        });





                    }else{
                                    Query q = FirebaseDatabase.getInstance().getReference("Pomocna");
                                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                Pomocna p = ds.getValue(Pomocna.class);
                                                 brojac = p.getId();


                                            }

                                            cancelAlarm(getActivity(),brojac);//popravi ovo da radi,npr sacuvas duzinu liste i te ponistis
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                  }
                    return true;
                }

            });




        }

        public void cancelAlarm(Context c,int brojac){

            for(int i=0;i<brojac;i++) {
                AlarmManager alarmManager = (AlarmManager) c.getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(c, AlertReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(c, i, intent, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.cancel(pendingIntent);

                }
            }
            ListPreference listPreference = (ListPreference) findPreference("obavestenjaKeys");
            listPreference.setValue("tad");


        }



        public void setAlarmForReservations(Context c, final String selektovano){
            c11=c;
            lista= new ArrayList<>();
            currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy. HH:mm");
            String dateString = format.format( currentTime );
            String [] datumivreme = dateString.split(" ");
            String datum = datumivreme[0];
            String vreme = datumivreme[1];

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
            User u = databaseHandler.findUser();
            Query query = FirebaseDatabase.getInstance().getReference("Rezervacije")
                    .orderByChild("emailKorisnika").equalTo(u.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    FirebaseDatabase.getInstance().getReference("Pomocna").removeValue();
                    Pomocna p = new Pomocna();
                    p.setId(lista.size());//mozda se dodaje vise nego sto treba
                    FirebaseDatabase.getInstance().getReference("Pomocna").push().setValue(p);

                    for(int i=0;i<lista.size();i++){
                    Termin t = lista.get(i).getT();
                    String[] splitovanje = t.getVreme().split(":");

                    int sati = Integer.parseInt(splitovanje[0]);
                    int min = Integer.parseInt(splitovanje[1]);
                    Calendar calendar = Calendar.getInstance();
                    String[] datumSplit = t.getDatum().split("\\.");

                    int dan = Integer.parseInt(datumSplit[0]);
                    int mesec= Integer.parseInt(datumSplit[1]);
                    int godina=Integer.parseInt(datumSplit[2]);

                    if(mesec==1){calendar.set(Calendar.MONTH, Calendar.JANUARY);}
                    else if(mesec==2){calendar.set(Calendar.MONTH, Calendar.FEBRUARY);}
                    else if(mesec==3){calendar.set(Calendar.MONTH, Calendar.MARCH);}
                    else if(mesec==4){calendar.set(Calendar.MONTH, Calendar.APRIL);}
                    else if(mesec==5){calendar.set(Calendar.MONTH, Calendar.MAY);}
                    else if(mesec==6){calendar.set(Calendar.MONTH, Calendar.JUNE);}
                    else if(mesec==7){calendar.set(Calendar.MONTH, Calendar.JULY);}
                    else if(mesec==8){calendar.set(Calendar.MONTH, Calendar.AUGUST);}
                    else if(mesec==9){calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);}
                    else if(mesec==10){calendar.set(Calendar.MONTH, Calendar.OCTOBER);}
                    else if(mesec==11){calendar.set(Calendar.MONTH, Calendar.NOVEMBER);}
                    else if(mesec==12){calendar.set(Calendar.MONTH, Calendar.DECEMBER);}

                        calendar.set(Calendar.YEAR, godina);

                        calendar.set(Calendar.DAY_OF_MONTH, dan);




                        if (sati == 0) {
                            calendar.set(Calendar.AM_PM, Calendar.AM);
                        } else if (sati < 12) {
                            calendar.set(Calendar.AM_PM, Calendar.AM);
                        } else if (sati == 12) {
                            calendar.set(Calendar.AM_PM, Calendar.PM);
                        } else {
                            calendar.set(Calendar.AM_PM, Calendar.PM);
                        }

                        if(sati==13){sati = 1;}
                    else if(sati==14){sati = 2;}
                    else if(sati==15){sati =3;}
                    else if(sati==16){sati=4;}
                    else if(sati==17){sati=5;}
                    else if(sati==18){sati=6;}
                    else if(sati==19){sati=7;}
                    else if(sati==20){sati=8;}
                    else if(sati==21){sati=9;}
                    else if(sati==22){sati=10;}
                    else if(sati==23){sati=11;}
                    //ne stavljaj 24 ili 12 sati
                            if(selektovano.equals("tad")) {
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, min);
                                calendar.set(Calendar.HOUR, sati);
                            }else if(selektovano.equals("15 minutes")){
                                if(min<15){
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MINUTE, 60-15+min);
                                    calendar.set(Calendar.HOUR, sati-1);
                                }else {
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MINUTE, min - 15);
                                    calendar.set(Calendar.HOUR, sati);
                                }
                            }else if(selektovano.equals("30 minutes")){
                                if(min<30){
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MINUTE, 60-30+min);
                                    calendar.set(Calendar.HOUR, sati-1);
                                }else {
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MINUTE, min - 30);
                                    calendar.set(Calendar.HOUR, sati);
                                }
                            }else if(selektovano.equals("1 hour")){
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, min);
                                calendar.set(Calendar.HOUR, sati-1);
                            }else if(selektovano.equals("3 hours")){
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, min);
                                calendar.set(Calendar.HOUR, sati-3);
                            }else if(selektovano.equals("6 hours")){
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, min);
                                calendar.set(Calendar.HOUR, sati-6);
                            }


                    AlarmManager alarmManager = (AlarmManager) c11.getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(c11, AlertReciever.class);
                    intent.putExtra("datum",lista.get(i).getT().getDatum());
                    intent.putExtra("vreme",lista.get(i).getT().getVreme());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(c11, i, intent, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Toast.makeText(c11, lista.get(i).getT().getVreme(), Toast.LENGTH_SHORT).show();
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }}








                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

        public void setAlarm(Context c){

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 20);
                    calendar.set(Calendar.HOUR, 4);
                    calendar.set(Calendar.AM_PM, Calendar.PM);
                    AlarmManager alarmManager = (AlarmManager) c.getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(c, AlertReciever.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 1, intent, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                    Log.d("USO SAM OVDE", "NOTIFIKACIJA");


        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
