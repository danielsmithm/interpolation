package br.ufrn.interpolation.application;

import java.util.List;

public class TemperatureQueryResult {

    private double temperatureValue;

    private List<SensorDTO> sensorDTOS;

    public TemperatureQueryResult(double temperatureValue, List<SensorDTO> sensorDTOS) {
        this.temperatureValue = temperatureValue;
        this.sensorDTOS = sensorDTOS;
    }

    public double getTemperatureValue() {
        return temperatureValue;
    }

    public List<SensorDTO> getSensorDTOS() {
        return sensorDTOS;
    }

    public static class SensorDTO {
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

}
