package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.model.ClientDto;
import ru.otus.service.ClientConverter;
import ru.otus.service.DBServiceClient;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientsController {

    private final ClientConverter clientConverter;
    private final DBServiceClient dbServiceClient;

    public ClientsController(ClientConverter clientConverter, DBServiceClient dbServiceClient) {
        this.clientConverter = clientConverter;
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping
    public String getAllClients(Model model) {
        List<ClientDto> clientDtoList = dbServiceClient.findAll().stream()
                .map(clientConverter::convert).collect(Collectors.toList());
        model.addAttribute("clients", clientDtoList);
        return "clients";
    }
}
