package br.ufrn.interpolation.web.controller;

import br.ufrn.interpolation.application.TemperatureQueryResult;
import br.ufrn.interpolation.application.TemperatureReporter;
import br.ufrn.interpolation.web.controller.request.TemperatureReportRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperature")
public class TemperatureReportController {

    private final TemperatureReporter temperatureReporter;

    public TemperatureReportController(TemperatureReporter temperatureReporter) {
        this.temperatureReporter = temperatureReporter;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json" )
    public TemperatureQueryResult getTemperature(@RequestBody TemperatureReportRequest request){
        return temperatureReporter.evaluateTemperature(request.getLatitude(), request.getLongitude(), request.getLocalDateTime());
    }

}
