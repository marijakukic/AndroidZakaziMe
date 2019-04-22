package com.example.marija;

public class Rezervacija {

    private int id;
    private Usluga u;
    private Termin t;
    private boolean aktivna;//ovo definise da li je u aktivnim ili proslim rezervacijama
    private String emailKorisnika;

    public Rezervacija(){}

    public Rezervacija(int id,Usluga u, Termin t, boolean aktivna,String korisnik) {
        this.id = id;
        this.u = u;
        this.t = t;
        this.aktivna = aktivna;
        this.emailKorisnika = korisnik;
    }

    public Usluga getU() {
        return u;
    }

    public void setU(Usluga u) {
        this.u = u;
    }

    public Termin getT() {
        return t;
    }

    public void setT(Termin t) {
        this.t = t;
    }

    public boolean isAktivna() {
        return aktivna;
    }

    public void setAktivna(boolean aktivna) {
        this.aktivna = aktivna;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailKorisnika() {
        return emailKorisnika;
    }

    public void setEmailKorisnika(String emailKorisnika) {
        this.emailKorisnika = emailKorisnika;
    }

    @Override
    public String toString() {
        return "Rezervacija{" +
                "id=" + id +
                ", u=" + u +
                ", t=" + t +
                ", aktivna=" + aktivna +
                ", emailKorisnika='" + emailKorisnika + '\'' +
                '}';
    }
}
