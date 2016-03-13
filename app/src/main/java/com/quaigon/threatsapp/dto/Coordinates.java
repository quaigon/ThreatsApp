package com.quaigon.threatsapp.dto;

/**
 * Created by Kamil on 13.03.2016.
 */
public class Coordinates {
    private String vertical;
    private String horizontal;

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
}
