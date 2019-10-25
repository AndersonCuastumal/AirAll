package com.lenovoexample.tracingvf;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.AdapterUsers;
import com.lenovoexample.tracingvf.Objects.Avatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {


    List<Avatar> avatars;
    ArrayList<String> sexo;


    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;


    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recycler = view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        avatars = new ArrayList<>();
        //prueba avatar funciona perro
        avatars.add(new Avatar(R.drawable.w1, "Angel Beats", "muy buenos dias"));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adapter = new AdapterUsers(avatars);
        recycler.setAdapter(adapter);
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sexo = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap cadena = (HashMap) childSnapshot.getValue();
                    if (cadena != null) {

                        sexo.add((String) cadena.get("name"));


                        Toast.makeText(getActivity(), sexo.get(0), Toast.LENGTH_SHORT).show();



                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }
}