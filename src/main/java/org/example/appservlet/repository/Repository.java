package org.example.appservlet.repository;

import org.hibernate.ObjectNotFoundException;

import java.util.Optional;

public interface Repository<T, ID> {
    T save(T t);

    void deleteById(ID id) throws ObjectNotFoundException;

    void update(T t);

    Optional<T> findById(ID id) throws ObjectNotFoundException;

    Iterable<T> findAll();

    boolean existsById(ID id);
}
