package ru.otus.service.db.handlers;

import ru.otus.dto.ClientData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.service.db.DBServiceClient;

import java.util.Optional;

public class AddClientRequestHandler implements RequestHandler {
    private final DBServiceClient dbService;

    public AddClientRequestHandler(DBServiceClient dbService) {
        this.dbService = dbService;
    }

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        var clientData = (ClientData) msg.getData();
        var data = new ClientData(dbService.saveClient(clientData.getClient()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, (T) data));
    }
}
