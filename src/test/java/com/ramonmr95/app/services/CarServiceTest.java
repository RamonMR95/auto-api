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

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.CarNotFoundException;
import com.ramonmr95.app.utils.EntityValidationException;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

	@InjectMocks
	private CarService carService;

	@Mock
	private PersistenceService<Car, UUID> persistenceService;

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
	public void whenCreatingACarWithValidValues() throws EntityValidationException {
		doNothing().when(this.persistenceService).persistEntity(car);
		this.carService.createCar(car);
		Mockito.verify(persistenceService, Mockito.times(1)).persistEntity(car);
	}

	@Test
	public void whenGettingAllOfTheCars_ShouldReturnListOfCars() {
		when(this.persistenceService.getEntitiesWithNamedQuery("Car.findAll", Car.class))
				.thenReturn(new ArrayList<Car>());
		List<Car> expectedCars = new ArrayList<Car>();
		List<Car> cars = this.carService.getCars();
		assertEquals(expectedCars, cars);
	}

	@Test
	public void whenGettingACarWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			Car carTest = this.carService.getCar(carId);
			assertEquals(car, carTest);
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidID() {
		try {
			String brand = "Renault";
			car.setBrand(brand);
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			doNothing().when(this.persistenceService).mergeEntity(car);
			this.carService.updateCar(car);
			assertEquals(car.getBrand(), brand);
		} catch (CarNotFoundException | EntityValidationException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			doNothing().when(persistenceService).deleteEntity(car);
			this.carService.deleteCar(carId);
			Mockito.verify(this.persistenceService, Mockito.times(1)).deleteEntity(car);
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingACarWithInvalidValues_ShouldThrowEntityValidationException() {
		try {
			Car carErrors = new Car();
			carErrors.setId(carId);
			carErrors.setBrand("Renault");
			car.setCountry(null);
			carErrors.setCreated_at(new Timestamp(new Date().getTime()));
			carErrors.setRegistration(new Timestamp(new Date().getTime()));
			carErrors.setUpdated_at(new Timestamp(new Date().getTime()));
			this.carService.createCar(carErrors);
			fail("Should not get here");
		} catch (EntityValidationException e) {
			
		}
	}

	@Test
	public void whenGettingAnUnexistingCar_ShouldThrowCarNotFoundException() {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			this.carService.getCar(id);
			fail("Should not get here");
		} catch (CarNotFoundException e) {
		}
	}

	@Test
	public void whenUpdatingACarWithAnInvalidID_ShouldThrowCarNotFoundException() {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			when(this.carService.getCar(id)).thenReturn(car);
			doThrow(CarNotFoundException.class).when(this.carService).updateCar(car);
			car.setBrand("X");
			this.carService.updateCar(car);
			fail("Should not get here");
		} catch (EntityValidationException e) {
			fail("Should not get here");
		} catch (CarNotFoundException e) {
		}
	}

	@Test
	public void whenUpdatingACarWithAValidIDAndEntityValidationErrors_ShouldThrowEntityValidationException()
			throws CarNotFoundException {
		try {
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			car.setBrand(null);
			this.carService.updateCar(car);
			fail("Should not get here");
		} catch (EntityValidationException e) {
			
		} catch (CarNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAnInValidID_ShouldThrowCarNotFoundException() {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			this.carService.deleteCar(id);
			fail("Should not get here");
		} catch (CarNotFoundException e) {
		}
	}

}
