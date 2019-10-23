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
import com.lenovoexample.tracingvf.Objects.AdapterAlarms;
import com.lenovoexample.tracingvf.Objects.Alarms;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminEventFragment extends Fragment {

    RecyclerView rv;
    List<Alarms> alarms;
    AdapterAlarms adapter;

    public AdminEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_event, container, false);
        rv = view.findViewById(R.id.recyclerId);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        alarms = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adapter = new AdapterAlarms(alarms);
        rv.setAdapter(adapter);
        database.getReference().child("alarms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alarms.removeAll(alarms);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Alarms alarmas = snapshot.getValue(Alarms.class);
                    alarms.add(alarmas);
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
