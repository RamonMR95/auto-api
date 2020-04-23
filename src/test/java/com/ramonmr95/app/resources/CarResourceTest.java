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

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.services.CarService;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceTest {

	@InjectMocks
	private CarResourceImpl carResource;

	@Mock
	private CarService carService;

	private Car car;

	private UUID carId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	private CarDto carDto;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		this.car = new Car();
		this.car.setId(carId);
		this.car.setBrand("Renault");
		this.car.setCountry("France");
		this.car.setCreated_at(new Timestamp(new Date().getTime()));
		this.car.setRegistration(new Timestamp(new Date().getTime()));
		this.car.setUpdated_at(new Timestamp(new Date().getTime()));
		this.carDto = this.car.getDto();
	}

	@After
	public void tearDown() throws Exception {

	}

	// Valid values
	@Test
	public void whenCreatingACarWithValidValues_ShouldReturnStatusCreated() {
		try {
			when(this.carService.createCar(Mockito.any(Car.class))).thenReturn(car);
			Response responseTest = this.carResource.createCar(car.getDto());
			assertEquals(carDto.getId(), ((CarDto) responseTest.getEntity()).getId());
			assertEquals(Status.CREATED.getStatusCode(), responseTest.getStatus());
		} catch (EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAllOfTheCars_ShouldReturnStatusOk() {
		when(this.carService.getCars(any(Integer.class), any(Integer.class), any(String.class), any(String.class)))
				.thenReturn(new ArrayList<Car>());
		Response responseTest = this.carResource.getAllCars(1, 1, "brand", "");
		assertEquals(Status.OK.getStatusCode(), responseTest.getStatus());
	}

	@Test
	public void whenGettingACarWithAValidID_ShouldReturnStatusOk() {
		try {
			when(this.carService.getCar(any(UUID.class))).thenReturn(car);
			Response response = this.carResource.getCarById(car.getDto().getId());
			assertEquals(carDto.getId(), ((CarDto) (response.getEntity())).getId());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			String brand = "Renault";
			car.setBrand(brand);
			when(this.carService.updateCar(any(Car.class), any(UUID.class))).thenReturn(car);
			Response response = this.carResource.updateCar(carId, car.getDto());
			assertEquals(car.getBrand(), ((CarDto) response.getEntity()).getBrand());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			doNothing().when(this.carService).deleteCar(carId);
			Response response = this.carResource.deleteCar(carId);
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingACarWithInvalidValues_ShouldReturnStatusBadRequest() {
		try {
			Response response = null;
			Car carErrors = new Car();
			carErrors.setId(carId);
			carErrors.setBrand("Renault");
			carErrors.setCountry(null);
			carErrors.setCreated_at(new Timestamp(new Date().getTime()));
			carErrors.setRegistration(new Timestamp(new Date().getTime()));
			carErrors.setUpdated_at(new Timestamp(new Date().getTime()));
			doThrow(EntityValidationException.class).when(this.carService).createCar(any(Car.class));
			response = this.carResource.createCar(carErrors.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The country is required\"]}", e.getMessage());
		}
	}

	@Test
	public void whenGettingAnUnexistingCar_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		when(this.carService.getCar(id)).thenThrow(EntityNotFoundException.class);
		Response response = this.carResource.getCarById(id);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void whenUpdatingACarWithAnInvalidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			Response response = null;
			when(this.carService.updateCar(any(Car.class), any(UUID.class))).thenThrow(EntityNotFoundException.class);
			response = this.carResource.updateCar(id, car.getDto());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidIDAndEntityValidationErrors_ShouldReturnBadRequest() {
		try {
			Response response = null;
			car.setBrand(null);
			doThrow(EntityValidationException.class).when(this.carService).updateCar(any(Car.class), any(UUID.class));
			response = this.carResource.updateCar(carId, car.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The brand is required\"]}", e.getMessage());
		} catch (EntityNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAnInValidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		doThrow(EntityNotFoundException.class).when(this.carService).deleteCar(id);
		Response response = this.carResource.deleteCar(id);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

}
