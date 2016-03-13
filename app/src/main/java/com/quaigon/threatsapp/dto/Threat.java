package com.quaigon.threatsapp.dto;

/**
 * Created by Kamil on 13.03.2016.
 */
public class Threat {
    private String uuid;
    private ThreatType type;
    private String description;
    private Coordinates coordinates;

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
