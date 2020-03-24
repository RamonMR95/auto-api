package com.ramonmr95.app.services;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ramonmr95.app.entities.Car;


@Stateless
public class CarService {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;
	
	public void createCar(Car car) {
		this.em.persist(car);
	}
}
