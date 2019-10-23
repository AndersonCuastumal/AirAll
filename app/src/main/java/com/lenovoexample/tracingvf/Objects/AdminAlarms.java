package com.lenovoexample.tracingvf.Objects;

public class AdminAlarms {

    private String name;
    private String email;
    private String adress;
    private String emergency;
    private int foto;

    public AdminAlarms() {
    }

    public AdminAlarms(String name, String email, String adress, String emergency, int foto) {
        this.name = name;
        this.email = email;
        this.adress = adress;
        this.emergency = emergency;
        this.foto = foto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
