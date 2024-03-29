package com.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(String id);

    List<T> getAll();

    boolean save(T t);

    boolean save(List<T> t);

    boolean update(T t);

    boolean delete(String id);

    List<T> delete(T t);
}
