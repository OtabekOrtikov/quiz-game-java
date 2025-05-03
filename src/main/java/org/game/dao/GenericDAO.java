package org.game.dao;

import java.io.IOException;
import java.util.List;

public interface GenericDAO<T> {
    List<T> findAll() throws IOException;
    void save(T object) throws IOException;
    void saveAll(List<T> objects) throws IOException;
}
