package com.lenovoexample.tracingvf;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TertiaryFragment extends Fragment {
    private TextView tDatos;
    String correo;

    public TertiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tertiary, container, false);
        tDatos = view.findViewById(R.id.tDatos);
        tDatos.setText("gonorrea ome gonorrea");
        return view;
    }

}
