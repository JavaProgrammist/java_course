package ru.otus.sessionmanager;

public interface TransactionManager {

    <T> T doInReadOnlyTransaction(TransactionAction<T> action);

    <T> T doInTransaction(TransactionAction<T> action);
}
