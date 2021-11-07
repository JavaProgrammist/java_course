package ru.otus;

import java.lang.reflect.Proxy;

public class IoC {

    public static TestLogging createTestLogging() {
        TestLogging testLogging = new TestLoggingImpl();
        TestLoggingInvocationHandler invocationHandler = new TestLoggingInvocationHandler(testLogging);
        return (TestLogging) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[] {TestLogging.class}, invocationHandler);
    }
}
