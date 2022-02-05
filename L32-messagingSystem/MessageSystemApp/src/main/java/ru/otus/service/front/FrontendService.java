package ru.otus.service.front;

import ru.otus.dto.ClientData;
import ru.otus.dto.ClientsData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.model.Client;

public interface FrontendService {
    void getAllClients(MessageCallback<ClientsData> dataConsumer);

    void addClient(Client client, MessageCallback<ClientData> dataConsumer);
}

