package com.quaigon.threatsapp.dto;

/**
 * Created by Kamil on 13.03.2016.
 */
public class Coordinates {
    private String vertical;
    private String horizontal;
    private String street;
    private String city;

    public Coordinates(String vertical, String horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
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
