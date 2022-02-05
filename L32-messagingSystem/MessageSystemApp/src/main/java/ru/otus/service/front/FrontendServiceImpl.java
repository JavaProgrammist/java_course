package ru.otus.service.front;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.dto.ClientData;
import ru.otus.dto.ClientsData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.model.Client;

@Service
public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(
            @Qualifier("frontendMsClient") MsClient msClient,
            @Value("${my.database_service_client_name}") String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getAllClients(MessageCallback<ClientsData> dataConsumer) {
        var outMsg = msClient.produceMessage(databaseServiceClientName, new ClientsData(),
                MessageType.GET_CLIENTS_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void addClient(Client client, MessageCallback<ClientData> dataConsumer) {
        var outMsg = msClient.produceMessage(databaseServiceClientName, new ClientData(client),
                MessageType.ADD_CLIENT, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
