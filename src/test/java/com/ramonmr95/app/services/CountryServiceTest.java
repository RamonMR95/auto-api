package com.ramonmr95.app.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;

@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {

	@InjectMocks
	private CountryService countryService;

	@Mock
	private PersistenceService<Country, UUID> persistenceService;

	@Mock
	private TypedQuery<Country> query;

	private Country country;

	private UUID countryId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.country = new Country();
		this.country.setId(this.countryId);
		this.country.setName("Spain");
		this.country.setFlagUrl("");
		this.country.setIsoCode("ES");
		this.country.setCreated_at(new Timestamp(new Date().getTime()));
		this.country.setUpdated_at(new Timestamp(new Date().getTime()));
	}

	@After
	public void tearDown() throws Exception {
	}

	// Valid values
	@Test
	public void whenCreatingACountryWithValidValues() throws EntityValidationException {
		try {
			doNothing().when(this.persistenceService).persistEntity(country);
			this.countryService.createCountry(country);
			Mockito.verify(persistenceService, Mockito.times(1)).persistEntity(country);
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenGettingAllOfTheCountries_ShouldReturnListOfCountries() {
		when(this.persistenceService.getEntities(Mockito.eq(Country.class))).thenReturn(new ArrayList<Country>());
		List<Country> expectedCountries = new ArrayList<Country>();
		List<Country> countries = this.countryService.getCountries();
		assertEquals(expectedCountries, countries);
	}

	@Test
	public void whenGettingACountryWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Country.class, countryId)).thenReturn(country);
			Country countryTest = this.countryService.getCountry(countryId.toString());
			assertEquals(country, countryTest);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACountryWithAValidID() {
		try {
			String name = "Brazil";
			country.setName(name);
			when(this.persistenceService.getEntityByID(Country.class, countryId)).thenReturn(country);
			doNothing().when(this.persistenceService).mergeEntity(country);
			this.countryService.updateCountry(country, countryId.toString());
			assertEquals(country.getName(), name);
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACountryWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Country.class, countryId)).thenReturn(country);
			doNothing().when(persistenceService).deleteEntity(country);
			this.countryService.deleteCountry(countryId.toString());
			Mockito.verify(this.persistenceService, Mockito.times(1)).deleteEntity(country);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingACountryWithInvalidValues_ShouldThrowEntityValidationException() {
		try {
			Country countryErrors = new Country();
			countryErrors.setId(this.countryId);
			countryErrors.setName(null);
			countryErrors.setFlagUrl("");
			countryErrors.setIsoCode("ES");
			countryErrors.setCreated_at(new Timestamp(new Date().getTime()));
			countryErrors.setUpdated_at(new Timestamp(new Date().getTime()));
			this.countryService.createCountry(countryErrors);
			fail("Should not get here");
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenGettingAnUnexistingCountry_ShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		when(this.persistenceService.getEntityByID(Country.class, id)).thenReturn(null);
		this.countryService.getCountry(id.toString());
		fail("Should not get here");
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUpdatingACountryWithAnInvalidID_ShouldThrowEntityNotFoundException()
			throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Country.class, id)).thenReturn(null);
			when(this.countryService.getCountry(id.toString())).thenReturn(country);
			doThrow(EntityNotFoundException.class).when(this.countryService).updateCountry(country, id.toString());
			country.setName("X");
			this.countryService.updateCountry(country, id.toString());
			fail("Should not get here");
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACountryWithAValidIDAndEntityValidationErrors_ShouldThrowEntityValidationException() {
		try {
			when(this.persistenceService.getEntityByID(Country.class, countryId)).thenReturn(country);
			country.setName(null);
			this.countryService.updateCountry(country, countryId.toString());
			fail("Should not get here");
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenDeletingACountryWithAnInValidID_ShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		when(this.persistenceService.getEntityByID(Country.class, id)).thenReturn(null);
		this.countryService.deleteCountry(id.toString());
		fail("Should not get here");
	}
}
