package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper;

    public FileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() throws IOException {
        //читает файл, парсит и возвращает результат
        InputStream inputStream = FileLoader.class.getClassLoader().getResourceAsStream(fileName);
        MeasurementWrapper[] measurementArr = mapper.readValue(inputStream, MeasurementWrapper[].class);
        return Arrays.stream(measurementArr)
                .map(MeasurementWrapper::getMeasurement)
                .collect(Collectors.toList());
    }
}
