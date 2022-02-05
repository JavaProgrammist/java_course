package ru.otus.service.db.handlers;

import ru.otus.dto.ClientsData;
import ru.otus.service.db.DBServiceClient;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;


public class GetClientsDataRequestHandler implements RequestHandler {
    private final DBServiceClient dbService;

    public GetClientsDataRequestHandler(DBServiceClient dbService) {
        this.dbService = dbService;
    }

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        var data = new ClientsData(dbService.findAll());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, (T) data));
    }
}
