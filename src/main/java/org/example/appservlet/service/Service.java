package org.example.appservlet.service;

import java.util.List;

public interface Service<T> {
    void save(T t);

    void deleteById(String id);

    void update(T t);

    T findById(String id);

    List<T> findAll();
}

