package com.at.service;

import java.util.List;

/**
 * Base service crud operation interface.
 */
public interface BaseCrudService<T, K> {

  T findById(K id);

  List<T> fetchAll();

  void delete(K id);

  T create(T obj);

  T update(K id, T obj);
}
