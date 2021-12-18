package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private final String className;
    private final Constructor<T> classConstructor;
    private final Field classIdField;
    private final List<Field> allClassFields;
    private final List<Field> classFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        className = clazz.getSimpleName();
        try {
            classConstructor = clazz.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        classIdField = Arrays.stream(clazz.getDeclaredFields())
                .filter(item -> item.isAnnotationPresent(Id.class)).findFirst()
                .orElseThrow(() -> new RuntimeException("Не найдено поле с аннотацией id"));
        allClassFields = Arrays.asList(clazz.getDeclaredFields());
        classFieldsWithoutId = allClassFields.stream()
                .filter(item -> !item.getName().equals(classIdField.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return classConstructor;
    }

    @Override
    public Field getIdField() {
        return classIdField;
    }

    @Override
    public List<Field> getAllFields() {
        return allClassFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return classFieldsWithoutId;
    }
}
