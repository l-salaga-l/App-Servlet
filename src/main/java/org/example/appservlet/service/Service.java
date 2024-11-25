package org.example.appservlet.service;

import org.hibernate.ObjectNotFoundException;

public interface Service<T> {
    T save(T t);

    void deleteById(Integer id) throws ObjectNotFoundException;

    void update(T t) throws ObjectNotFoundException;

    T findById(Integer id) throws ObjectNotFoundException;

    Iterable<T> findAll();
}

