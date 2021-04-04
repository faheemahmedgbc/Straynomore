package com.example.straynomore;

public class Shelter {

    private String name, location;

    public Shelter(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Shelter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
