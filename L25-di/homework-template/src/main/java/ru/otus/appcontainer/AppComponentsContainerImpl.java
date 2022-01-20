package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws
            InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return getAppComponentByType(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException(String.format("Не найден компонент по названию '%s'", componentName));
        }
        return (C) component;
    }

    private void processConfig(Class<?> configClass)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            InstantiationException {
        checkConfigClass(configClass);

        List<AppComponentMethodInfo> compMethodInfoList = getAppComponentMethodInfoList(configClass);
        Object configObject = configClass.getConstructor().newInstance();
        for (AppComponentMethodInfo compMethodInfo : compMethodInfoList) {
            Object component = invokeComponentMethod(configObject, compMethodInfo.getMethod());
            appComponents.add(component);
            appComponentsByName.put(compMethodInfo.getName(), component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<AppComponentMethodInfo> getAppComponentMethodInfoList(Class<?> configClass)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<AppComponentMethodInfo> methodInfoList = new ArrayList<>();
        for (Method method : configClass.getMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                Annotation appComponentAnnotation = method.getAnnotation(AppComponent.class);
                Method orderMethod = appComponentAnnotation.annotationType().getMethod("order");
                Integer order = (Integer) orderMethod.invoke(appComponentAnnotation);

                Method componentNameMethod = appComponentAnnotation.annotationType().getMethod("name");
                String componentName = (String) componentNameMethod.invoke(appComponentAnnotation);

                AppComponentMethodInfo methodInfo = new AppComponentMethodInfo(method, order, componentName);
                methodInfoList.add(methodInfo);
            }
        }
        return methodInfoList.stream().sorted(Comparator.comparing(AppComponentMethodInfo::getOrder))
                .collect(Collectors.toList());
    }

    private Object invokeComponentMethod(Object configObject, Method method)
            throws InvocationTargetException, IllegalAccessException {
        List<Object> methodArguments = new ArrayList<>();
        for (Class<?> paramType : method.getParameterTypes()) {
            methodArguments.add(getAppComponentByType(paramType));
        }
        return method.invoke(configObject, methodArguments.toArray());
    }

    private <C> C getAppComponentByType(Class<C> componentClass) {
        List<Object> suitableComponents = appComponents.stream()
                .filter(item -> componentClass.isAssignableFrom(item.getClass()))
                .collect(Collectors.toList());
        if (suitableComponents.size() == 0) {
            throw new RuntimeException(String.format("Не найден компонент по типу %s", componentClass.getName()));
        } else if (suitableComponents.size() > 2) {
            throw new RuntimeException(String.format("Было найдено по типу %s больше одного компонента",
                    componentClass.getName()));
        }
        return (C) suitableComponents.get(0);

    }

    private class AppComponentMethodInfo {
        private Method method;
        private int order;
        private String name;

        public AppComponentMethodInfo(Method method, int order, String name) {
            this.method = method;
            this.order = order;
            this.name = name;
        }

        public Method getMethod() {
            return method;
        }

        public int getOrder() {
            return order;
        }

        public String getName() {
            return name;
        }
    }
}
