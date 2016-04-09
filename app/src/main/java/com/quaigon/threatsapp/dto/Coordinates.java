package com.quaigon.threatsapp.dto;

import org.parceler.Parcel;

@Parcel
public class Coordinates {
    public String vertical;
    public String horizontal;
    public String street;
    public String city;


    public Coordinates() {
    }

    public Coordinates(String vertical, String horizontal, String street, String city) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.street = street;
        this.city = city;
    }

    public String getVertical() {
        return vertical;
    }

    public String getHorizontal() {
        return horizontal;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
