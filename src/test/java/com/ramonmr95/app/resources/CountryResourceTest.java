package com.ramonmr95.app.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

import com.ramonmr95.app.dtos.CountryDto;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.services.CountryService;

@RunWith(MockitoJUnitRunner.class)
public class CountryResourceTest {

	@InjectMocks
	private CountryResourceImpl countryResource;

	@Mock
	private CountryService countryService;

	private Country country;

	private UUID countryID = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	private CountryDto countryDto;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		this.country = new Country();
		this.country.setId(this.countryID);
		this.country.setName("Spain");
		this.country.setFlagUrl("");
		this.country.setIsoCode("ES");
		this.country.setCreated_at(new Timestamp(new Date().getTime()));
		this.country.setUpdated_at(new Timestamp(new Date().getTime()));
		this.countryDto = this.country.getDto();
	}

	@After
	public void tearDown() throws Exception {

	}

	// Valid values
	@Test
	public void whenCreatingACountryWithValidValues_ShouldReturnStatusCreated() {
		try {
			when(this.countryService.createCountry(Mockito.any(Country.class))).thenReturn(country);
			Response responseTest = this.countryResource.createCountry(country.getDto());
			assertEquals(countryDto.getId(), ((CountryDto) responseTest.getEntity()).getId());
			assertEquals(Status.CREATED.getStatusCode(), responseTest.getStatus());
		} catch (EntityValidationException e) {
			fail("Should not get here");
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAllOfTheCountries_ShouldReturnStatusOk() {
		when(this.countryService.getCountries()).thenReturn(new ArrayList<Country>());
		Response responseTest = this.countryResource.getAllCountries();
		assertEquals(Status.OK.getStatusCode(), responseTest.getStatus());
	}

	@Test
	public void whenGettingACountryWithAValidID_ShouldReturnStatusOk() {
		try {
			when(this.countryService.getCountry(any(String.class))).thenReturn(country);
			Response response = this.countryResource.getCountryById(country.getDto().getId().toString());
			assertEquals(countryDto.getId(), ((CountryDto) (response.getEntity())).getId());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACountryWithAValidID_ShouldReturnStatusNoContent() {
		try {
			String name = "France";
			country.setName(name);
			when(this.countryService.updateCountry(any(Country.class), any(String.class))).thenReturn(country);
			Response response = this.countryResource.updateCountry(countryID.toString(), country.getDto());
			assertEquals(country.getName(), ((CountryDto) response.getEntity()).getName());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACountryWithAValidID_ShouldReturnStatusNoContent() {
		try {
			doNothing().when(this.countryService).deleteCountry(countryID.toString());
			Response response = this.countryResource.deleteCountry(countryID.toString());
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingACountryWithInvalidValues_ShouldReturnStatusBadRequest() {
		try {
			Response response = null;
			Country invalidCountry = new Country();
			invalidCountry.setId(this.countryID);
			invalidCountry.setName(null);
			invalidCountry.setFlagUrl("");
			invalidCountry.setIsoCode("ES");
			invalidCountry.setCreated_at(new Timestamp(new Date().getTime()));
			invalidCountry.setUpdated_at(new Timestamp(new Date().getTime()));
			doThrow(EntityValidationException.class).when(this.countryService).createCountry(any(Country.class));
			response = this.countryResource.createCountry(invalidCountry.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAnUnexistingCountry_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			String id = "e72fd0a4-f7a5-42d4-908e-7bc1dc62f000";
			when(this.countryService.getCountry(id)).thenThrow(EntityNotFoundException.class);
			Response response = this.countryResource.getCountryById(id.toString());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenUpdatingACountryWithAnInvalidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			String id = "e72fd0a4-f7a5-42d4-908e-7bc1dc62f000";
			Response response = null;
			when(this.countryService.updateCountry(any(Country.class), any(String.class)))
					.thenThrow(EntityNotFoundException.class);
			response = this.countryResource.updateCountry(id, country.getDto());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACountryWithAValidIDAndEntityValidationErrors_ShouldReturnBadRequest() {
		try {
			Response response = null;
			country.setName(null);
			doThrow(EntityValidationException.class).when(this.countryService).updateCountry(any(Country.class),
					any(String.class));
			response = this.countryResource.updateCountry(this.countryID.toString(), country.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACountryWithAnInValidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			String id = "e72fd0a4-f7a5-42d4-908e-7bc1dc62f000";
			doThrow(EntityNotFoundException.class).when(this.countryService).deleteCountry(id);
			Response response = this.countryResource.deleteCountry(id.toString());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

}
