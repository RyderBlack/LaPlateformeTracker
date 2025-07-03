package app.tracky.service;

import app.tracky.model.BaseEntity;

import java.util.List;

public interface Service<T extends BaseEntity> {
    T findById(int id);
    List<T> findAll();
    T create(T entity);
    T update(T entity);
    boolean delete(int id);
}
