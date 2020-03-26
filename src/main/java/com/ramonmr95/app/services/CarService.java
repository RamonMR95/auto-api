package com.ramonmr95.app.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.CarNotFoundException;
import com.ramonmr95.app.utils.EntityValidationException;

@Stateless
public class CarService {
	
	private static final Logger log = LogManager.getLogger(CarService.class);

	@PersistenceContext(unitName = "carPU")
	private EntityManager em;

	public List<Car> getCars() {
		log.info("Entering getCars");
		TypedQuery<Car> query = em.createNamedQuery("Car.findAll", Car.class);
		log.info("Exiting getCars");
		return query.getResultList();
	}

	public Car getCar(UUID id) throws CarNotFoundException {
		log.info("Entering getCar");
		Car car = this.em.find(Car.class, id);
		log.info("Exiting getCar");
		if (car != null) {
			return car;
		}
		throw new CarNotFoundException();
	}

	public void createCar(Car car) throws EntityValidationException {
		if (isCarValid(car)) {
			this.em.persist(car);
		}
		else {
			throw new EntityValidationException();
		}
	}

	public void updateCar(Car car) throws CarNotFoundException, EntityValidationException {
		getCar(car.getId());
		if (isCarValid(car)) {
			this.em.merge(car);
		}
		else {
			throw new EntityValidationException();
		}
	}

	public void deleteCar(UUID id) throws CarNotFoundException {
		log.info("Entering deleteCar");
		Car car = this.getCar(id);
		this.em.remove(car);
		log.info("Exiting deleteCar");
	}
	
	public Map<String, Set<String>> getCarValidationErrors(Car car) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Car>> validationErrors = validator.validate(car);
		Set<String> errorsSet = new HashSet<String>();
		
		for (ConstraintViolation<Car> constraintViolation : validationErrors) {
			errorsSet.add(constraintViolation.getMessage());
		}
		
		Map<String, Set<String>> errors = new HashMap<String, Set<String>>();
		errors.put("errors", errorsSet);
		return errors;
	}
	
	private boolean isCarValid(Car car) {
		return this.getCarValidationErrors(car).get("errors").isEmpty();
	}
	
}