package com.ramonmr95.app.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.ramonmr95.app.interceptors.LoggingInterceptor;

/**
 * Class used to perform the basic CRUD operations.
 * 
 * @author Ramón Moñino Rubio
 *
 * @param <T> Entity to be used.
 * @param <K> Type of the id.
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class PersistenceService<T, K> {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	/**
	 * Gets all of the entities of a given class contained in the db.
	 * 
	 * @param c Class of the entity.
	 * @return entities List that contains all of the entities.
	 */
	public List<T> getEntities(Class<T> c) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(c);
		Root<T> r = cq.from(c);
		cq.select(r);
		TypedQuery<T> query = this.em.createQuery(cq);
		return query.getResultList();
	}

	/**
	 * 
	 * Gets an entity given its class and id.
	 * 
	 * @param c  Class of the entity.
	 * @param id Id of the entity
	 * @return entity if found, null if not.
	 */
	public T getEntityByID(Class<T> c, K id) {
		return this.em.find(c, id);
	}

	/**
	 * 
	 * Gets a list of entities given its class, field to filter by and name.
	 * 
	 * @param c     Class of the entity.
	 * @param field Field to compare.
	 * @param name  Value of the field.
	 * @return list of entities
	 */
	public List<T> getEntityByField(Class<T> c, String field, String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(c);
		Root<T> r = cq.from(c);
		cq.where(cb.like(r.get(field), name));
		TypedQuery<T> query = this.em.createQuery(cq);
		return query.getResultList();
	}

	/**
	 * 
	 * Persists a given entity.
	 * 
	 * @param entity Entity to be persisted.
	 */
	public void persistEntity(T entity) {
		this.em.persist(entity);
	}

	/**
	 * 
	 * Updates an existing entity.
	 * 
	 * @param entity Entity to be updated.
	 */
	public void mergeEntity(T entity) {
		this.em.merge(entity);
	}

	/**
	 * 
	 * Removes an entity.
	 * 
	 * @param entity Entity to be removed.
	 */
	public void deleteEntity(T entity) {
		this.em.remove(entity);
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
