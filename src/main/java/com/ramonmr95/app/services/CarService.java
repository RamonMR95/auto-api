package com.ramonmr95.app.services;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class CarService {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;
	
}
