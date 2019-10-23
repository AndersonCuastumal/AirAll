package com.lenovoexample.tracingvf.Objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterNotifySuper extends RecyclerView.Adapter<AdapterNotifySuper.UsersviewHolder>{

    List<pushValors> usuarios;

    public AdapterNotifySuper(List<pushValors> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public AdapterNotifySuper.UsersviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarms_recycler, viewGroup,false);
        AdapterNotifySuper.UsersviewHolder holder = new AdapterNotifySuper.UsersviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotifySuper.UsersviewHolder usersviewHolder, int i) {
        pushValors users = usuarios.get(i);
        usersviewHolder.tAlarma.setText(users.getAlarm());
        usersviewHolder.tNombre.setText(users.getNameSupervisor());
        usersviewHolder.tDescripcion.setText(users.getAdressSupervisor());
        usersviewHolder.tCorreo.setText(users.getEmailSupervisor());
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
