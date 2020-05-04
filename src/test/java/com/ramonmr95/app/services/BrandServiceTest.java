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

import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;

@RunWith(MockitoJUnitRunner.class)
public class BrandServiceTest {

	@InjectMocks
	private BrandService brandService;

	@Mock
	private PersistenceService<Brand, UUID> persistenceService;

	@Mock
	private TypedQuery<Brand> query;

	private Brand brand;

	private UUID brandId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.brand = new Brand();
		brand.setId(this.brandId);
		brand.setName("Renault");
		brand.setCreated_at(new Timestamp(new Date().getTime()));
		brand.setUpdated_at(new Timestamp(new Date().getTime()));
	}

	@After
	public void tearDown() throws Exception {
	}

	// Valid values
	@Test
	public void whenCreatingABrandWithValidValues() throws EntityValidationException {
		try {
			doNothing().when(this.persistenceService).persistEntity(brand);
			this.brandService.createBrand(brand);
			Mockito.verify(persistenceService, Mockito.times(1)).persistEntity(brand);
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenGettingAllOfTheBrands_ShouldReturnListOfCars() {
		when(this.persistenceService.getEntities(Mockito.eq(Brand.class))).thenReturn(new ArrayList<Brand>());
		List<Brand> expectedBrands = new ArrayList<Brand>();
		List<Brand> brands = this.brandService.getBrands();
		assertEquals(expectedBrands, brands);
	}

	@Test
	public void whenGettingABrandWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Brand.class, brandId)).thenReturn(brand);
			Brand brandTest = this.brandService.getBrand(brandId.toString());
			assertEquals(brand, brandTest);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingABrandWithAValidID() {
		try {
			String name = "Renault";
			brand.setName(name);
			when(this.persistenceService.getEntityByID(Brand.class, brandId)).thenReturn(brand);
			doNothing().when(this.persistenceService).mergeEntity(brand);
			this.brandService.updateBrand(brand, brandId.toString());
			assertEquals(brand.getName(), name);
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingABrandWithAValidID() {
		try {
			when(this.persistenceService.getEntityByID(Brand.class, brandId)).thenReturn(brand);
			doNothing().when(persistenceService).deleteEntity(brand);
			this.brandService.deleteBrand(brandId.toString());
			Mockito.verify(this.persistenceService, Mockito.times(1)).deleteEntity(brand);
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingABrandWithInvalidValues_ShouldThrowEntityValidationException() {
		try {
			Brand brandErrors = new Brand();
			brandErrors.setId(this.brandId);
			brandErrors.setName(null);
			brandErrors.setCreated_at(new Timestamp(new Date().getTime()));
			brandErrors.setUpdated_at(new Timestamp(new Date().getTime()));
			this.brandService.createBrand(brandErrors);
			fail("Should not get here");
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenGettingAnUnexistingBrand_ShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		when(this.persistenceService.getEntityByID(Brand.class, id)).thenReturn(null);
		this.brandService.getBrand(id.toString());
		fail("Should not get here");
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUpdatingABrandWithAnInvalidID_ShouldThrowEntityNotFoundException() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.persistenceService.getEntityByID(Brand.class, id)).thenReturn(null);
			when(this.brandService.getBrand(id.toString())).thenReturn(brand);
			doThrow(EntityNotFoundException.class).when(this.brandService).updateBrand(brand, id.toString());
			brand.setName("X");
			this.brandService.updateBrand(brand, id.toString());
			fail("Should not get here");
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingABrandWithAValidIDAndEntityValidationErrors_ShouldThrowEntityValidationException() {
		try {
			when(this.persistenceService.getEntityByID(Brand.class, brandId)).thenReturn(brand);
			brand.setName(null);
			this.brandService.updateBrand(brand, brandId.toString());
			fail("Should not get here");
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenDeletingABrandWithAnInValidID_ShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, InvalidUUIDFormatException {
		UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
		when(this.persistenceService.getEntityByID(Brand.class, id)).thenReturn(null);
		this.brandService.deleteBrand(id.toString());
		fail("Should not get here");
	}

}
