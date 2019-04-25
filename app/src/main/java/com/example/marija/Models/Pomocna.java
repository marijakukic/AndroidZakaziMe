package com.example.marija.Models;

public class Pomocna {

    int id;
    String datum;
    String vreme;
    int idRez;

    public Pomocna(int id, String datum, String vreme, int idRez) {
        this.id = id;
        this.datum = datum;
        this.vreme = vreme;
        this.idRez = idRez;
    }

    public Pomocna(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getVreme() {
        return vreme;
    }

    public void setVreme(String vreme) {
        this.vreme = vreme;
    }

    public int getIdRez() {
        return idRez;
    }

    public void setIdRez(int idRez) {
        this.idRez = idRez;
    }
}
