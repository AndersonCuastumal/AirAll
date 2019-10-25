package com.lenovoexample.tracingvf.Objects;

public class Avatar {

    private int imagen;
    private String nombre;
    private String descripcion;

    public Avatar(int imagen, String nombre, String descripcion) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public int getImagen() {
        return imagen;
    }
}