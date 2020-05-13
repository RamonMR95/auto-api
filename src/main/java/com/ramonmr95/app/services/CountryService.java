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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.parsers.JsonParser;
import com.ramonmr95.app.resources.CountryResourceImpl;
import com.ramonmr95.app.validators.EntityValidator;

/**
 * 
 * Service that using {@link PersistenceService} service generates a CRUD to be
 * used by {@link CountryResourceImpl}.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class CountryService {

	private final Logger log = LogManager.getLogger(CountryService.class);

	@EJB
	private PersistenceService<Country, UUID> persistenceService;

	private EntityValidator<Country> validator = new EntityValidator<>();

	private JsonParser jsonParser = new JsonParser();

	/**
	 * Gets all of the {@link Country} entities.
	 * 
	 * @return countries List that contains all of the {@link Country} entities.
	 */
	public List<Country> getCountries() {
		return this.persistenceService.getEntities(Country.class);
	}

	/**
	 * 
	 * Gets a country given its id.
	 * 
	 * @param id Id of the country as string fullfilling the {@link UUID} format.
	 * @return country Returns the country if the given id matches any
	 *         {@link Country} entity of the database.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Country} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Country getCountry(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID uuid = this.jsonParser.parseUUIDFromString(id);
		Country country = this.persistenceService.getEntityByID(Country.class, uuid);
		if (country != null) {
			return country;
		}
		log.error(String.format("Cannot find a country with id: %s", id));
		String errorMsg = this.jsonParser.getErrorsAsJSONFormatString("Cannot find a country with id: " + id);
		throw new EntityNotFoundException(errorMsg);
	}

	/**
	 * 
	 * Gets a country given its name.
	 * 
	 * @param name name of the country.
	 * @return country Returns the country if the given name matches any
	 *         {@link Country} entity of the database.
	 * @throws EntityNotFoundException If the given name does not match any
	 *                                 {@link Country} entity of the database.
	 */
	public Country getCountryByName(String name) throws EntityNotFoundException {
		List<Country> country = this.persistenceService.getEntityByField(Country.class, "name", name);
		if (country != null && !country.isEmpty()) {
			return country.get(0);
		}

		log.error(String.format("Cannot find a country with name: %s", name));
		String errorMsg = this.jsonParser.getErrorsAsJSONFormatString("Cannot find a country with name: " + name);
		throw new EntityNotFoundException(errorMsg);
	}

	/**
	 * 
	 * Creates a country given by the request body.
	 * 
	 * @param country {@link Country} to create.
	 * @return country Created {@link Country}.
	 * @throws EntityValidationException If the entity {@link Country} contains
	 *                                   validation errors.
	 * @throws NotUniqueKeyException     If there is already a country either the
	 *                                   country name or isoCode
	 */
	public Country createCountry(Country country) throws EntityValidationException, NotUniqueKeyException {
		if (validator.isEntityValid(country)) {
			Set<String> errorsSet = new HashSet<>();
			Map<String, Set<String>> errorsMap = new HashMap<>();

			if (!this.persistenceService.getEntityByField(Country.class, "name", country.getName()).isEmpty()) {
				errorsSet.add(String.format("Not unique name: %s %n", country.getName()));
			}

			if (!this.persistenceService.getEntityByField(Country.class, "isoCode", country.getIsoCode()).isEmpty()) {
				errorsSet.add(String.format("Not unique iso_code: %s n", country.getIsoCode()));
			}

			if (errorsSet.isEmpty()) {
				this.persistenceService.persistEntity(country);
				return country;
			}
			errorsMap.put("errors", errorsSet);
			String errorsStr = this.jsonParser.getMapAsJsonFormat(errorsMap);
			log.error(errorsStr);
			throw new NotUniqueKeyException(errorsStr);

		} else {
			log.error("The country does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(country));
		}
	}

	/**
	 * 
	 * @param country {@link Country} to update.
	 * @param id      Id of the country as string fullfilling the {@link UUID}
	 *                format.
	 * @return country Updated {@link Country}
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Country} entity of the database.
	 * @throws EntityValidationException  If the entity {@link Country} contains
	 *                                    validation errors.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public Country updateCountry(Country country, String id)
			throws EntityNotFoundException, EntityValidationException, InvalidUUIDFormatException {
		this.jsonParser.parseUUIDFromString(id);
		Country oldCountry = getCountry(id);

		if (oldCountry == null) {
			log.error(String.format("Country not found with id: %s", id));
			throw new EntityNotFoundException(
					jsonParser.getErrorsAsJSONFormatString(String.format("Country not found with id: %s", id)));
		}

		if (validator.isEntityValid(country)) {
			oldCountry.setName(country.getName());
			oldCountry.setIsoCode(country.getIsoCode());
			oldCountry.setFlagUrl(country.getFlagUrl());
			this.persistenceService.mergeEntity(oldCountry);
			return oldCountry;
		} else {
			log.error("The country does not fulfill all of the validations");
			throw new EntityValidationException(validator.getEntityValidationErrorsString(country));
		}
	}

	/**
	 * 
	 * Deletes a {@link Country} from the database given its id.
	 * 
	 * @param id id Id of the country as string fullfilling the {@link UUID} format.
	 * @throws EntityNotFoundException    If the given id does not match any
	 *                                    {@link Country} entity of the database.
	 * @throws InvalidUUIDFormatException If the given id cannot be parsed to UUID
	 *                                    format.
	 */
	public void deleteCountry(String id) throws EntityNotFoundException, InvalidUUIDFormatException {
		this.jsonParser.parseUUIDFromString(id);
		Country country = this.getCountry(id);
		this.persistenceService.deleteEntity(country);
	}
}
