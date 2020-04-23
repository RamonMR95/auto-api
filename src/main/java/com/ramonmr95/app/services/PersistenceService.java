package com.ramonmr95.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ramonmr95.app.interceptors.LoggingInterceptor;

@Stateless
@Interceptors(LoggingInterceptor.class)
public class PersistenceService<T, K> {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public TypedQuery<T> getEntitiesQuery(Class<T> c, Map<String, String> filterMap, String orderBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(c);
		Root<T> r = cq.from(c);
		cq.select(r);
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			Expression<String> key = cb.lower(r.get(entry.getKey()).as(String.class));
			String value = String.format("%%%s%%", entry.getValue().toLowerCase());
			predicates.add(cb.like(key, value));
		}
		Predicate predicate = cb.or(predicates.toArray(new Predicate[0]));
		cq.where(predicate);
		if (orderBy != null && !orderBy.isEmpty()) {
			if (orderBy.charAt(0) == '-') {
				cq.orderBy(cb.desc(r.get(orderBy.substring(1))));
			} else {
				cq.orderBy(cb.asc(r.get(orderBy)));
			}
		}
		TypedQuery<T> query = this.em.createQuery(cq);
		return query;
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
