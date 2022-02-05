package ru.otus.messagesystem.message;

public enum MessageType {
    VOID_MESSAGE("voidMessage"),
    GET_CLIENTS_DATA("GetClientsData"),
    ADD_CLIENT("AddClient");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
