package com.lenovoexample.tracingvf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SupervisorActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fm;
    private FragmentTransaction ft;

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("supervisors");

    String correo = "";
    String direcciones = "";
    Double lat = 0.0;
    Double lng = 0.0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.mEventos:
                    NorificacionesFragment norificacionesFragment = new NorificacionesFragment();
                    ft.replace(R.id.framesaso, norificacionesFragment).commit();
                    return true;
                case R.id.mClientes:
                    UsersFragment usersFragment = new UsersFragment();
                    ft.replace(R.id.framesaso, usersFragment).commit();
                    return true;
                case R.id.mMapa:
                    SupervisorMapFragment supervisorMapFragment = new SupervisorMapFragment();
                    ft.replace(R.id.framesaso, supervisorMapFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        NorificacionesFragment norificacionesFragment = new NorificacionesFragment();
        ft.replace(R.id.framesaso, norificacionesFragment).commit();
        recibirDatosBD();
        miUbicacion();

    }

    private void recibirDatosBD() {
        Bundle extras = getIntent().getExtras();
        correo = extras.getString("correo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //coloca menu
        getMenuInflater().inflate(R.menu.supervisordosmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id){
            case R.id.mSesion:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                intent = new Intent(SupervisorActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){finish();}

    private static int PETICION_PERMISO_LOCACION = 101;

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCACION);
            return;
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ubicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }
    }

    private void ubicacion(Location location) {
        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }
    public void setLocation(Location location){
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(!list.isEmpty()){
                    Address dirCalle = list.get(0);
                    direcciones = (dirCalle.getAddressLine(0));
                    Query q = userRef.orderByChild("email").equalTo(correo);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                String clave =datasnapshot.getKey();
                                userRef.child(clave).child("lat").setValue(lat);
                                userRef.child(clave).child("lng").setValue(lng);
                                userRef.child(clave).child("adress").setValue(direcciones);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ubicacion(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            locationStart();
        }
    };
    private void locationStart(){
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingIntent);
        }
    }

    public String getDataFragment(){
        return correo;
    }



}
