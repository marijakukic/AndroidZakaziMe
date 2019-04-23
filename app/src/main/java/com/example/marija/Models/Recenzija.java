package com.example.marija.Models;

public class Recenzija {

    private String komentar;
    private String ocena;
    private String emailKorinika;
    private int idUsluge;

    public Recenzija(){}

    public Recenzija(String komentar, String ocena, String emailKorinika, int idUsluge) {
        this.komentar = komentar;
        this.ocena = ocena;
        this.emailKorinika = emailKorinika;
        this.idUsluge = idUsluge;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getOcena() {
        return ocena;
    }

    public void setOcena(String ocena) {
        this.ocena = ocena;
    }

    public String getEmailKorinika() {
        return emailKorinika;
    }

    public void setEmailKorinika(String emailKorinika) {
        this.emailKorinika = emailKorinika;
    }

    public int getIdUsluge() {
        return idUsluge;
    }

    public void setIdUsluge(int idUsluge) {
        this.idUsluge = idUsluge;
    }
}
