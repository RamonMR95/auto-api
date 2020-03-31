package com.ramonmr95.app.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.EntityNotFoundException;
import com.ramonmr95.app.utils.EntityValidationException;
import com.ramonmr95.app.utils.LoggingInterceptor;

@Stateless
@Interceptors(LoggingInterceptor.class)
public class CarService {

	private final Logger log = LogManager.getLogger(CarService.class);

	@EJB
	private PersistenceService<Car, UUID> persistenceService;

	public List<Car> getCars() {
		return this.persistenceService.getEntitiesWithNamedQuery("Car.findAll", Car.class);
	}

	public Car getCar(UUID id) throws EntityNotFoundException {
		Car car = this.persistenceService.getEntityByID(Car.class, id);
		if (car != null) {
			return car;
		}
		log.error("Cannot find a car with id: " + id);
		throw new EntityNotFoundException("Cannot find a car with id: " + id);
	}

	public void createCar(Car car) throws EntityValidationException {
		if (isCarValid(car)) {
			this.persistenceService.persistEntity(car);
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(getCarValidationErrorsString(car));
		}
	}

	public void updateCar(Car car) throws EntityNotFoundException, EntityValidationException {
		getCar(car.getId());
		if (isCarValid(car)) {
			this.persistenceService.mergeEntity(car);
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(getCarValidationErrorsString(car));
		}
	}

	public void deleteCar(UUID id) throws EntityNotFoundException {
		Car car = this.getCar(id);
		this.persistenceService.deleteEntity(car);
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

	private String getCarValidationErrorsString(Car car) {
		ObjectMapper mapper = new ObjectMapper();
		String errs = null;
		try {
			errs = mapper.writeValueAsString(getCarValidationErrors(car));
		} catch (JsonProcessingException e) {
			errs = "Validation errors";
		}
		return errs;
	}

	private boolean isCarValid(Car car) {
		return this.getCarValidationErrors(car).get("errors").isEmpty();
	}

}