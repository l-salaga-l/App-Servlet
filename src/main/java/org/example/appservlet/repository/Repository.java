package org.example.appservlet.repository;

import java.util.Optional;

public interface Repository<T, ID> {
    T save(T t);

    void deleteById(ID id);

    boolean existsById(ID id);

    void update(T t);

    Optional<T> findById(ID id);

    Iterable<T> findAll();
}
