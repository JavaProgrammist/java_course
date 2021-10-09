package ru.otus;

public class TestLoggingImpl implements TestLogging {
    @Log
    @Override
    public void calculation(int param) {
        System.out.println("in calculation(int param)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("in calculation(int param1, int param2)");

    }

    @Override
    public void calculation(int param1, double param2) {
        System.out.println("in calculation(int param1, double param2)");

    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("in calculation(int param1, int param2, String param3)");

    }
}
