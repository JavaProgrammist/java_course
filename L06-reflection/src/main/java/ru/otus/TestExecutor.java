package ru.otus;

import java.lang.reflect.InvocationTargetException;

public interface TestExecutor {

    void executeTest(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
