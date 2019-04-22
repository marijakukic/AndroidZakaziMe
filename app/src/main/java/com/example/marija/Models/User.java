package com.example.marija.Models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private long ID;
    private String Name;
    private String koriscnickoIme;
    private String email;
    private String pass;
    private String prezime;



    public User(){}

    public User(long ID,String Name,String koriscnickoIme,String email,String pass,String prezime) {
        this.ID = ID;
        this.Name = Name;
        this.koriscnickoIme = koriscnickoIme;
        this.email = email;
        this.pass = pass;
        this.prezime = prezime;

    }

    public User(String Name,String koriscnickoIme,String email,String pass,String prezime) {
        this.Name = Name;
        this.koriscnickoIme = koriscnickoIme;
        this.email = email;
        this.pass = pass;
        this.prezime = prezime;

    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKoriscnickoIme() {
        return koriscnickoIme;
    }

    public void setKoriscnickoIme(String koriscnickoIme) {
        this.koriscnickoIme = koriscnickoIme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }


   /* public List<Rezervacija> getListaAktivnih(){
        List<Rezervacija> listaAktivnih = new ArrayList<Rezervacija>();
        for(Rezervacija r :listaRezervacija){
            if(r.isAktivna()){
                listaAktivnih.add(r);
            }
        }

        return listaAktivnih;
    }

    public List<Rezervacija> getListaProslih(){
        List<Rezervacija> listaProslih = new ArrayList<Rezervacija>();
        for(Rezervacija r :listaRezervacija){
            if(!r.isAktivna()){
                listaProslih.add(r);
            }
        }

        return listaProslih;
    }

    public void addToListaAktivnih(Rezervacija r){
        r.setAktivna(true);
        listaRezervacija.add(r);
    }

    public void addToListProslih(Rezervacija r){
        r.setAktivna(false);
        listaRezervacija.add(r);
    }*/


}
