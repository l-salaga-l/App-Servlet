package org.example.appservlet.service;

import org.example.appservlet.exception.NotFoundException;

public interface Service<T> {
    T save(T t);

    Iterable<T> findAll();

    T findById(Integer id) throws NotFoundException;

    void update(T t) throws NotFoundException;

    void deleteById(Integer id) throws NotFoundException;
}

