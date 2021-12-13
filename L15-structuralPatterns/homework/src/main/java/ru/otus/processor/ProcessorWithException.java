package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorWithException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorWithException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        LocalDateTime now = dateTimeProvider.getDate();
        System.out.println(now);
        if (now.getSecond() % 2 == 0) {
            throw new RuntimeException("Четная секунда");
        }
        return message;
    }
}
