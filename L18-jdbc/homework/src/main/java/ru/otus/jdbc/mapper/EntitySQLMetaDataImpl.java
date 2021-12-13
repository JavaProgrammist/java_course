package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select %s from %s", joinFieldNames(entityClassMetaData.getAllFields()),
                entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select %s from %s where %s=?", joinFieldNames(entityClassMetaData.getAllFields()),
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        return String.format("insert into %s (%s) values (%s)", entityClassMetaData.getName(),
                joinFieldNames(fieldsWithoutId), joinQuestionMarks(fieldsWithoutId.size()));
    }

    @Override
    public String getUpdateSql() {
        List<Field> allFields = entityClassMetaData.getAllFields();
        return String.format("update %s set %s where %s=?", entityClassMetaData.getName(),
                joinFieldNamesWithQuestionMarks(allFields), entityClassMetaData.getIdField().getName());
    }

    private String joinFieldNames(List<Field> fields) {
        return fields.stream().map(Field::getName).reduce((item1, item2) -> item1 + ", " + item2)
                .orElseThrow(() -> new RuntimeException("Поля не найдены"));
    }

    private String joinFieldNamesWithQuestionMarks(List<Field> fields) {
        return fields.stream().map(Field::getName).reduce((item1, item2) -> item1 + "=?, " + item2 + "=?")
                .orElseThrow(() -> new RuntimeException("Поля не найдены"));
    }

    private String joinQuestionMarks(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("?");
        }
        return stringBuilder.toString();
    }
}
