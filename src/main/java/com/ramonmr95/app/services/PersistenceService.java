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

@Stateless
@Interceptors(LoggingInterceptor.class)
public class PersistenceService<T, K> {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public List<T> getEntities(Class<T> c) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(c);
		Root<T> r = cq.from(c);
		cq.select(r);
		TypedQuery<T> query = this.em.createQuery(cq);
		return query.getResultList();
	}

	public T getEntityByID(Class<T> c, K id) {
		return this.em.find(c, id);
	}

	public List<T> getEntityByField(Class<T> c, String field, String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(c);
		Root<T> r = cq.from(c);
		cq.where(cb.like(r.get(field), name));
		TypedQuery<T> query = this.em.createQuery(cq);
		return query.getResultList();
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

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
