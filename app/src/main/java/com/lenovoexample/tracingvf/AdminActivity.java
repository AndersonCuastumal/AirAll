package com.lenovoexample.tracingvf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.pushValors;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity implements OnMapReadyCallback {

    private android.support.v4.app.FragmentManager fm;
    private FragmentTransaction ft;

    private DatabaseReference alarmDatabase;
    private DatabaseReference supervisorDatabase;
    private DatabaseReference conectorDatabase;
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference();


    Integer numberIndex;
    ArrayList<String> correosList = new ArrayList<String>();

    ArrayList<Double> latSupervisor = new ArrayList<Double>();
    ArrayList<Double> lngSupervisor = new ArrayList<Double>();
    ArrayList<String> emailSupervisor = new ArrayList<String>();
    ArrayList<String> adressSupervisor = new ArrayList<String>();
    ArrayList<String> nameSupervisor = new ArrayList<String>();

    ArrayList<Double> latAlarm = new ArrayList<Double>();
    ArrayList<Double> lngAlarm = new ArrayList<Double>();
    ArrayList<String> emailAlarm = new ArrayList<String>();
    ArrayList<String> nameAlarm = new ArrayList<String>();
    ArrayList<String> adressAlarm = new ArrayList<String>();
    ArrayList<String> textAlarm = new ArrayList<String>();
    ArrayList<String> fechaAlarm = new ArrayList<String>();

    ArrayList<Float> distance = new ArrayList<Float>();
    ArrayList<Integer> menDistance = new ArrayList<Integer>();
     int popo = 0;
    int h;

    DatabaseReference adminReftwice = FirebaseDatabase.getInstance().getReference("conector");

    private static final int LOCATION_REQUEST = 500;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.mEventos:
                    AdminEventFragment adminEventFragment = new AdminEventFragment();
                    ft.replace(R.id.frames,adminEventFragment).commit();
                    return true;
                case R.id.mClientes:
                    UsersFragment usersFragment = new UsersFragment();
                    ft.replace(R.id.frames, usersFragment).commit();
                    return true;
                case R.id.mSupervisores:
                    SupervisorFragment supervisorFragment = new SupervisorFragment();
                    ft.replace(R.id.frames,supervisorFragment).commit();
                    return true;
                case R.id.mMapa:
                    AdminMapFragment adminMapFragment = new AdminMapFragment();
                    ft.replace(R.id.frames,adminMapFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigations);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        conectorDatabase = FirebaseDatabase.getInstance().getReference().child("conector");
        supervisorDatabase = FirebaseDatabase.getInstance().getReference().child("supervisors");
        alarmDatabase = FirebaseDatabase.getInstance().getReference().child("alarms");
        conectorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                correosList = new ArrayList<String>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    HashMap map =(HashMap) childSnapshot.getValue();
                    if(map!=null) {
                        correosList.add((String) map.get("emailUser"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        supervisorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latSupervisor = new ArrayList<Double>();
                lngSupervisor = new ArrayList<Double>();
                emailSupervisor = new ArrayList<String>();
                nameSupervisor = new ArrayList<String>();
                adressSupervisor = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        latSupervisor.add((Double) map.get("lat"));
                        lngSupervisor.add((Double) map.get("lng"));
                        emailSupervisor.add((String) map.get("email"));
                        nameSupervisor.add((String) map.get("name"));
                        adressSupervisor.add((String) map.get("adress"));
                    }
                }
                alarmDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        latAlarm = new ArrayList<Double>();
                        lngAlarm = new ArrayList<Double>();
                        emailAlarm = new ArrayList<String>();
                        nameAlarm = new ArrayList<String>();
                        adressAlarm = new ArrayList<String>();
                        textAlarm = new ArrayList<String>();
                        fechaAlarm = new ArrayList<String>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            HashMap map = (HashMap) childSnapshot.getValue();
                            if (map != null) {
                                latAlarm.add((Double) map.get("lat"));
                                lngAlarm.add((Double) map.get("lng"));
                                emailAlarm.add((String) map.get("email"));
                                nameAlarm.add((String) map.get("name"));
                                adressAlarm.add((String) map.get("adress"));
                                textAlarm.add((String) map.get("alarm"));
                                fechaAlarm.add((String) map.get("fecha"));
                            }
                        }
                        if(latAlarm.size()>0) {
                            for (int i = 0; i < nameAlarm.size(); i++) {
                                for (int b = 0; b < latSupervisor.size(); b++) {
                                    distancia(latAlarm.get(i), lngAlarm.get(i), latSupervisor.get(b), lngSupervisor.get(b));
                                }
                                menDistance.add(distance.indexOf(distance.get(min())));
                            }
                            if(distance.size()>0){
                                try {
                                    AdminEventFragment adminEventFragment = new AdminEventFragment();
                                    ft.replace(R.id.frames, adminEventFragment).commit();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void distancia(Double aDouble, Double aDouble1, Double aDouble2, Double aDouble3) {
        Location locationA = new Location("punto A");
        Location locationB = new Location("punto B");
        locationA.setLatitude(aDouble);
        locationA.setLongitude(aDouble1);
        locationB.setLatitude(aDouble2);
        locationB.setLongitude(aDouble3);
        float ddd = locationA.distanceTo(locationB);
        distance.add(ddd/1000);
    }

    private Integer min() {
        int min = 0;
        Float minimo = distance.get(0);
        for(; popo<distance.size();popo++){
            if(minimo<=distance.get(popo)){
                min = distance.indexOf(minimo);
            }
            else {
                minimo = distance.get(popo);
                min = distance.indexOf(minimo);
            }
        }
        popo++;
        return min;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //coloca menu
        getMenuInflater().inflate(R.menu.admindosmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (id){
            case R.id.mAgregar:
                intent = new Intent(AdminActivity.this, AgregarSupervisorActivity.class);
                startActivity(intent);
                break;
            case R.id.mBorrarS:
                intent = new Intent(AdminActivity.this, BorrarSupervisorActivity.class);
                startActivity(intent);
                break;
            case R.id.mBorrar:
                Caudal();
                break;
            case R.id.mSesion:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Caudal() {

        if(distance.size()>0){
            numberIndex = min();
                for(int i = 0; i<latAlarm.size();i++){
                    h = i;
                    if(!correosList.contains(emailAlarm.get(i))){
                        pushValors Push = new pushValors(nameSupervisor.get(menDistance.get(i)),nameAlarm.get(i),emailSupervisor.get(menDistance.get(i)),emailAlarm.get(i),latSupervisor.get(menDistance.get(i)),lngSupervisor.get(menDistance.get(i)),latAlarm.get(i),lngAlarm.get(i),adressSupervisor.get(menDistance.get(i)),adressAlarm.get(i),textAlarm.get(i),fechaAlarm.get(i));
                        refUser.child("conector").push().setValue(Push);
                        correosList.add(emailAlarm.get(i));
                    }
                    else {
                        Query qtwice = adminReftwice.orderByChild("emailUser").equalTo(emailAlarm.get(i));
                        qtwice.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                    String clave = datasnapshot.getKey();
                                    adminReftwice.child(clave).child("nameSupervisor").setValue(nameSupervisor.get(menDistance.get(h)));
                                    adminReftwice.child(clave).child("emailSupervisor").setValue(emailSupervisor.get(menDistance.get(h)));
                                    adminReftwice.child(clave).child("latSupervisor").setValue(latSupervisor.get(menDistance.get(h)));
                                    adminReftwice.child(clave).child("lngSupervisor").setValue(lngSupervisor.get(menDistance.get(h)));
                                    adminReftwice.child(clave).child("adressSupervisor").setValue(adressSupervisor.get(menDistance.get(h)));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
        }
    }

    @Override
    public void onBackPressed(){finish();}

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
