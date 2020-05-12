package com.ramonmr95.app.services;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.parsers.JsonParser;
import com.ramonmr95.app.resources.BrandResourceImpl;
import com.ramonmr95.app.validators.EntityValidator;

/**
 * 
 * Service that using {@link PersistenceService} service generates a CRUD to be
 * used by {@link BrandResourceImpl}.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class BrandService {

	private final Logger log = LogManager.getLogger(BrandService.class);

	@EJB
	private PersistenceService<Brand, UUID> persistenceService;

	private EntityValidator<Brand> validator = new EntityValidator<>();

	private JsonParser jsonParser = new JsonParser();

	/**
	 * Gets all of the {@link Brand} entities.
	 * 
	 * @return brands List that contains all of the {@link Brand} entities.
	 */
	public List<Brand> getBrands() {
		return this.persistenceService.getEntities(Brand.class);
	}

	/**
	 * 
	 * Gets a brand given its id.
	 * 
	 * @param id Id of the brand as string fullfilling the {@link UUID} format.
	 * @return brand Returns the brand if the given id matches any {@link Brand}
	 *         entity of the database.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Brand} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Brand getBrand(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID uuid = this.jsonParser.parseUUIDFromString(id);
		Brand brand = this.persistenceService.getEntityByID(Brand.class, uuid);
		if (brand != null) {
			return brand;
		}
		log.error(String.format("Cannot find a brand with id: %s", id));
		String errorMsg = this.jsonParser.getErrorsAsJSONFormatString("Cannot find a brand with id: " + id);
		throw new EntityNotFoundException(errorMsg);
	}

	/**
	 * 
	 * Gets a brand given its name.
	 * 
	 * @param name name of the brand.
	 * @return brand Returns the brand if the given name matches any {@link Brand}
	 *         entity of the database.
	 * @throws EntityNotFoundException If the given name does not match any
	 *                                 {@link Brand} entity of the database.
	 */
	public Brand getBrandByName(String name) throws EntityNotFoundException {
		List<Brand> brand = this.persistenceService.getEntityByField(Brand.class, "name", name);
		if (brand != null && !brand.isEmpty()) {
			return brand.get(0);
		}
		log.error("Cannot find a brand with name: " + name);
		String errorMsg = this.jsonParser.getErrorsAsJSONFormatString("Cannot find a brand with name: " + name);
		throw new EntityNotFoundException(errorMsg);
	}

	/**
	 * 
	 * Creates a brand given by the request body.
	 * 
	 * @param brand {@link Country} to create.
	 * @return brand Created {@link Country}.
	 * @throws EntityValidationException If the entity {@link Brand} contains
	 *                                   validation errors.
	 * @throws NotUniqueKeyException     If there is already a brand named like the
	 *                                   given one.
	 */
	public Brand createBrand(Brand brand) throws EntityValidationException, NotUniqueKeyException {
		if (validator.isEntityValid(brand)) {
			if (this.persistenceService.getEntityByField(Brand.class, "name", brand.getName()).isEmpty()) {
				this.persistenceService.persistEntity(brand);
				return brand;
			}
			log.error(String.format("Not unique name: %s%n", brand.getName()));
			String errorMsg = this.jsonParser
					.getErrorsAsJSONFormatString(String.format("Not unique name: %s%n", brand.getName()));
			throw new NotUniqueKeyException(errorMsg);
		} else {
			log.error("The brand does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(brand));
		}
	}

	/**
	 * 
	 * @param brand {@link Brand} to update.
	 * @param id    Id of the brand as string fullfilling the {@link UUID} format.
	 * @return brand Updated {@link Brand}
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Brand} entity of the database.
	 * @throws EntityValidationException  If the entity {@link Brand} contains
	 *                                    validation errors.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Brand updateBrand(Brand brand, String id)
			throws EntityNotFoundException, EntityValidationException, InvalidUUIDFormatException {
		this.jsonParser.parseUUIDFromString(id);
		Brand oldBrand = getBrand(id);

		if (oldBrand == null) {
			log.error(String.format("Brand not found with id: %s", id));
			throw new EntityNotFoundException(
					jsonParser.getErrorsAsJSONFormatString(String.format("Brand not found with id: %s", id)));
		}

		if (validator.isEntityValid(brand)) {
			oldBrand.setName(brand.getName());
			this.persistenceService.mergeEntity(oldBrand);
			return oldBrand;
		} else {
			log.error("The brand does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(brand));
		}
	}

	/**
	 * 
	 * Deletes a {@link Brand} from the database given its id.
	 * 
	 * @param id Id of the brand as string fullfilling the {@link UUID} format.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Brand} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public void deleteBrand(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		this.jsonParser.parseUUIDFromString(id);
		Brand brand = this.getBrand(id);
		this.persistenceService.deleteEntity(brand);
	}

}
