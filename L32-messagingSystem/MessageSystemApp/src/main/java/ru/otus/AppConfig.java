package ru.otus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.service.db.DBServiceClient;
import ru.otus.service.db.handlers.AddClientRequestHandler;
import ru.otus.service.db.handlers.GetClientsDataRequestHandler;
import ru.otus.service.front.handlers.ClientOperationResultHandler;

@Configuration
public class AppConfig {

    @Value("${my.frontend_service_client_name}")
    private String frontendServiceClientName;

    @Value("${my.database_service_client_name}")
    private String databaseServiceClientName;

    @Bean
    public MessageSystem messageSystem() {
        MessageSystem messageSystem = new MessageSystemImpl();
        //MsClient databaseMsClient = databaseMsClient(dbServiceClient, messageSystem);
        //messageSystem.addClient(databaseMsClient);
        //MsClient frontendMsClient = frontendMsClient(messageSystem);
        //messageSystem.addClient(frontendMsClient);
        return messageSystem;
    }

    @Bean
    public MsClient databaseMsClient(DBServiceClient dbServiceClient, MessageSystem messageSystem) {
        var requestHandlerStore = new HandlersStoreImpl();
        requestHandlerStore.addHandler(MessageType.GET_CLIENTS_DATA, new GetClientsDataRequestHandler(dbServiceClient));
        requestHandlerStore.addHandler(MessageType.ADD_CLIENT, new AddClientRequestHandler(dbServiceClient));
        MsClient databaseMsClient = new MsClientImpl(databaseServiceClientName, messageSystem, requestHandlerStore);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    public MsClient frontendMsClient(MessageSystem messageSystem) {
        var requestHandlersStore = new HandlersStoreImpl();
        requestHandlersStore.addHandler(MessageType.GET_CLIENTS_DATA, new ClientOperationResultHandler());
        requestHandlersStore.addHandler(MessageType.ADD_CLIENT, new ClientOperationResultHandler());
        MsClient frontendMsClient = new MsClientImpl(frontendServiceClientName, messageSystem, requestHandlersStore);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

}
