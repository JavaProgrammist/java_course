package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorWithException implements Processor {
    @Override
    public Message process(Message message) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        if (now.getSecond() % 2 == 0) {
            throw new RuntimeException("Четная секунда");
        }
        return message;
    }
}
