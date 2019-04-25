package com.example.marija.Models;

import java.sql.Date;

public class Termin {
    private int id;
    private String datum;
    private String vreme;
    private boolean slobodan;
    private int idUsluge;

    public Termin(String datum, String vreme, boolean slobodan) {
        this.datum = datum;
        this.vreme = vreme;
        this.slobodan = slobodan;
    }

    public Termin(int id, String datum, String vreme, boolean slobodan, int idUsluge) {
        this.id = id;
        this.datum = datum;
        this.vreme = vreme;
        this.slobodan = slobodan;
        this.idUsluge = idUsluge;
    }

    public Termin(){}

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

    public boolean isSlobodan() {
        return slobodan;
    }

    public void setSlobodan(boolean slobodan) {
        this.slobodan = slobodan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsluge() {
        return idUsluge;
    }

    public void setIdUsluge(int idUsluge) {
        this.idUsluge = idUsluge;
    }
}
