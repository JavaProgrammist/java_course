package ru.otus.jdbc.mapper;

import org.apache.commons.lang3.reflect.FieldUtils;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id),
                this::convertToEntity);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(),
                this::convertToEntityList)
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> fieldValues = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        return FieldUtils.readField(field, client, true);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Field> fields = new ArrayList<>(entityClassMetaData.getFieldsWithoutId());
        fields.add(entityClassMetaData.getIdField());
        List<Object> fieldValues = fields.stream()
                .map(field -> {
                    try {
                        return FieldUtils.readField(field, client, true);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldValues);
    }

    private T convertToEntity(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                T obj = entityClassMetaData.getConstructor().newInstance();
                int index = 1;
                for (Field field : entityClassMetaData.getAllFields()) {
                    FieldUtils.writeField(field, obj, resultSet.getObject(index), true);
                    index++;
                }
                return obj;
            }
            return null;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<T> convertToEntityList(ResultSet resultSet) {
        try {
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T obj = entityClassMetaData.getConstructor().newInstance();
                int index = 1;
                for (Field field : entityClassMetaData.getAllFields()) {
                    FieldUtils.writeField(field, obj, resultSet.getObject(index), true);
                    index++;
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
