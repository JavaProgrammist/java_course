package ru.otus;

public class StartPoint {

    public static void main(String[] args) {
        TestLogging testLogging = IoC.createTestLogging();
        testLogging.calculation(1);
        testLogging.calculation(1, 2);
        testLogging.calculation(1, 3.0);
        testLogging.calculation(3, 2, "один");
    }
}
