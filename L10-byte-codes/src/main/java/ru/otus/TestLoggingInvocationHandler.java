package ru.otus;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.Optional;

@AllArgsConstructor
public class TestLoggingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private TestLogging testLogging;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1 вариант: указать аннотации в интерфейсе
        if (method.isAnnotationPresent(Log.class)) {
            logMethod(method, args);
        //2 вариант: указать аннотации в классе, реализующем интерфейс
        } else if (hasMethodImplLogAnnotation(method)) {
            logMethod(method, args);
        }
        return method.invoke(testLogging, args);
    }

    //Возвращает признак того, что реализация метода интерфейса TestLogging имеет аннотацию Log.
    private boolean hasMethodImplLogAnnotation(Method interfaceMethod) {
        try {
            Method foundMethod = testLogging.getClass().getMethod(interfaceMethod.getName(),
                    interfaceMethod.getParameterTypes());
            return foundMethod.isAnnotationPresent(Log.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
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
