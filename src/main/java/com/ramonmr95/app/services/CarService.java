package com.ramonmr95.app.services;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.entities.metamodels.Brand_;
import com.ramonmr95.app.entities.metamodels.Car_;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.parsers.JsonParser;
import com.ramonmr95.app.resources.CarResourceImpl;
import com.ramonmr95.app.validators.EntityValidator;

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

	@EJB
	private CountryService countryService;

	@EJB
	private BrandService brandService;

	private EntityValidator<Car> validator = new EntityValidator<>();

	private JsonParser jsonParser = new JsonParser();

	/**
	 * Gets all of the {@link Car} filtered and ordered.
	 * 
	 * @param filterBy {@link Car} Field to be filtered by.
	 * @param orderBy  {@link Car} Field to be ordered by.
	 * @return cars List that contains all of the {@link Car} entities.
	 */
	public TypedQuery<Car> getCars(String filterBy, String orderBy) {
		CriteriaBuilder cb = this.persistenceService.getEm().getCriteriaBuilder();
		CriteriaQuery<Car> cq = cb.createQuery(Car.class);
		Root<Car> r = cq.from(Car.class);
		Join<Car, Brand> joinBrand = r.join(Car_.BRAND);
		cq.select(r);

		if (filterBy != null && !filterBy.isEmpty()) {
			Predicate brandPredicate = cb.like(cb.lower(joinBrand.get(Brand_.NAME).as(String.class)),
					String.format("%%%s%%", filterBy.toLowerCase()));

			Predicate carPredicate = cb.like(cb.lower(r.get(Car_.REGISTRATION).as(String.class)),
					String.format("%%%s%%", filterBy.toLowerCase()));
			cq.where(cb.or(brandPredicate, carPredicate));
		}

		if (orderBy != null && !orderBy.isEmpty()) {
			if (orderBy.charAt(0) == '-') {
				if (orderBy.substring(1).equalsIgnoreCase("brand")) {
					cq.orderBy(cb.desc(joinBrand.get(Brand_.NAME)));
				} else if (orderBy.substring(1).equalsIgnoreCase("registration")) {
					cq.orderBy(cb.desc(r.get(orderBy.substring(1))));
				}
			} else {
				if (orderBy.equalsIgnoreCase("brand")) {
					cq.orderBy(cb.asc(joinBrand.get(Brand_.NAME)));
				} else if (orderBy.equalsIgnoreCase("registration")) {
					cq.orderBy(cb.asc(r.get(orderBy)));
				}
			}
		}
		return this.persistenceService.getEm().createQuery(cq);
	}

	/**
	 * Gets all of the {@link Car} filtered, ordered and paginated.
	 * 
	 * @param page     page number of the pagination. (DEF=1)
	 * @param size     number of cars
	 * @param filterBy {@link Car} Field to be filtered by.
	 * @param orderBy  {@link Car} Field to be ordered by.
	 * @return cars List that contains all of the {@link Car} entities.
	 */
	public List<Car> getCarsPaginated(int page, int size, String filterBy, String orderBy) {
		TypedQuery<Car> query = this.getCars(filterBy, orderBy);
		query.setFirstResult((size * page) - size);
		query.setMaxResults(size);
		return query.getResultList();
	}

	/**
	 * 
	 * Gets the number of cars filtered by name and registration.
	 * 
	 * @param filterBy {@link Car} Field to be filtered by.
	 * @return count Number of cars.
	 */
	public Long getFilteredCarsCount(String filterBy) {
		return (long) this.getCars(filterBy, "").getResultList().size();
	}

	/**
	 * 
	 * Gets a car given its id.
	 * 
	 * @param id Id of the car as string fullfilling the {@link UUID} format.
	 * @return car Returns the car if the given id matches any {@link Car} entity of
	 *         the database.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Car} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Car getCar(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID uuid = this.jsonParser.parseUUIDFromString(id);
		Car car = this.persistenceService.getEntityByID(Car.class, uuid);
		if (car != null) {
			return car;
		}
		log.error(String.format("Cannot find a car with id: %s", id));
		String errorMsg = this.jsonParser.getErrorsAsJSONFormatString("Cannot find a car with id: " + id);
		throw new EntityNotFoundException(errorMsg);
	}

	/**
	 * 
	 * Creates a car given by the request body.
	 * 
	 * @param car {@link Car} to create.
	 * @return car Created {@link Car}.
	 * @throws EntityValidationException  If the entity {@link Car} contains
	 *                                    validation errors.
	 * @throws EntityNotFoundException
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Car createCar(CarDto carDto)
			throws EntityValidationException, EntityNotFoundException, InvalidUUIDFormatException {
		Car car = carDto.convertToEntity();
		Country country = this.countryService.getCountryByName(carDto.getCountry().convertToEntity().getName());
		Brand brand = this.brandService.getBrandByName(carDto.getBrand().convertToEntity().getName());
		car.setCountry(country);
		car.setBrand(brand);

		if (validator.isEntityValid(car)) {
			this.persistenceService.persistEntity(car);
			return this.getCar(car.getId().toString());
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(car));
		}
	}

	/**
	 * 
	 * Updates a {@link Car} given from the request body.
	 * 
	 * @param car {@link Car} to update.
	 * @param id  Id of the car as string fullfilling the {@link UUID} format.
	 * @return car Updated {@link Car}.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Car} entity of the database.
	 * @throws EntityValidationException  If the entity {@link Car} contains
	 *                                    validation errors.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Car updateCar(CarDto carDto, String id)
			throws EntityNotFoundException, EntityValidationException, InvalidUUIDFormatException {
		UUID uuid = this.jsonParser.parseUUIDFromString(id);
		Country country = this.countryService.getCountryByName(carDto.getCountry().convertToEntity().getName());
		Brand brand = this.brandService.getBrandByName(carDto.getBrand().convertToEntity().getName());

		Car car = this.persistenceService.getEntityByID(Car.class, uuid);

		if (car == null) {
			log.error(String.format("Car not found with id: %s", id));
			throw new EntityNotFoundException(
					jsonParser.getErrorsAsJSONFormatString(String.format("Car not found with id: %s", id)));
		}

		if (validator.isEntityValid(carDto.convertToEntity())) {
			car.setBrand(brand);
			car.setCountry(country);
			car.setRegistration(car.getRegistration());
			car.setColor(car.getColor());
			car.setModel(car.getModel());
			car.setCarComponents(car.getCarComponents());
			this.persistenceService.mergeEntity(car);
			return car;
		} else {
			log.error("The car does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(carDto.convertToEntity()));
		}
	}

	/**
	 * 
	 * Deletes a {@link Car} from the database given its id.
	 * 
	 * @param id Id of the car as string fullfilling the {@link UUID} format.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Car} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public void deleteCar(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		this.jsonParser.parseUUIDFromString(id);
		Car car = this.getCar(id);
		this.persistenceService.deleteEntity(car);
	}

}