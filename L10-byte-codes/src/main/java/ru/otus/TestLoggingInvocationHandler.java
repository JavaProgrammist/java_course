package ru.otus;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

@AllArgsConstructor
public class TestLoggingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private TestLogging testLogging;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (hasMethodImplLogAnnotation(method)) {
            logMethod(method, args);
        }
        return method.invoke(testLogging, args);
    }

    //Возвращает признак того, что метод имеет аннотацию Log.
    /*private boolean hasMethodLogAnnotation(Method method) {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(Log.class)) {
                return true;
            }
        }
        return false;
    }*/

    //Возвращает признак того, что реализация метода интерфейса TestLogging имеет аннотацию Log.
    private boolean hasMethodImplLogAnnotation(Method interfaceMethod) {
        Optional<Method> MethodImplOpt = findMethodImplementation(interfaceMethod);
        if (MethodImplOpt.isEmpty()) {
            return false;
        }
        for (Annotation annotation : MethodImplOpt.get().getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(Log.class)) {
                return true;
            }
        }
        return false;
    }

    //Возвращает реализацию метода интерфейса TestLogging.
    private Optional<Method> findMethodImplementation(Method interfaceMethod) {
        outerLoop: for (Method methodImpl : testLogging.getClass().getMethods()) {
            if (!interfaceMethod.getName().equals(methodImpl.getName())) {
                continue;
            } else if (interfaceMethod.getParameterCount() != methodImpl.getParameterCount()) {
                continue;
            } else {
                Class<?>[] methodParamTypes = interfaceMethod.getParameterTypes();
                Class<?>[] methodImplParamTypes = methodImpl.getParameterTypes();
                for (int i = 0; i < methodParamTypes.length; i++) {
                    if (!methodParamTypes[i].equals(methodImplParamTypes[i])) {
                        continue outerLoop;
                    }
                }
            }
            return Optional.of(methodImpl);
        }
        return Optional.empty();
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
