package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;

@Data
public class Coordinates {
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double accuracy;
    private Double altitudeAccuracy;
    private Double heading;
    private Double speed;
}
