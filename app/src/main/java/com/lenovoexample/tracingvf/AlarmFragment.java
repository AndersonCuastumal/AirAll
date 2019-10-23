package com.lenovoexample.tracingvf;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.Alarms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {
    //Alarma SOS;
    String correo;
    Button bHurto,bIncendio,bSalud,bTecnico,bRuta,bCancelar;
    DatabaseReference databaseReference;
    String fecha;
    String alarm = "";
    String alarmas ="";
    boolean bandera = false;
    ArrayList<Double> lat = new ArrayList<Double>();
    ArrayList<Double> lng = new ArrayList<Double>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> email = new ArrayList<String>();
    ArrayList<String> adress = new ArrayList<String>();

    ArrayList<String> correosList = new ArrayList<String>();

    private DatabaseReference usersDatabase;

    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference();


    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference userReftwice = FirebaseDatabase.getInstance().getReference("alarms");

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        bRuta=view.findViewById(R.id.bRuta);
        bHurto=view.findViewById(R.id.bHurto);
        bIncendio=view.findViewById(R.id.bIncendio);
        bSalud=view.findViewById(R.id.bSalud);
        bTecnico=view.findViewById(R.id.bTecnico);
        bCancelar=view.findViewById(R.id.bCancelar);

        UsuarioActivity usuarioActivity = (UsuarioActivity) getActivity();
        name = usuarioActivity.getListName();
        email = usuarioActivity.getListEmail();
        adress = usuarioActivity.getListAdress();
        lat = usuarioActivity.getListLat();
        lng = usuarioActivity.getListLng();
        correo = usuarioActivity.getDataFragment();

        fecha = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("alarms");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                correosList = new ArrayList<String>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    HashMap map =(HashMap) childSnapshot.getValue();
                    if(map!=null) {
                        correosList.add((String) map.get("email"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bHurto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarm = "hurto";
                alarmas = alarmas + ", "+alarm;
                Toast.makeText(getActivity(),"Emergencia enviada: Hurto",Toast.LENGTH_LONG).show();
                data();
            }
        });

        bIncendio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarm = "incendio";
                alarmas = alarmas + ", "+alarm;
                Toast.makeText(getActivity(),"Emergencia enviada: Incendio",Toast.LENGTH_LONG).show();
                data();
            }
        });

        bSalud.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarm = "salud";
                alarmas = alarmas + ", "+alarm;
                Toast.makeText(getActivity(),"Emergencia enviada: Salud",Toast.LENGTH_LONG).show();
                data();
            }
        });

        bTecnico.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarm = "tecnico";
                alarmas = alarmas + ", "+alarm;
                data();
                Toast.makeText(getActivity(),"Emergencia enviada: Hurto",Toast.LENGTH_LONG).show();
            }
        });

        bCancelar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Query qtwice = userReftwice.orderByChild("email").equalTo(correo);
                qtwice.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                            String clave = datasnapshot.getKey();
                            userReftwice.child(clave).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query q = userRef.orderByChild("email").equalTo(correo);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                            String clave =datasnapshot.getKey();
                            userRef.child(clave).child("alarm").setValue("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getActivity(),"Falsa alarma",Toast.LENGTH_LONG).show();
            }
        });

        bRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goMap();
                    }
                }
        );

        return view;
    }

    private void data() {
            Query q = userRef.orderByChild("email").equalTo(correo);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                        String clave =datasnapshot.getKey();
                        userRef.child(clave).child("alarm").setValue(alarm);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(!correosList.contains(correo)){
                Alarms alarms = new Alarms(name.get(email.indexOf(correo)), adress.get(email.indexOf(correo)), email.get(email.indexOf(correo)), lat.get(email.indexOf(correo)), lng.get(email.indexOf(correo)),alarm,fecha);
                refUser.child("alarms").push().setValue(alarms);
                correosList.add(email.get(email.indexOf(correo)));
            }else{
                Query qtwice = userReftwice.orderByChild("email").equalTo(correo);
                qtwice.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                            String clave =datasnapshot.getKey();
                            userReftwice.child(clave).child("alarm").setValue(alarm);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
    }

    private void goMap() {
        SeguimientoFragment fr= new SeguimientoFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameUsuario,fr).addToBackStack(null).commit();
    }
}
