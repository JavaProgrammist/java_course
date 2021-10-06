package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestExecutorImpl implements TestExecutor {

    private Class<?> clazz;
    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private int successfullyTestCount;
    private int failedTestCount;

    @Override
    public synchronized void executeTest(String className) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        clazz = Class.forName(className);
        beforeMethods = findMethodsByAnnotationName(clazz, Before.class);
        afterMethods = findMethodsByAnnotationName(clazz, After.class);
        List<Method> testMethods = findMethodsByAnnotationName(clazz, Test.class);
        successfullyTestCount = 0;
        failedTestCount = 0;

        for (Method testMethod : testMethods) {
            executeTest(testMethod);
        }
        System.out.printf("Total tests: %d, successfully: %d, failed: %s%n",
                testMethods.size(), successfullyTestCount, failedTestCount);
    }

    private void executeTest(Method testMethod) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        System.out.println(String.format("---Test %s---", testMethod.getName()));
        Object testObject = createTestObject(clazz);
        try {
            try {
                executeMethods(testObject, beforeMethods);
                testMethod.invoke(testObject);
                successfullyTestCount++;
            } finally {
                executeMethods(testObject, afterMethods);
            }
        } catch (Exception ex) {
            failedTestCount++;
            printExceptionMessage(ex);
        }
        System.out.println("------");
    }

    private void executeMethods(Object obj, List<Method> methods) throws InvocationTargetException,
            IllegalAccessException {
        for (Method method : methods) {
            method.invoke(obj);
        }
    }

    private void printExceptionMessage(Exception ex) {
        Throwable targetEx = ex.getCause() == null ? ex : ex.getCause();
        targetEx.printStackTrace(System.out);
    }

    private Object createTestObject(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    private <T> List<Method> findMethodsByAnnotationName(Class<T> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> foundMethods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                foundMethods.add(method);
            }
        }
        return foundMethods;
    }
}
