package com.example.marija.Models;

import android.widget.ImageView;

public class Usluga {

    private int ID;
    private String naziv;
    private int slika;
    private String opis;
    private String lokacija;
    private String kategorija;
    private String adresa;
    private String cenovik;
    private String radnoVreme;
    private String nacinPlacanja;
    String ime_slike;

    public Usluga(){}
    public Usluga(String naziv, int slika, String opis, String lokacija, String kategorija) {

        this.naziv = naziv;
        this.slika = slika;
        this.opis = opis;
        this.lokacija = lokacija;
        this.kategorija = kategorija;
    }

    public Usluga(int ID, String naziv, int slika, String opis, String lokacija,
                  String kategorija, String adresa, String cenovik, String radnoVreme, String nacinPlacanja) {
        this.ID = ID;
        this.naziv = naziv;
        this.slika = slika;
        this.opis = opis;
        this.lokacija = lokacija;
        this.kategorija = kategorija;
        this.adresa = adresa;
        this.cenovik = cenovik;
        this.radnoVreme = radnoVreme;
        this.nacinPlacanja = nacinPlacanja;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getCenovik() {
        return cenovik;
    }

    public void setCenovik(String cenovik) {
        this.cenovik = cenovik;
    }

    public String getRadnoVreme() {
        return radnoVreme;
    }

    public void setRadnoVreme(String radnoVreme) {
        this.radnoVreme = radnoVreme;
    }

    public String getNacinPlacanja() {
        return nacinPlacanja;
    }

    public void setNacinPlacanja(String nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
    }

    public String getIme_slike() {
        return ime_slike;
    }

    public void setIme_slike(String ime_slike) {
        this.ime_slike = ime_slike;
    }
}
