package com.lenovoexample.tracingvf;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupervisorMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    private MarkerOptions place1, place2;
    LatLng latLng;
    double lat1 = 0.0;
    double lng1= 0.0;
    double lat2= 0.0;
    double lng2= 0.0;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    String correo = "";
    private DatabaseReference usersDatabase;
    ArrayList<Double> latSupervisor = new ArrayList<Double>();
    ArrayList<Double> lngSupervisor = new ArrayList<Double>();
    ArrayList<Double> latUser = new ArrayList<Double>();
    ArrayList<Double> lngUser = new ArrayList<Double>();
    ArrayList<String> nameSupervisor = new ArrayList<String>();
    ArrayList<String> nameUser = new ArrayList<String>();
    ArrayList<String> emailSupervisor = new ArrayList<String>();

    Button getDireccion;


    public SupervisorMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supervisor_map, container, false);
        SupervisorActivity supervisorActivity = (SupervisorActivity) getActivity();
        getDireccion = view.findViewById(R.id.bDireccion);
        correo = supervisorActivity.getDataFragment();
        listPoints=new ArrayList<>();
        place1=new MarkerOptions();
        place2=new MarkerOptions();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("conector");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latSupervisor = new ArrayList<Double>();
                lngSupervisor = new ArrayList<Double>();
                latUser = new ArrayList<Double>();
                lngUser = new ArrayList<Double>();
                nameSupervisor = new ArrayList<String>();
                nameUser = new ArrayList<String>();
                emailSupervisor = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        latSupervisor.add((Double) map.get("latSupervisor"));
                        lngSupervisor.add((Double) map.get("lngSupervisor"));
                        nameSupervisor.add((String) map.get("nameSupervisor"));
                        latUser.add((Double) map.get("latUser"));
                        lngUser.add((Double) map.get("lngUser"));
                        nameUser.add((String) map.get("nameUser"));
                        emailSupervisor.add((String) map.get("emailSupervisor"));
                    }
                }
                if(emailSupervisor.contains(correo)){
                    int index = emailSupervisor.indexOf(correo);
                    lat1 = latSupervisor.get(index);
                    lng1 = lngSupervisor.get(index);
                    lat2 = latUser.get(index);
                    lng2 = lngUser.get(index);
                }
                latLng=new LatLng(lat1,lng1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirection(lat1,lng1,lat2,lng2);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mSeguimiento);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        listPoints=new ArrayList<>();
        mMap=googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("conector");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latSupervisor = new ArrayList<Double>();
                lngSupervisor = new ArrayList<Double>();
                latUser = new ArrayList<Double>();
                lngUser = new ArrayList<Double>();
                nameSupervisor = new ArrayList<String>();
                nameUser = new ArrayList<String>();
                emailSupervisor = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) childSnapshot.getValue();
                    if (map != null) {
                        latSupervisor.add((Double) map.get("latSupervisor"));
                        lngSupervisor.add((Double) map.get("lngSupervisor"));
                        nameSupervisor.add((String) map.get("nameSupervisor"));
                        latUser.add((Double) map.get("latUser"));
                        lngUser.add((Double) map.get("lngUser"));
                        nameUser.add((String) map.get("nameUser"));
                        emailSupervisor.add((String) map.get("emailSupervisor"));
                    }
                }
                if(emailSupervisor.contains(correo)){
                    int index = emailSupervisor.indexOf(correo);
                    lat1 = latSupervisor.get(index);
                    lng1 = lngSupervisor.get(index);
                    lat2 = latUser.get(index);
                    lng2 = lngUser.get(index);
                    place1.position(new LatLng(lat1,lng1)).title(nameSupervisor.get(index));
                    place2.position(new LatLng(lat2,lng2)).title(nameUser.get(index));
                }
                mMap.addMarker(place1);
                mMap.addMarker(place2);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,24));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    private void getDirection(Double lat1, Double lng1, Double lat2, Double lng2) {
//Getting the URL
        String url = makeURL(lat1,lng1,lat2,lng2);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Calculando ruta...", "Por favor espera.", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                //Calling the method drawPath to draw the path
                System.out.println("ENTRO : onResponse" + response);
                drawPath(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder StringURL = new StringBuilder();
        StringURL.append("https://maps.googleapis.com/maps/api/directions/json");
        StringURL.append("?origin=");// from
        StringURL.append(Double.toString(sourcelat));
        StringURL.append(",");
        StringURL.append(Double.toString(sourcelog));
        StringURL.append("&destination=");// to
        StringURL.append(Double.toString(destlat));
        StringURL.append(",");
        StringURL.append(Double.toString(destlog));
        StringURL.append("&sensor=false&mode=driving&alternatives=true");
        StringURL.append("&key=AIzaSyDU8qoCAi17a1cDxe-kZsV-xTxszF8K4cY");
        return StringURL.toString();
    }
    public void drawPath(String result) {
        //System.out.print("ENTRO: drawPath");


        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray legsArray;
            //JSONObject legs = legsArray.getJSONObject(0);
            JSONObject legs2 = null;
            long totaldistancia=0;

            //String distance = legs2.getString("text");

            for(int i=0;i<routeArray.length();i++){
                legsArray=((JSONObject)routeArray.get(i)).getJSONArray("legs");
                for(int j=0;j<legsArray.length();j++){
                    legs2=((JSONObject)legsArray.get(j)).getJSONObject("distance");
                    totaldistancia=totaldistancia+Long.parseLong(legs2.getString("value"));

                }
            }
            double dist = totaldistancia / 1000.0;
            Toast.makeText(getActivity(),"Distancia= "+Double.toString(dist),Toast.LENGTH_LONG).show();


            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(Color.RED)
                    .geodesic(true)
            );
            //PermiteProgramar = true;
        } catch (JSONException e) {
            System.out.println("Fallo------------------------------:" + e);

            //PermiteProgramar = false;
        }
    }
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;

    }
}
