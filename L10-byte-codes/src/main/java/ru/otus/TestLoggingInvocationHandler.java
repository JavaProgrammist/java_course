package ru.otus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TestLoggingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private TestLogging testLogging;
    private Set<Method> methodsWithLog;

    public TestLoggingInvocationHandler(TestLogging testLogging) {
        this.testLogging = testLogging;
        methodsWithLog = Arrays.stream(testLogging.getClass().getDeclaredMethods())
                .filter(item -> item.isAnnotationPresent(Log.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (hasMethodImplLogAnnotation(method)) {
            logMethod(method, args);
        }
        return method.invoke(testLogging, args);
    }

    //Возвращает признак того, что реализация метода интерфейса TestLogging имеет аннотацию Log.
    private boolean hasMethodImplLogAnnotation(Method interfaceMethod) {
        return methodsWithLog.stream()
                .anyMatch(item -> item.getName().equals(interfaceMethod.getName()) &&
                        Arrays.equals(item.getParameterTypes(), interfaceMethod.getParameterTypes()));
    }

    //Производит запись в лог информации о методе.
    private void logMethod(Method method, Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("executed method: %s, param: ", method.getName()));
        int index = 0;
        for (Object arg : args) {
            if (index > 0) stringBuilder.append(", ");
            stringBuilder.append(arg == null ? "null" : arg.toString());
            index++;
        }
        System.out.println(stringBuilder);
    }
}
