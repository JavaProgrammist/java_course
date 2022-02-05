package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.Client;

public class ClientData implements ResultDataType {
    private final Client client;

    public ClientData(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "client=" + client +
                '}';
    }
}
