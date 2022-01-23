package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.AppComponentsContainerException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws AppComponentsContainerException {
        try {
            processConfig(initialConfigClass);
        } catch (Exception e) {
            throw new AppComponentsContainerException(e);
        }
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

    private void processConfig(Class<?> configClass) throws AppComponentsContainerException {
        try {
            checkConfigClass(configClass);

            List<AppComponentMethodInfo> compMethodInfoList = getAppComponentMethodInfoList(configClass);
            Object configObject = configClass.getConstructor().newInstance();
            for (AppComponentMethodInfo compMethodInfo : compMethodInfoList) {
                if (compMethodInfoList.stream()
                        .anyMatch(item -> item.name.equals(compMethodInfo.getName()) && item != compMethodInfo)) {
                    throw new AppComponentsContainerException(String.format(
                            "Было найдено по названию %s больше одного компонента", compMethodInfo.getName()));
                }
                Object component = invokeComponentMethod(configObject, compMethodInfo.getMethod());
                appComponents.add(component);
                appComponentsByName.put(compMethodInfo.getName(), component);
            }
        } catch (Exception e) {
            throw new AppComponentsContainerException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<AppComponentMethodInfo> getAppComponentMethodInfoList(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .map(m -> new AppComponentMethodInfo(m, m.getAnnotation(AppComponent.class).order(),
                        m.getAnnotation(AppComponent.class).name()))
                .collect(Collectors.toList());
    }

    private Object invokeComponentMethod(Object configObject, Method method) {
        try {
            List<Object> methodArguments = new ArrayList<>();
            for (Class<?> paramType : method.getParameterTypes()) {
                methodArguments.add(getAppComponentByType(paramType));
            }
            return method.invoke(configObject, methodArguments.toArray());
        } catch (Exception e) {
            throw new AppComponentsContainerException(e);
        }
    }

    private <C> C getAppComponentByType(Class<C> componentClass) {
        List<Object> suitableComponents = appComponents.stream()
                .filter(item -> componentClass.isAssignableFrom(item.getClass()))
                .collect(Collectors.toList());
        if (suitableComponents.size() == 0) {
            throw new AppComponentsContainerException(String.format("Не найден компонент по типу %s",
                    componentClass.getName()));
        } else if (suitableComponents.size() > 1) {
            throw new AppComponentsContainerException(String.format(
                    "Было найдено по типу %s больше одного компонента", componentClass.getName()));
        }
        return (C) suitableComponents.get(0);

    }

    private static class AppComponentMethodInfo {
        private final Method method;
        private final int order;
        private final String name;

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
