package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        //формирует результирующий json и сохраняет его в файл
        String str = mapper.writeValueAsString(data);
        Path path = Paths.get(fileName);
        Files.writeString(path, str);
    }
}
