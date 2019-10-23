package com.lenovoexample.tracingvf;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.AdapterNotifySuper;
import com.lenovoexample.tracingvf.Objects.pushValors;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotficacionesUserFragment extends Fragment {

    RecyclerView rv;
    ArrayList<pushValors> alarms;
    AdapterNotifySuper adapter;
    String correo;

    public NotficacionesUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notficaciones_user, container, false);
        rv = view.findViewById(R.id.recyclernoify);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        alarms = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adapter = new AdapterNotifySuper(alarms);
        rv.setAdapter(adapter);
        UsuarioActivity usuarioActivity = (UsuarioActivity) getActivity();
        correo = usuarioActivity.getDataFragment();
        database.getReference().child("conector").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alarms.removeAll(alarms);

                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    pushValors alarmas = snapshot.getValue(pushValors.class);
                    if (alarmas.getEmailUser().equals(correo)){
                        alarms.add(alarmas);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

}
