package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class FileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper;
    private final Gson gson;

    public FileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
        gson = new Gson();
    }

    @Override
    public List<Measurement> load() throws IOException, URISyntaxException {
        //читает файл, парсит и возвращает результат
        URL resource = FileLoader.class.getClassLoader().getResource(fileName);
        assert resource != null;
        var gsonFileAsString = Files.readString(new File(resource.toURI()).toPath());
        return gson.fromJson(gsonFileAsString, new TypeToken<List<Measurement>>() {}.getType());

        /*try (InputStream inputStream = FileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            MeasurementWrapper[] measurementArr = mapper.readValue(inputStream, MeasurementWrapper[].class);
            return Arrays.stream(measurementArr)
                    .map(MeasurementWrapper::getMeasurement)
                    .collect(Collectors.toList());
        }*/
    }
}
