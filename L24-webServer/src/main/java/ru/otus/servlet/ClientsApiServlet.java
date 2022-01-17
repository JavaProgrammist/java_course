package ru.otus.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Client;
import ru.otus.model.ClientDto;
import ru.otus.service.ClientConverter;
import ru.otus.service.DBServiceClient;

import java.io.IOException;
import java.util.stream.Collectors;


@MultipartConfig
public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;
    private final ClientConverter clientConverter;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson,
                             ClientConverter clientConverter) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.clientConverter = clientConverter;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lines = request.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        ClientDto clientDto = objectMapper.readValue(lines, ClientDto.class);
        Client client = clientConverter.convert(clientDto);
        client = dbServiceClient.saveClient(client);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(objectMapper.writeValueAsString(clientConverter.convert(client)));
    }


}
