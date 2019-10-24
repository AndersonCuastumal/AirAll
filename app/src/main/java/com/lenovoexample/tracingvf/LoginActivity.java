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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.Usuarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText eCorreo, eContraseña;
    private Button bRegistro;
    private TextView tRegistro;
    String correo = "", contraseña ="", repContraseña ="";

    private FirebaseAuth mAuth;

    private String mailAdmin = "david";
    private String passAdmin = "1234";
    ArrayList<String> correosGoogle = new ArrayList<String>();

    //Login FB
    private FirebaseAuth.AuthStateListener getFirebaseAuthListener;



    //Login Google




    public static final int SIGN_IN_CODE=777;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "ViewDataBase";
    private FirebaseAuth mAuthAutentication;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

    boolean bandera = true;
    boolean pase = true;

    String namePerfil ="";
    String mailPerfil = "";
    String direcciones = "";
    Double lat = 0.0;
    Double lng = 0.0;

    String correoGoogle="";

    DatabaseReference listaRef = FirebaseDatabase.getInstance().getReference("users");

    ArrayList<String> correosList = new ArrayList<String>();


    private DatabaseReference usersDatabase;


    //autenticacion Logeo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //captura la ubicacion del usuario.
        miUbicacion();
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        getFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


        eCorreo = findViewById(R.id.eCorreo);
        eContraseña = findViewById(R.id.eContraseña);
        bRegistro = findViewById(R.id.bRegistro);
        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });
        tRegistro = findViewById(R.id.tRegistro);

        //usuarios base de datos y validacion
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                correosList = new ArrayList<String>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    HashMap map =(HashMap) childSnapshot.getValue();
                    if(map!=null) {
                        correosList.add((String) map.get("email"));
                    }
                    //or
                    String name = (String) childSnapshot.child("email").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

        getFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    goMainScreen();
                }
            }
        };

        tRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intent);
            }
        });
        if (getIntent().getBooleanExtra("EXIT", false)) { finish(); }
    }

    private void enviarDatos() {
        if(eCorreo.getText().toString().equals("")||eContraseña.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
        }else {
            inicioSesion();
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            namePerfil = acct.getDisplayName();
            mailPerfil = acct.getEmail();

            if (lat != 0.0 && lng != 0.0){
                try{
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(lat,lng,1);
                    if(!list.isEmpty()){
                        Address dirCalle = list.get(0);
                        direcciones = (dirCalle.getAddressLine(0)); }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                        try{usuarios.setName(ds.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Usuarios.class).getName());

                            if(!mailPerfil.equals(usuarios.getName())){
                                bandera = false;
                            }
                        }catch (Exception e){

                        }
                    }
                    if(bandera) {
                        if(!correosList.contains(mailPerfil)){
                            Usuarios usuarios = new Usuarios(namePerfil, direcciones, mailPerfil, lat, lng,"");
                            userRef.child("users").push().setValue(usuarios);
                            correoGoogle = mailPerfil;
                            correosList.add(mailPerfil);
                        }
                        bandera = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(this,"no se puede acceder",Toast.LENGTH_SHORT).show();
        }
    }

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

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"No se logro autenticar con firebase",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainScreen() {
        Intent i= new Intent(LoginActivity.this, AdminActivity.class);
        i.putExtra("correo",mailPerfil);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }



    private void inicioSesion() {
        String pass = eContraseña.getText().toString();
        String mail = eCorreo.getText().toString();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String mail = eCorreo.getText().toString();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                intent.putExtra("correo",mail);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Bienvenido Supervisor(Tracing)", Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().getMessage()+"",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onStop(){
        super.onStop();

    }

    @Override
    public void onBackPressed(){
        finish();
    }



}
