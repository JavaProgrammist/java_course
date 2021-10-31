package ru.otus;

public interface TestLogging {

    void calculation(int param);
    void calculation(int param1, int param2);
    @Log
    void calculation(int param1, double param2);
    void calculation(int param1, int param2, String param3);
}
