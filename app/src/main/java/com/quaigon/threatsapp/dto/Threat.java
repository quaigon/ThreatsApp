package com.quaigon.threatsapp.dto;


import org.parceler.Parcel;

@Parcel
public class Threat {
    public String uuid;
    public ThreatType type;
    public String description;
    public Coordinates coordinates;


    public Threat() {
    }

    public Threat(String uuid, ThreatType type, String description, Coordinates coordinates) {
        this.uuid = uuid;
        this.type = type;
        this.description = description;
        this.coordinates = coordinates;
    }


    public String getUuid() {
        return uuid;
    }

    public ThreatType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

}


