package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        //Проверяю, было ли уже сообщение с аналогичным идентификатором, поскольку есть findMessageById, по которому
        //я так понимаю, что у всех сообщений должны быть уникальные идентификаторы.
        if (history.containsKey(msg.getId())) {
            throw new RuntimeException(String.format("Сообщение с идентификатором %s уже было", msg.getId()));
        }
        history.put(msg.getId(), new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        if (history.containsKey(id)) {
            return Optional.of(history.get(id));
        } else {
            return Optional.empty();
        }
    }
}
