package com.example.marija.Models;

public class Kategorija {

    private String naziv;
    private int id;

    public Kategorija() {
    }

    public Kategorija(String naziv,int id) {
        this.naziv = naziv;
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
