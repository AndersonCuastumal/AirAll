package com.lenovoexample.tracingvf.Objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterAlarms extends RecyclerView.Adapter<AdapterAlarms.UsersviewHolder>{

    List<Alarms> alarms;

    public AdapterAlarms(List<Alarms> alarms) {
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public AdapterAlarms.UsersviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarms_recycler, viewGroup,false);
        AdapterAlarms.UsersviewHolder holder = new AdapterAlarms.UsersviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlarms.UsersviewHolder usersviewHolder, int i) {
        Alarms alarmas = alarms.get(i);
        usersviewHolder.tAlarma.setText(alarmas.getAlarm());
        usersviewHolder.tNombre.setText(alarmas.getName());
        usersviewHolder.tDescripcion.setText(alarmas.getAdress());
        usersviewHolder.tCorreo.setText(alarmas.getEmail());
        usersviewHolder.tFecha.setText(alarmas.getFecha());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class UsersviewHolder extends RecyclerView.ViewHolder{

        TextView tNombre, tDescripcion, tCorreo,tAlarma,tFecha;

        public UsersviewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tDescripcion = itemView.findViewById(R.id.tDescripcion);
            tCorreo = itemView.findViewById(R.id.tCorreo);
            tAlarma = itemView.findViewById(R.id.tAlarma);
            tFecha = itemView.findViewById(R.id.tHora);
        }
    }
}
