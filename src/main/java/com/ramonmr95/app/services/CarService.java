package com.ramonmr95.app.services;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.CarNotFoundException;

@Stateless
public class CarService {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public List<Car> getCars() {
		TypedQuery<Car> query = em.createNamedQuery("Car.findAll", Car.class);
		return query.getResultList();
	}

	public Car getCar(UUID id) throws CarNotFoundException {
		Car car = this.em.find(Car.class, id);
		if (car != null) {
			return car;
		}
		throw new CarNotFoundException();
	}

	public void createCar(Car car) {
		this.em.persist(car);
	}

	public void updateCar(Car car) throws CarNotFoundException {
		getCar(car.getId());
		this.em.merge(car);
	}

	public void deleteCar(UUID id) throws CarNotFoundException {
		Car car = this.getCar(id);
		this.em.remove(car);
	}
}