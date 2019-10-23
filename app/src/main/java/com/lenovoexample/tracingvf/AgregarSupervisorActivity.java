package com.lenovoexample.tracingvf;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lenovoexample.tracingvf.Objects.Supervisores;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AgregarSupervisorActivity extends AppCompatActivity {

    EditText eNombre, eCorreo, eContraseña;
    Button bButon;
    String direccion = "Oficinas Tracing";
    Double lat = 0.0, lng = 0.0;
    String direcciones = "";
    String nombre = "", correo = "", contrasena = "";

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_supervisor);

        eNombre = findViewById(R.id.eNombre);
        eCorreo = findViewById(R.id.eCorreo);
        eContraseña = findViewById(R.id.eContraseña);
        progressDialog = new ProgressDialog(this);
        bButon = findViewById(R.id.bButon);

        bButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarSupervisor();
            }
        });
    }

    private void registrarSupervisor() {
        nombre = eNombre.getText().toString().trim();
        correo = eCorreo.getText().toString().trim();
        contrasena = eContraseña.getText().toString().trim();
        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingresar un nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(correo)){
            Toast.makeText(this, "Ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Ingresar contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (nombre.equals("")||correo.equals("") || contrasena.equals("")) {
            Toast.makeText(this, "Llene todos los espacios ", Toast.LENGTH_LONG).show();
            return;
        }
        if(correo.contains("@tracing.com")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo,contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.setMessage("Registrando");
                                progressDialog.show();
                                miUbicacion();
                                Supervisores supervisores = new Supervisores(nombre,direccion,correo,lat,lng);
                                userRef.child("supervisors").push().setValue(supervisores);
                                Toast.makeText(getApplicationContext(), "Supervisor creado correctamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AgregarSupervisorActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage()+"", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(this, "Ingresar un correo valido(@tracing.com)", Toast.LENGTH_LONG).show();
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
}
