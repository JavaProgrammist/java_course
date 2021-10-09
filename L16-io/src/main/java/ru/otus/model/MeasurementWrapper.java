package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementWrapper {

    @JsonIgnore
    private Measurement measurement;

    @JsonCreator
    public MeasurementWrapper(@JsonProperty("name") String name,
                              @JsonProperty("value") double value) {
        this.measurement = new Measurement(name, value);
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
}
