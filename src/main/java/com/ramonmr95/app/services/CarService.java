package com.ramonmr95.app.services;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.CarNotFoundException;

@Stateless
public class CarService {

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public List<Car> getCars() {
		return this.em.createQuery("FROM Car", Car.class).getResultList();
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
		if (getCar(car.getId()) != null) {
			this.em.merge(car);
		}
	}

	public void deleteCar(UUID id) throws CarNotFoundException {
		Car car = this.getCar(id);
		if (car != null) {
			this.em.remove(car);
		}
	}
}