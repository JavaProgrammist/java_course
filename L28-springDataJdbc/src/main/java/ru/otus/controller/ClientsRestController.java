package ru.otus.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.Client;
import ru.otus.model.ClientDto;
import ru.otus.service.ClientConverter;
import ru.otus.service.DBServiceClient;

@RestController
@RequestMapping("/api/clients")
public class ClientsRestController {

    private final ClientConverter clientConverter;
    private final DBServiceClient dbServiceClient;

    public ClientsRestController(ClientConverter clientConverter, DBServiceClient dbServiceClient) {
        this.clientConverter = clientConverter;
        this.dbServiceClient = dbServiceClient;
    }

    @PostMapping
    public ClientDto addClient(@RequestBody ClientDto clientDto) {
        Client client = clientConverter.convert(clientDto);
        return clientConverter.convert(dbServiceClient.saveClient(client));
    }
}
