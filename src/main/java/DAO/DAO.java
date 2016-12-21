package DAO;

import java.util.List;

/**
 * Created by Admin on 18.12.2016.
 */
public interface DAO<E extends model.ModelItem> {
    void persist(E entity);
    void remove(E entity);
    E findById(Long id);
    void  update(E entity);
    void removeById(Long id);
    List<E> findAll();
}
