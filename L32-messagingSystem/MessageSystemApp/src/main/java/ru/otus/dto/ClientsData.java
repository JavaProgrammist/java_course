package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsData implements ResultDataType {
    private final List<Client> clients;

    public ClientsData() {
        clients = new ArrayList<>();
    }

    public ClientsData(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }
}
