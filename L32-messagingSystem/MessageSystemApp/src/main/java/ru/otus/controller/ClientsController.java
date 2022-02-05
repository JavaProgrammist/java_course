package ru.otus.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.ClientDto;
import ru.otus.model.Phone;
import ru.otus.service.ClientConverter;
import ru.otus.service.front.FrontendService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientsController {

    private final ClientConverter clientConverter;
    private final SimpMessagingTemplate template;
    private final FrontendService frontendService;

    public ClientsController(ClientConverter clientConverter, SimpMessagingTemplate template,
                             FrontendService frontendService) {
        this.clientConverter = clientConverter;
        this.template = template;
        this.frontendService = frontendService;
    }

    @GetMapping("/clients")
    public String getClientsPage() {
        return "clients";
    }

    @MessageMapping("/message.clients")
    public void getAllClients() {
        frontendService.getAllClients(data -> template.convertAndSend("/topic/response.clients",
                data.getClients().stream().map(clientConverter::convert).collect(Collectors.toList())));
    }

    @MessageMapping("/message.clients.add")
    public void addClient(ClientDto clientDto) {
        Client client = clientConverter.convert(clientDto);
        frontendService.addClient(client, data -> template.convertAndSend("/topic/response.clients.add",
                        clientConverter.convert(data.getClient())));
    }
}
