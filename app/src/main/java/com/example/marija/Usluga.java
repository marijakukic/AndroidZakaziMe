package com.example.marija;

import android.widget.ImageView;

public class Usluga {

    private String ID;
    private String naziv;
    private int slika;
    private String opis;
    private String lokacija;
    private String kategorija;

    public Usluga(){}

    public Usluga(String naziv, int slika, String opis, String lokacija, String kategorija) {
        this.naziv = naziv;
        this.slika = slika;
        this.opis = opis;
        this.lokacija = lokacija;
        this.kategorija = kategorija;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getSlika() {
        return slika;
    }

    public void setSlika(int slika) {
        this.slika = slika;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
