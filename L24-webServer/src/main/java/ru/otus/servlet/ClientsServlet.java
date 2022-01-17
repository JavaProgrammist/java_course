package ru.otus.servlet;

import ru.otus.model.ClientDto;
import ru.otus.service.ClientConverter;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.DBServiceClient;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;
    private final ClientConverter clientConverter;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient,
                          ClientConverter clientConverter) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.clientConverter = clientConverter;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<ClientDto> clientDtoList = dbServiceClient.findAll().stream()
                .map(clientConverter::convert).collect(Collectors.toList());
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clientDtoList);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

}
