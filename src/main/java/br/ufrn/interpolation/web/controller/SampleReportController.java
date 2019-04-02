package br.ufrn.interpolation.web.controller;

import br.ufrn.interpolation.application.SampleReporter;
import br.ufrn.interpolation.application.SampleQueryResult;
import br.ufrn.interpolation.web.controller.request.TemperatureReportRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperature")
public class SampleReportController {

    private final SampleReporter sampleReporter;

    public SampleReportController(SampleReporter sampleReporter) {
        this.sampleReporter = sampleReporter;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json" )
    public SampleQueryResult getTemperature(@RequestBody TemperatureReportRequest request){
        return sampleReporter.evaluateTemperature(request.getLatitude(), request.getLongitude(), request.getLocalDateTime());
    }

}
