package org.example.appservlet.db;

import org.hibernate.Session;

/**
 * Функциональный интерфейс для операций с транзакциями.
 *
 * @param <T> тип результата
 */
@FunctionalInterface
public interface TransactionOperation<T> {
    T execute(Session session);
}
