package br.ufrn.interpolation.application;

public class SensorDTO {
    private String sensorName;
    private double latitude;
    private double longitude;
    private double distanteInMeters;

    public SensorDTO(String sensorName, double latitude, double longitude, double distanteInMeters) {
        this.sensorName = sensorName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanteInMeters = distanteInMeters;
    }

    public String getSensorName() {
        return sensorName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistanteInMeters() {
        return distanteInMeters;
    }
}
