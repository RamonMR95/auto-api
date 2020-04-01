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
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;

/**
 * 
 * Service that using {@link PersistenceService} service generates a CRUD to be
 * used by {@link CarResourceImpl}.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class CarService {

	private final Logger log = LogManager.getLogger(CarService.class);

	@EJB
	private PersistenceService<Car, UUID> persistenceService;

	/**
	 * 
	 * Gets all of the {@link Car} entities by using a namedQuery.
	 * 
	 * @return cars List that contains all of the {@link Car} entities.
	 */
	public List<Car> getCars() {
		return this.persistenceService.getEntitiesWithNamedQuery("Car.findAll", Car.class);
	}

	/**
	 * 
	 * Gets a car given its id.
	 * 
	 * @param id Id of the car in {@link UUID} format
	 * @return car Returns the car if the given id matches any {@link Car} entity of
	 *         the database.
	 * @throws EntityNotFoundException If the given id does not match any
	 *                                 {@link Car} entity of the database.
	 */
	public Car getCar(UUID id) throws EntityNotFoundException {
		Car car = this.persistenceService.getEntityByID(Car.class, id);
		if (car != null) {
			return car;
		}
		log.error("Cannot find a car with id: " + id);
		throw new EntityNotFoundException("Cannot find a car with id: " + id);
	}

	/**
	 * 
	 * Creates a car given by the request body.
	 * 
	 * @param car {@link Car} to create.
	 * @throws EntityValidationException If the entity {@link Car} contains
	 *                                   validation errors.
	 */
	public void createCar(Car car) throws EntityValidationException {
		if (isCarValid(car)) {
			this.persistenceService.persistEntity(car);
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(getCarValidationErrorsString(car));
		}
	}

	/**
	 * 
	 * Updates a {@link Car} given from the request body.
	 * 
	 * @param car {@link Car} to update.
	 * @throws EntityNotFoundException   If the given id does not match any
	 *                                   {@link Car} entity of the database.
	 * @throws EntityValidationException If the entity {@link Car} contains
	 *                                   validation errors.
	 */
	public void updateCar(Car car) throws EntityNotFoundException, EntityValidationException {
		getCar(car.getId());
		if (isCarValid(car)) {
			this.persistenceService.mergeEntity(car);
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(getCarValidationErrorsString(car));
		}
	}

	/**
	 * 
	 * Deletes a {@link Car} from the database given its id.
	 * 
	 * @param id Id of the car in {@link UUID} format.
	 * @throws EntityNotFoundException If the given id does not match any
	 *                                 {@link Car} entity of the database.
	 */
	public void deleteCar(UUID id) throws EntityNotFoundException {
		Car car = this.getCar(id);
		this.persistenceService.deleteEntity(car);
	}

	private Map<String, Set<String>> getCarValidationErrors(Car car) {
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