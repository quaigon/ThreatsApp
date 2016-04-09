package com.quaigon.threatsapp.dto;

import org.parceler.Parcel;


@Parcel
public class ThreatType {
    public String uuid;
    public String threatType;


    public ThreatType() {
    }

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
