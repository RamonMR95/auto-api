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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.services.CarService;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceRestTest {

	@InjectMocks
	private CarResourceRestImpl carResource;

	@Mock
	private CarService carService;

	private Car car;

	private UUID carId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	private Brand brand;

	private UUID brandId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f852");

	private Country country;

	private UUID countryId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f851");

	private CarDto carDto;

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
		this.carDto = this.car.getDto();
	}

	@After
	public void tearDown() throws Exception {

	}

	// Valid values
	@Test
	public void whenCreatingACarWithValidValues_ShouldReturnStatusCreated() {
		try {
			when(this.carService.createCar(Mockito.any(CarDto.class))).thenReturn(car);
			Response responseTest = this.carResource.createCar(car.getDto());
			assertEquals(carDto.getId(), ((CarDto) responseTest.getEntity()).getId());
			assertEquals(Status.CREATED.getStatusCode(), responseTest.getStatus());
		} catch (EntityValidationException | InvalidUUIDFormatException | EntityNotFoundException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAllOfTheCars_ShouldReturnStatusOk() {
		List<Car> cars = new ArrayList<Car>();
		when(this.carService.getCarsPaginated(any(Integer.class), any(Integer.class), any(String.class),
				any(String.class))).thenReturn(cars);
		when(this.carService.getFilteredCarsCount(any(String.class))).thenReturn(3L);
		Response responseTest = this.carResource.getAllCars(1, 1, "brand", "");
		assertEquals(Status.OK.getStatusCode(), responseTest.getStatus());
	}

	@Test
	public void whenGettingACarWithAValidID_ShouldReturnStatusOk() {
		try {
			when(this.carService.getCar(any(String.class))).thenReturn(car);
			Response response = this.carResource.getCarById(car.getDto().getId().toString());
			assertEquals(carDto.getId(), ((CarDto) (response.getEntity())).getId());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			this.brand.setName("Mercedes");
			car.setBrand(brand);
			when(this.carService.updateCar(any(CarDto.class), any(String.class))).thenReturn(car);
			Response response = this.carResource.updateCar(carId.toString(), car.getDto());
			assertEquals(car.getBrand().getName(), ((CarDto) response.getEntity()).getBrand().getName());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAValidID_ShouldReturnStatusNoContent() {
		try {
			doNothing().when(this.carService).deleteCar(carId.toString());
			Response response = this.carResource.deleteCar(carId.toString());
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
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
			carErrors.setBrand(this.brand);
			carErrors.setCountry(null);
			carErrors.setCreated_at(new Timestamp(new Date().getTime()));
			carErrors.setRegistration(new Timestamp(new Date().getTime()));
			carErrors.setUpdated_at(new Timestamp(new Date().getTime()));
			doThrow(EntityValidationException.class).when(this.carService).createCar(any(CarDto.class));
			response = this.carResource.createCar(carErrors.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The country is required\"]}", e.getMessage());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAnUnexistingCar_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			String id = "e72fd0a4-f7a5-42d4-908e-7bc1dc62f000";
			when(this.carService.getCar(any(String.class))).thenThrow(EntityNotFoundException.class);
			Response response = this.carResource.getCarById(id);
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenUpdatingACarWithAnInvalidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			Response response = null;
			when(this.carService.updateCar(any(CarDto.class), any(String.class)))
					.thenThrow(EntityNotFoundException.class);
			response = this.carResource.updateCar(id.toString(), car.getDto());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidIDAndEntityValidationErrors_ShouldReturnBadRequest() {
		try {
			Response response = null;
			car.setBrand(null);
			doThrow(EntityValidationException.class).when(this.carService).updateCar(any(CarDto.class),
					any(String.class));
			response = this.carResource.updateCar(carId.toString(), car.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The brand is required\"]}", e.getMessage());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAnInValidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			String id = "e72fd0a4-f7a5-42d4-908e-7bc1dc62f000";
			doThrow(EntityNotFoundException.class).when(this.carService).deleteCar(id);
			Response response = this.carResource.deleteCar(id.toString());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

}
