package com.ramonmr95.app.resources;

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
import org.mockito.junit.MockitoJUnitRunner;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.services.CarService;
import com.ramonmr95.app.utils.CarNotFoundException;
import com.ramonmr95.app.utils.EntityValidationException;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceTest {

	@InjectMocks
	private CarResource carResource;

	@Mock
	private CarService carService;

	private Car car;

	private UUID carId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		car = new Car();
		car.setId(carId);
		car.setBrand("Renault");
		car.setCountry("France");
		car.setCreated_at(new Timestamp(new Date().getTime()));
		car.setRegistration(new Timestamp(new Date().getTime()));
		car.setUpdated_at(new Timestamp(new Date().getTime()));
	}

	@After
	public void tearDown() throws Exception {

	}

	// Valid values
	@Test
	public void whenCreatingACarWithValidValues_ShouldReturnStatusCreated() {
		try {
			doNothing().when(this.carService).createCar(car);
			Response responseTest = this.carResource.createCar(car);
			assertEquals(car, responseTest.getEntity());
			assertEquals(Status.CREATED.getStatusCode(), responseTest.getStatus());
		} catch (EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAllOfTheCars_ShouldReturnStatusOk() {
		when(this.carService.getCars()).thenReturn(new ArrayList<Car>());
		List<Car> expectedCars = new ArrayList<Car>();
		Response responseTest = this.carResource.getAllCars();
		assertEquals(expectedCars, responseTest.getEntity());
		assertEquals(Status.OK.getStatusCode(), responseTest.getStatus());
	}

	@Test
	public void whenGettingACarWithAValidID_ShouldReturnStatusOk() {
		try {
			when(this.carService.getCar(car.getId())).thenReturn(car);
			Response response = this.carResource.getCarById(car.getId());
			assertEquals(car, response.getEntity());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			String brand = "Renault";
			when(this.carService.getCar(car.getId())).thenReturn(car);
			car.setBrand(brand);
			doNothing().when(this.carService).updateCar(car);
			Response response = this.carResource.updateCar(carId, car);
			assertEquals(car.getBrand(), ((Car) response.getEntity()).getBrand());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (CarNotFoundException | EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			doNothing().when(this.carService).deleteCar(carId);
			Response response = this.carResource.deleteCar(carId);
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingACarWithInvalidValues_ShouldReturnStatusBadRequest() throws EntityValidationException {
		Response response = null;
		Car carErrors = new Car();
		carErrors.setId(carId);
		carErrors.setBrand("Renault");
		carErrors.setCountry(null);
		carErrors.setCreated_at(new Timestamp(new Date().getTime()));
		carErrors.setRegistration(new Timestamp(new Date().getTime()));
		carErrors.setUpdated_at(new Timestamp(new Date().getTime()));
		doThrow(EntityValidationException.class).when(this.carService).createCar(carErrors);
		response = this.carResource.createCar(carErrors);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	public void whenGettingAnUnexistingCar_ShouldReturnStatusNotFound() throws CarNotFoundException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		doThrow(CarNotFoundException.class).when(this.carService).getCar(id);
		Response response = this.carResource.getCarById(id);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void whenUpdatingACarWithAnInvalidID_ShouldReturnStatusNotFound() throws CarNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			Response response = null;
			when(this.carService.getCar(id)).thenReturn(car);
			doThrow(CarNotFoundException.class).when(this.carService).updateCar(car);
			response = this.carResource.updateCar(id, car);
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidIDAndEntityValidationErrors_ShouldReturnBadRequest()
			throws EntityValidationException {
		try {
			Response response = null;
			when(this.carService.getCar(carId)).thenReturn(car);
			car.setBrand(null);
			doThrow(EntityValidationException.class).when(this.carService).updateCar(car);
			response = this.carResource.updateCar(carId, car);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAnInValidID_ShouldReturnStatusNotFound() throws CarNotFoundException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		doThrow(CarNotFoundException.class).when(this.carService).deleteCar(id);
		Response response = this.carResource.deleteCar(id);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

}
