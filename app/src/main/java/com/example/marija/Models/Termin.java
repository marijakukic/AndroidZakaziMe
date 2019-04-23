package com.example.marija.Models;

import java.sql.Date;

public class Termin {

    private String datum;
    private String vreme;
    private boolean slobodan;

    public Termin(String datum, String vreme, boolean slobodan) {
        this.datum = datum;
        this.vreme = vreme;
        this.slobodan = slobodan;
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
}
