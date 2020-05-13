package com.ramonmr95.app.resources;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

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

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.listeners.CarResourceAsyncImpl;
import com.ramonmr95.app.services.CarService;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceAsyncTest {

	@InjectMocks
	private CarResourceAsyncImpl carResourceAsyncImpl;

	@Mock
	private CarService carService;

	@Mock
	private Message message;

	private Car car;

	private UUID carId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	private Brand brand;

	private UUID brandId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f852");

	private Country country;

	private UUID countryId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f851");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.brand = new Brand();
		this.brand.setId(brandId);
		this.brand.setName("BMW");
		this.brand.setCreated_at(new Timestamp(new Date().getTime()));
		this.brand.setUpdated_at(new Timestamp(new Date().getTime()));

		this.country = new Country();
		this.country.setId(this.countryId);
		this.country.setName("Spain");
		this.country.setFlagUrl("");
		this.country.setIsoCode("ES");
		this.country.setCreated_at(new Timestamp(new Date().getTime()));
		this.country.setUpdated_at(new Timestamp(new Date().getTime()));

		this.car = new Car();
		this.car.setId(carId);
		this.car.setBrand(brand);
		this.car.setCountry(country);
		this.car.setCreated_at(new Timestamp(new Date().getTime()));
		this.car.setRegistration(new Timestamp(new Date().getTime()));
		this.car.setUpdated_at(new Timestamp(new Date().getTime()));

	}

	@After
	public void tearDown() throws Exception {
	}

	// Valid values
	@Test
	public void whenPostMethod_ShouldCreateACar() {
		try {
			String json = "{\"brand\":{\"name\":\"BMW22222\"},\"model\":\"Serie 3\",\"color\":\"black\",\"registration\":\"2020-03-24T09:13:26.441+01:00\",\"country\":{\"name\":\"Germany\",\"isoCode\":\"DEU\",\"flagUrl\":\"\"},\"car_components\":[\"Steering wheel\"]}";
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("POST");
			Mockito.when(this.message.getBody(String.class)).thenReturn(json);
			Mockito.when(this.carService.createCar(Mockito.any(CarDto.class))).thenReturn(this.car);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService).createCar(Mockito.any(CarDto.class));
		} catch (JMSException | EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenUpdateMethod_ShouldUpdateACar() {
		try {
			String json = "{\"brand\":{\"name\":\"BMW22222\"},\"model\":\"Serie 3\",\"color\":\"black\",\"registration\":\"2020-03-24T09:13:26.441+01:00\",\"country\":{\"name\":\"Germany\",\"isoCode\":\"DEU\",\"flagUrl\":\"\"},\"car_components\":[\"Steering wheel\"]}";
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("PUT");
			Mockito.when(this.message.getStringProperty("id")).thenReturn(this.carId.toString());
			Mockito.when(this.message.getBody(String.class)).thenReturn(json);
			Mockito.when(this.carService.updateCar(Mockito.any(CarDto.class), Mockito.any(String.class)))
					.thenReturn(this.car);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService).updateCar(Mockito.any(CarDto.class), Mockito.any(String.class));
		} catch (JMSException | EntityValidationException | EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenDeleteMethod_ShouldMarkToDeleteACar() {
		try {
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("DELETE");
			Mockito.when(this.message.getStringProperty("id")).thenReturn(this.carId.toString());
			Mockito.doNothing().when(this.carService).markCarToDelete(this.carId.toString());
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService).markCarToDelete(this.carId.toString());
		} catch (JMSException | EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	// Invalid values
	@Test
	public void whenPostMethodWithANullCar_ShouldNotExecuteCarServiceCreateCarMethod() {
		try {
			String json = null;
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("POST");
			Mockito.when(this.message.getBody(String.class)).thenReturn(json);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService, Mockito.never()).createCar(Mockito.any(CarDto.class));
		} catch (JMSException | EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdateMethodWithAnInvalidId_ShouldNeverExecuteCarServiceUpdateCarMethod() {
		try {
			String json = "{\"brand\":{\"name\":\"BMW22222\"},\"model\":\"Serie 3\",\"color\":\"black\",\"registration\":\"2020-03-24T09:13:26.441+01:00\",\"country\":{\"name\":\"Germany\",\"isoCode\":\"DEU\",\"flagUrl\":\"\"},\"car_components\":[\"Steering wheel\"]}";
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("PUT");
			Mockito.when(this.message.getStringProperty("id")).thenReturn(null);
			Mockito.when(this.message.getBody(String.class)).thenReturn(json);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService, Mockito.never()).updateCar(Mockito.any(CarDto.class),
					Mockito.any(String.class));
		} catch (JMSException | EntityValidationException | EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenUpdateMethodWithANullCar_ShouldNeverExecuteCarServiceUpdateCarMethod() {
		try {
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("PUT");
			Mockito.when(this.message.getStringProperty("id")).thenReturn(this.carId.toString());
			Mockito.when(this.message.getBody(String.class)).thenReturn(null);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService, Mockito.never()).updateCar(Mockito.any(CarDto.class),
					Mockito.any(String.class));
		} catch (JMSException | EntityValidationException | EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenDeleteMethodWithAnInvalidId_ShouldNeverExecuteCarServiceMarkCarToDeleteMethod() {
		try {
			Mockito.when(this.message.getStringProperty("METHOD")).thenReturn("DELETE");
			Mockito.when(this.message.getStringProperty("id")).thenReturn(null);
			this.carResourceAsyncImpl.onMessage(message);
			Mockito.verify(this.carService, Mockito.never()).markCarToDelete(this.carId.toString());
		} catch (JMSException | EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

}
