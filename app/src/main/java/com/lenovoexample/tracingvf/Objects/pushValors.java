package com.lenovoexample.tracingvf.Objects;

public class pushValors {

    String nameSupervisor;
    String nameUser;
    String emailSupervisor;
    String emailUser;
    Double latSupervisor;
    Double lngSupervisor;
    Double latUser;
    Double lngUser;
    String adressSupervisor;
    String adressUser;
    String alarm;
    String fecha;

    public pushValors() {
    }

    public pushValors(String nameSupervisor, String nameUser, String emailSupervisor, String emailUser, Double latSupervisor, Double lngSupervisor, Double latUser, Double lngUser, String adressSupervisor, String adressUser, String alarm, String fecha) {
        this.nameSupervisor = nameSupervisor;
        this.nameUser = nameUser;
        this.emailSupervisor = emailSupervisor;
        this.emailUser = emailUser;
        this.latSupervisor = latSupervisor;
        this.lngSupervisor = lngSupervisor;
        this.latUser = latUser;
        this.lngUser = lngUser;
        this.adressSupervisor = adressSupervisor;
        this.adressUser = adressUser;
        this.alarm = alarm;
        this.fecha = fecha;
    }

    public String getNameSupervisor() {
        return nameSupervisor;
    }

    public void setNameSupervisor(String nameSupervisor) {
        this.nameSupervisor = nameSupervisor;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmailSupervisor() {
        return emailSupervisor;
    }

    public void setEmailSupervisor(String emailSupervisor) {
        this.emailSupervisor = emailSupervisor;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Double getLatSupervisor() {
        return latSupervisor;
    }

    public void setLatSupervisor(Double latSupervisor) {
        this.latSupervisor = latSupervisor;
    }

    public Double getLngSupervisor() {
        return lngSupervisor;
    }

    public void setLngSupervisor(Double lngSupervisor) {
        this.lngSupervisor = lngSupervisor;
    }

    public Double getLatUser() {
        return latUser;
    }

    public void setLatUser(Double latUser) {
        this.latUser = latUser;
    }

    public Double getLngUser() {
        return lngUser;
    }

    public void setLngUser(Double lngUser) {
        this.lngUser = lngUser;
    }

    public String getAdressSupervisor() {
        return adressSupervisor;
    }

    public void setAdressSupervisor(String adressSupervisor) {
        this.adressSupervisor = adressSupervisor;
    }

    public String getAdressUser() {
        return adressUser;
    }

    public void setAdressUser(String adressUser) {
        this.adressUser = adressUser;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
