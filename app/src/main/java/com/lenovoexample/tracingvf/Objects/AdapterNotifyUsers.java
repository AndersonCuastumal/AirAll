package com.lenovoexample.tracingvf.Objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterNotifyUsers extends RecyclerView.Adapter<AdapterNotifyUsers.UsersviewHolder>{

    List<pushValors> usuarios;

    public AdapterNotifyUsers(List<pushValors> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public AdapterNotifyUsers.UsersviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarms_recycler, viewGroup,false);
        AdapterNotifyUsers.UsersviewHolder holder = new AdapterNotifyUsers.UsersviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotifyUsers.UsersviewHolder usersviewHolder, int i) {
        pushValors users = usuarios.get(i);
        usersviewHolder.tAlarma.setText(users.getAlarm());
        usersviewHolder.tNombre.setText(users.getNameUser());
        usersviewHolder.tDescripcion.setText(users.getAdressUser());
        usersviewHolder.tCorreo.setText(users.getEmailUser());
        usersviewHolder.tFecha.setText(users.getFecha());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
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
