package br.ufrn.interpolation.application.actors;

import akka.actor.AbstractActor;
import br.ufrn.interpolation.application.SampleReporter;

import java.time.LocalDateTime;

public final class ReportActor extends AbstractActor {

    private SampleReporter sampleReporter;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RequestReport.class, requestReport -> getSender().tell(sampleReporter.evaluateTemperature(requestReport.latitude,requestReport.longitude, requestReport.dateTime), getSelf()))
                .build();
    }

    public static final class RequestReport {

        private double latitude;
        private double longitude;
        private LocalDateTime dateTime;

        public RequestReport(double latitude, double longitude, LocalDateTime dateTime) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.dateTime = dateTime;
        }
    }

}
