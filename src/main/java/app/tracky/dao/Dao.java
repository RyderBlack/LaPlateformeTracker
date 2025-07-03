package app.tracky.dao;

import app.tracky.model.BaseEntity;

import java.util.List;

public interface Dao<T extends BaseEntity> {
    T findById(int id);
    List<T> findAll();
    T create(T entity);
    T update(T entity);
    boolean delete(int id);
}
