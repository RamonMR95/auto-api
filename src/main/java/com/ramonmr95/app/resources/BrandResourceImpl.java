package com.ramonmr95.app.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ramonmr95.app.dtos.BrandDto;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.services.BrandService;

@Path("/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Interceptors(LoggingInterceptor.class)
public class BrandResourceImpl implements IBrandResource {

	@EJB
	private BrandService brandService;

	@GET
	@Override
	public Response getAllBrands() {
		List<BrandDto> brands = this.brandService.getBrands().stream().map(brand -> brand.getDto())
				.collect(Collectors.toList());
		Map<String, List<BrandDto>> brandsMap = new HashMap<>();
		brandsMap.put("brands", brands);
		return Response.ok(brandsMap).build();
	}

	@GET
	@Path("/{id}")
	@Override
	public Response getBrandById(@PathParam("id") String id) {
		Response response = null;
		try {
			BrandDto brandDto = this.brandService.getBrand(id).getDto();
			response = Response.ok(brandDto).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (InvalidUUIDFormatException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@POST
	@Override
	public Response createBrand(BrandDto brandDto) {
		Response response = null;
		try {
			BrandDto newBrandDto = this.brandService.createBrand(brandDto.convertToEntity()).getDto();
			response = Response.status(Status.CREATED).entity(newBrandDto).build();
		} catch (EntityValidationException | NotUniqueKeyException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@PUT
	@Path("/{id}")
	@Override
	public Response updateBrand(@PathParam("id") String id, BrandDto brandDto) {
		Response response = null;
		BrandDto updatedBrandDto;
		try {
			updatedBrandDto = this.brandService.updateBrand(brandDto.convertToEntity(), id).getDto();
			response = Response.status(Status.OK).entity(updatedBrandDto).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (EntityValidationException | InvalidUUIDFormatException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@DELETE
	@Path("/{id}")
	@Override
	public Response deleteBrand(@PathParam("id") String id) {
		Response response = null;
		try {
			this.brandService.deleteBrand(id);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (InvalidUUIDFormatException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

}
