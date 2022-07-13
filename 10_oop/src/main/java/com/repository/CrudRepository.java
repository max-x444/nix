package com.repository;

import java.util.List;

public interface CrudRepository<T> {
    T getById(String id);

    List<T> getAll();

    boolean create(T t);

    boolean create(List<T> t);

    boolean update(T t);

    boolean delete(String id);

    List<T> delete(T t);
}
