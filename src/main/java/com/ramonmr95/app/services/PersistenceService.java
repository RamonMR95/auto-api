package com.ramonmr95.app.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ramonmr95.app.utils.LoggingInterceptor;

@Stateless
@Interceptors(LoggingInterceptor.class)
public class PersistenceService<T, K> {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public List<T> getEntitiesWithNamedQuery(String namedQuery, Class<T> c) {
		TypedQuery<T> query = em.createNamedQuery(namedQuery, c);
		return query.getResultList();
	}

	public T getEntityByID(Class<T> c, K id) {
		return this.em.find(c, id);
	}

	public void persistEntity(T entity) {
		this.em.persist(entity);
	}

	public void mergeEntity(T entity) {
		this.em.merge(entity);
	}

	public void deleteEntity(T entity) {
		this.em.remove(entity);
	}
}
