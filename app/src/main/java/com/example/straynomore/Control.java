package com.example.straynomore;

public class Control {

    private String name, location;

    public Control(String name, String address) {
        this.name = name;
        this.location = address;
    }

    public Control(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }
}
