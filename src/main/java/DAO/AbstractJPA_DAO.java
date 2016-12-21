package DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Admin on 18.12.2016.
 */
public abstract class AbstractJPA_DAO<E extends model.ModelItem> implements DAO<E>{
    protected Class<E> entityClass;

    @PersistenceContext(unitName = "PortalMain")//, type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;

    public AbstractJPA_DAO(Class<E> type) {
        this.entityClass = type;
    }

    public void persist(E entity) {
        entityManager.persist(entity);
//        entityManager.flush();
    }

    public void remove(E entity) {
        entityManager.remove(entity);
      //  entityManager.flush();
    }

    public void update(E entity){
        entityManager.merge(entity);
      //  entityManager.flush();
    }

    public void removeById(Long id){
        remove(findById(id));
    }

    public E findById(Long id) { return entityManager.find(entityClass, id); }

    public Class<E> getType(){
        return entityClass;
    }

    public List<E> findAll(){
        return entityManager.createQuery("SELECT up FROM " + entityClass.getSimpleName() + " up", entityClass).getResultList();
    }
}
