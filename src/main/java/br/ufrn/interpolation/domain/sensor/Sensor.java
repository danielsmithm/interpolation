package br.ufrn.interpolation.domain.sensor;

import java.time.LocalDateTime;

public class Sensor {

    private final double baseHeight;
    private final Boolean active;
    private final Source source;
    private final Double sensorHeight;
    private final String name;
    private final LocalDateTime latest;
    private final String type;
    private final double longitude;
    private final double latitude;

    public Sensor(double baseHeight, Boolean active, Source source, Double sensorHeight, String name, LocalDateTime latest, String type, double longitude, double latitude) {
        this.baseHeight = baseHeight;
        this.active = active;
        this.source = source;
        this.sensorHeight = sensorHeight;
        this.name = name;
        this.latest = latest;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getBaseHeight() {
        return baseHeight;
    }

    public Boolean isActive() {
        return active;
    }

    public Source getSource() {
        return source;
    }

    public Double getSensorHeight() {
        return sensorHeight;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLatest() {
        return latest;
    }

    public String getType() {
        return type;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
