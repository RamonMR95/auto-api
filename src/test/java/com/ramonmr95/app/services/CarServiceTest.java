package com.ramonmr95.app.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
import com.ramonmr95.app.validators.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

	@InjectMocks
	private CarService carService;

	@Mock
	private PersistenceService<Car, UUID> persistenceService;

	@Mock
	private BrandService brandService;

	@Mock
	private CountryService CountryService;

	@Mock
	private TypedQuery<Car> query;

	@Mock
	private EntityValidator<Car> validator;

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
		this.country.setIsoCode("ES");
		this.country.setFlagUrl("");
		this.country.setCreated_at(new Timestamp(new Date().getTime()));
		this.country.setUpdated_at(new Timestamp(new Date().getTime()));

		this.car = new Car();
		this.car.setId(this.carId);
		this.car.setBrand(brand);
		this.car.setModel("A");
		this.car.setColor("Black");
		this.car.setRegistration(new Timestamp(new Date().getTime()));
		this.car.setCountry(country);
		this.car.addComponent("Wheel");
		this.car.setCreated_at(new Timestamp(new Date().getTime()));
		this.car.setUpdated_at(new Timestamp(new Date().getTime()));
		this.carDto = this.car.getDto();
	}

	@After
	public void tearDown() throws Exception {
	}

	// Valid values
	@Test
	public void whenCreatingACarWithValidValues() throws EntityNotFoundException {
		try {
			when(this.CountryService.getCountryByName(Mockito.any(String.class))).thenReturn(this.country);
			when(this.brandService.getBrandByName(Mockito.any(String.class))).thenReturn(brand);
			when(this.validator.isEntityValid(Mockito.any(Car.class))).thenReturn(true);
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			Car createdCar = this.carService.createCar(this.carDto);
			assertEquals(carDto.getBrand().getName(), createdCar.getBrand().getName());
			assertEquals(carDto.getModel(), createdCar.getModel());
			assertEquals(carDto.getColor(), createdCar.getColor());
			assertEquals(carDto.getCountry().getName(), createdCar.getCountry().getName());
			assertEquals(carDto.getCarComponents(), createdCar.getCarComponents());
			assertEquals(carDto.getRegistration(), createdCar.getRegistration());
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenGettingAllOfTheCars_ShouldReturnTypedQueryOfCars() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		CriteriaQuery<Car> cq = (CriteriaQuery<Car>) Mockito.mock(CriteriaQuery.class);
		Root<Car> r = (Root<Car>) Mockito.mock(Root.class);
		EntityManager em = Mockito.mock(EntityManager.class);
		when(this.persistenceService.getEm()).thenReturn(em);
		when(this.persistenceService.getEm().getCriteriaBuilder()).thenReturn(cb);
		when(cb.createQuery(Car.class)).thenReturn(cq);
		when(cq.from(Car.class)).thenReturn(r);
		when(cq.select(r)).thenReturn(cq);
		when(this.persistenceService.getEm().createQuery(cq)).thenReturn((TypedQuery<Car>) this.query);
		TypedQuery<Car> expectedQuery = this.carService.getCars("", "");
		assertEquals(expectedQuery, this.query);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenGettingAllOfTheCarsWithPagination_ShouldReturnListOfCars() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		CriteriaQuery<Car> cq = (CriteriaQuery<Car>) Mockito.mock(CriteriaQuery.class);
		Root<Car> r = (Root<Car>) Mockito.mock(Root.class);
		EntityManager em = Mockito.mock(EntityManager.class);
		when(this.persistenceService.getEm()).thenReturn(em);
		when(this.persistenceService.getEm().getCriteriaBuilder()).thenReturn(cb);
		when(cb.createQuery(Car.class)).thenReturn(cq);
		when(cq.from(Car.class)).thenReturn(r);
		when(cq.select(r)).thenReturn(cq);
		when(this.persistenceService.getEm().createQuery(cq)).thenReturn((TypedQuery<Car>) this.query);
		List<Car> expectedCars = this.carService.getCarsPaginated(1, 2, "", "");
		assertNotNull(expectedCars);
	}

	@Test
	public void whenGettingACarWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			Car carTest = this.carService.getCar(carId.toString());
			assertEquals(car, carTest);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingACarWithAValidID() {
		try {
			when(this.CountryService.getCountryByName(Mockito.any(String.class))).thenReturn(this.country);
			when(this.brandService.getBrandByName(Mockito.any(String.class))).thenReturn(brand);
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(this.car);
			when(this.validator.isEntityValid(Mockito.any(Car.class))).thenReturn(true);
			String brandName = "Renault";
			this.brand.setName(brandName);
			doNothing().when(this.persistenceService).mergeEntity(this.car);
			Car updatedCar = this.carService.updateCar(car.getDto(), carId.toString());
			assertEquals(updatedCar.getBrand().getName(), brandName);
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingACarWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(car);
			doNothing().when(persistenceService).deleteEntity(car);
			this.carService.deleteCar(carId.toString());
			Mockito.verify(this.persistenceService, Mockito.times(1)).deleteEntity(car);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test(expected = EntityValidationException.class)
	public void whenCreatingACarWithInvalidValues_ShouldThrowEntityValidationException()
			throws EntityValidationException {
		try {
			this.car.setColor(null);
			when(this.CountryService.getCountryByName(Mockito.any(String.class))).thenReturn(this.country);
			when(this.brandService.getBrandByName(Mockito.any(String.class))).thenReturn(brand);
			when(this.validator.isEntityValid(Mockito.any(Car.class))).thenReturn(false);
			this.carService.createCar(this.carDto);
			fail("Should not get here");
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenGettingAnUnexistingCar_ShouldThrowEntityNotFoundException() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			this.carService.getCar(id.toString());
		} catch (InvalidUUIDFormatException e) {
		}
		fail("Should not get here");
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUpdatingACarWithAnInvalidID_ShouldThrowEntityNotFoundException() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			when(this.carService.getCar(id.toString())).thenReturn(car);
			doThrow(EntityNotFoundException.class).when(this.carService).updateCar(car.getDto(), id.toString());
			this.brand.setName("X");
			car.setBrand(brand);
			this.carService.updateCar(car.getDto(), id.toString());
			fail("Should not get here");
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test(expected = EntityValidationException.class)
	public void whenUpdatingACarWithAValidIDAndEntityValidationErrors_ShouldThrowEntityValidationException()
			throws EntityValidationException {
		try {
			this.car.setColor(null);
			when(this.CountryService.getCountryByName(Mockito.any(String.class))).thenReturn(this.country);
			when(this.brandService.getBrandByName(Mockito.any(String.class))).thenReturn(brand);
			when(this.persistenceService.getEntityByID(Car.class, carId)).thenReturn(this.car);
			when(this.validator.isEntityValid(Mockito.any(Car.class))).thenReturn(false);
			this.carService.updateCar(this.car.getDto(), carId.toString());
			fail("Should not get here");
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenDeletingACarWithAnInValidID_ShouldThrowCarNotFoundException() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Car.class, id)).thenReturn(null);
			this.carService.deleteCar(id.toString());
		} catch (InvalidUUIDFormatException e) {
		}
		fail("Should not get here");
	}

}
