package com.lenovoexample.tracingvf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UsuarioActivity extends AppCompatActivity{

    String correo="";
    ArrayList<Double> lat = new ArrayList<Double>();
    ArrayList<Double> lng = new ArrayList<Double>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> email = new ArrayList<String>();
    ArrayList<String> adress = new ArrayList<String>();
    String cNombre, cDireccion,rMensaje;
    double latitud,longitud;
    boolean flag = false;
    private android.support.v4.app.FragmentManager fm;
    private FragmentTransaction ft;

    GoogleApiClient googleApiClient;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.mEventos:
                    NotficacionesUserFragment notficacionesUserFragment = new NotficacionesUserFragment();
                    ft.replace(R.id.frameUsuario, notficacionesUserFragment).commit();
                    return true;
                case R.id.mMap:
                    cast();
                    return true;
                case R.id.mPerfil:

                    return true;
            }
            return false;
        }
    };

    private void cast() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat = new ArrayList<Double>();
                lng = new ArrayList<Double>();
                name = new ArrayList<String>();
                email = new ArrayList<String>();
                adress = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        lat.add((Double) map.get("lat"));
                        lng.add((Double) map.get("lng"));
                        name.add((String) map.get("name"));
                        email.add((String)map.get("email"));
                        adress.add((String)map.get("adress"));
                    }
                }
                if(email.size()>0){
                    try {
                        AlarmFragment mapFragment = new AlarmFragment();
                        ft.replace(R.id.frameUsuario, mapFragment).commit();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle extras = getIntent().getExtras();
        correo = extras.getString("correo");

        NotficacionesUserFragment notficacionesUserFragment = new NotficacionesUserFragment();
        ft.replace(R.id.frameUsuario, notficacionesUserFragment).commit();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat = new ArrayList<Double>();
                lng = new ArrayList<Double>();
                name = new ArrayList<String>();
                email = new ArrayList<String>();
                adress = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        lat.add(Double.valueOf(String.valueOf(map.get("lat"))));
                        lng.add((Double)map.get("lng"));
                        name.add((String)map.get("name"));
                        email.add((String)map.get("email"));
                        adress.add((String)map.get("adress"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //coloca menu
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id){
            case R.id.mSesion:
                FirebaseAuth.getInstance().signOut();
                try{
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }catch (Exception e){
                    LoginManager.getInstance().logOut();
                    intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){finish();}

    public String getDataFragment(){
        return correo;
    }

    public ArrayList<String> getListName(){
        return name;
    }

    public ArrayList<String> getListAdress(){
        return adress;
    }

    public ArrayList<String> getListEmail(){
        return email;
    }

    public ArrayList<Double> getListLat(){
        return lat;
    }

    public ArrayList<Double> getListLng(){
        return lng;
    }

}