package com.example.marija;

public class User {

    private long ID;
    private String Name;
    private String koriscnickoIme;
    private String email;
    private String pass;

    public User(){}

    public User(long ID,String Name,String koriscnickoIme,String email,String pass) {
        this.ID = ID;
        this.Name = Name;
        this.koriscnickoIme = koriscnickoIme;
        this.email = email;
        this.pass = pass;
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
}
