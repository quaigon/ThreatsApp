package com.quaigon.threatsapp.dto;

/**
 * Created by Kamil on 13.03.2016.
 */
public class ThreatType {
    private String uuid;
    private String threatType;

    public ThreatType(String uuid, String threatType) {
        this.uuid = uuid;
        this.threatType = threatType;
    }

    public String getUuid() {
        return uuid;
    }

    public String getThreatType() {
        return threatType;
    }
}
