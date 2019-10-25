package com.lenovoexample.tracingvf.Objects;

public class Usuarios {

    String name;
    String adress;
    String email;
    Double lat;
    Double lng;
    String alarm;

    public Usuarios() {
    }

    public Usuarios(String name, String adress, String email, Double lat, Double lng, String alarm) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.alarm = alarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }


}
