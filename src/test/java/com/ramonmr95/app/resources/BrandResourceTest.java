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

import com.ramonmr95.app.dtos.BrandDto;
import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.services.BrandService;

@RunWith(MockitoJUnitRunner.class)
public class BrandResourceTest {

	@InjectMocks
	private BrandResourceImpl brandResource;

	@Mock
	private BrandService brandService;

	private Brand brand;

	private UUID brandId = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f857");

	private BrandDto brandDto;

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
		this.brandDto = this.brand.getDto();
	}

	@After
	public void tearDown() throws Exception {

	}

	// Valid values
	@Test
	public void whenCreatingABrandWithValidValues_ShouldReturnStatusCreated() {
		try {
			when(this.brandService.createBrand(Mockito.any(Brand.class))).thenReturn(brand);
			Response responseTest = this.brandResource.createBrand(brand.getDto());
			assertEquals(brandDto.getId(), ((BrandDto) responseTest.getEntity()).getId());
			assertEquals(Status.CREATED.getStatusCode(), responseTest.getStatus());
		} catch (EntityValidationException | NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAllOfTheBrands_ShouldReturnStatusOk() {
		when(this.brandService.getBrands()).thenReturn(new ArrayList<Brand>());
		Response responseTest = this.brandResource.getAllBrands();
		assertEquals(Status.OK.getStatusCode(), responseTest.getStatus());
	}

	@Test
	public void whenGettingABrandWithAValidID_ShouldReturnStatusOk() {
		try {
			when(this.brandService.getBrand(any(String.class))).thenReturn(brand);
			Response response = this.brandResource.getBrandById(brand.getDto().getId().toString());
			assertEquals(brandDto.getId(), ((BrandDto) (response.getEntity())).getId());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingABrandWithAValidID_ShouldReturnStatusNoContent() {
		try {
			String name = "Renault";
			brand.setName(name);
			when(this.brandService.updateBrand(any(Brand.class), any(String.class))).thenReturn(brand);
			Response response = this.brandResource.updateBrand(brandId.toString(), brand.getDto());
			assertEquals(brand.getName(), ((BrandDto) response.getEntity()).getName());
			assertEquals(Status.OK.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingABrandWithAValidID_ShouldReturnStatusNoContent() {
		try {
			doNothing().when(this.brandService).deleteBrand(brandId.toString());
			Response response = this.brandResource.deleteBrand(brandId.toString());
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	// Invalid values
	@Test
	public void whenCreatingABrandWithInvalidValues_ShouldReturnStatusBadRequest() {
		try {
			Response response = null;
			Brand invalidBrand = new Brand();
			invalidBrand.setId(this.brandId);
			invalidBrand.setName(null);
			invalidBrand.setCreated_at(new Timestamp(new Date().getTime()));
			invalidBrand.setUpdated_at(new Timestamp(new Date().getTime()));
			doThrow(EntityValidationException.class).when(this.brandService).createBrand(any(Brand.class));
			response = this.brandResource.createBrand(invalidBrand.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (NotUniqueKeyException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenGettingAnUnexistingBrand_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			when(this.brandService.getBrand(id.toString())).thenThrow(EntityNotFoundException.class);
			Response response = this.brandResource.getBrandById(id.toString());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

	@Test
	public void whenUpdatingABrandWithAnInvalidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			Response response = null;
			when(this.brandService.updateBrand(any(Brand.class), any(String.class)))
					.thenThrow(EntityNotFoundException.class);
			response = this.brandResource.updateBrand(id.toString(), brand.getDto());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenUpdatingABrandWithAValidIDAndEntityValidationErrors_ShouldReturnBadRequest() {
		try {
			Response response = null;
			brand.setName(null);
			doThrow(EntityValidationException.class).when(this.brandService).updateBrand(any(Brand.class),
					any(String.class));
			response = this.brandResource.updateBrand(this.brandId.toString(), brand.getDto());
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		} catch (EntityValidationException e) {
			assertEquals("{\"errors\":[\"The name is required\"]}", e.getMessage());
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}
	}

	@Test
	public void whenDeletingABrandWithAnInValidID_ShouldReturnStatusNotFound() throws EntityNotFoundException {
		try {
			UUID id = UUID.fromString("e72fd0a4-f7a5-42d4-908e-7bc1dc62f000");
			doThrow(EntityNotFoundException.class).when(this.brandService).deleteBrand(id.toString());
			Response response = this.brandResource.deleteBrand(id.toString());
			assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		} catch (InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

}
