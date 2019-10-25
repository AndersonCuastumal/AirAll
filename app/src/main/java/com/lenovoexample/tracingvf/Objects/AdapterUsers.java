package com.lenovoexample.tracingvf.Objects;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.UsersviewHolder>{

    private List<Avatar> avatars;

    public static class UsersviewHolder extends RecyclerView.ViewHolder{

        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;

        public UsersviewHolder(View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
        }
    }




    public AdapterUsers(List<Avatar> avatars) {
        this.avatars = avatars;
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }


    @Override
    public UsersviewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.users_recycler, viewGroup, false);
        return new UsersviewHolder(v);
    }

    @Override
    public void onBindViewHolder( UsersviewHolder holder, int position) {
        Avatar avatar = avatars.get(position);
        holder.imagen.setImageResource(avatar.getImagen());
        holder.nombre.setText(avatar.getNombre());
        holder.descripcion.setText(avatar.getDescripcion());
    }



}
