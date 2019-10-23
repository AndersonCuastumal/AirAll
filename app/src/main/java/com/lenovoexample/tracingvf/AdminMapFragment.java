package com.lenovoexample.tracingvf;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminMapFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;

    MapView mapView;
    GoogleMap mMap;

    private DatabaseReference usersDatabase;
    private DatabaseReference supervisorDatabase;
    ArrayList<Double> lat = new ArrayList<Double>();
    ArrayList<Double> lng = new ArrayList<Double>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<LatLng> ubicacion = new ArrayList<LatLng>();
    ArrayList<Double> latS = new ArrayList<Double>();
    ArrayList<Double> lngS = new ArrayList<Double>();
    ArrayList<String> nameS = new ArrayList<String>();


    public AdminMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_map, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat = new ArrayList<Double>();
                lng = new ArrayList<Double>();
                name = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        lat.add((Double) map.get("lat"));
                        lng.add((Double) map.get("lng"));
                        name.add((String) map.get("name"));
                    }
                }
                for (int i = 0; i < lat.size(); i++) {
                    LatLng pos = new LatLng(lat.get(i), lng.get(i));
                    ubicacion.add(pos);
                    mMap.addMarker(new MarkerOptions().position(pos).title(name.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        supervisorDatabase = FirebaseDatabase.getInstance().getReference().child("supervisors");
        supervisorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latS = new ArrayList<Double>();
                lngS = new ArrayList<Double>();
                nameS = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        latS.add((Double) map.get("lat"));
                        lngS.add((Double) map.get("lng"));
                        nameS.add((String) map.get("name"));
                    }
                }
                for (int i = 0; i < latS.size(); i++) {
                    LatLng pos = new LatLng(latS.get(i), lngS.get(i));
                    ubicacion.add(pos);
                    mMap.addMarker(new MarkerOptions().position(pos).title(nameS.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LatLng medellin = new LatLng(6.2431443, -75.5845242);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(medellin, 12));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
