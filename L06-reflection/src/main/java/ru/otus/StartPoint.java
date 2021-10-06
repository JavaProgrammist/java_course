package ru.otus;

import java.lang.reflect.InvocationTargetException;

public class StartPoint {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestExecutor testExecutor = new TestExecutorImpl();
        testExecutor.executeTest(MathOperationTest.class.getName());
    }
}