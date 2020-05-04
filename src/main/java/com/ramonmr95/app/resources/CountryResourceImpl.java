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

import com.ramonmr95.app.dtos.CountryDto;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.services.CountryService;

@Path("/countries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Interceptors(LoggingInterceptor.class)
public class CountryResourceImpl implements ICountryResource {

	@EJB
	private CountryService countryService;

	@GET
	@Override
	public Response getAllCountries() {
		List<CountryDto> countries = this.countryService.getCountries().stream().map(country -> country.getDto())
				.collect(Collectors.toList());
		Map<String, List<CountryDto>> countryMap = new HashMap<>();
		countryMap.put("countries", countries);
		return Response.ok(countryMap).build();
	}

	@GET
	@Path("/{id}")
	@Override
	public Response getCountryById(@PathParam("id") String id) {
		Response response = null;
		try {
			CountryDto countryDto = this.countryService.getCountry(id).getDto();
			response = Response.ok(countryDto).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (InvalidUUIDFormatException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@POST
	@Override
	public Response createCountry(CountryDto countryDto) {
		Response response = null;
		try {
			CountryDto newCountryDto = this.countryService.createCountry(countryDto.convertToEntity()).getDto();
			response = Response.status(Status.CREATED).entity(newCountryDto).build();
		} catch (EntityValidationException | NotUniqueKeyException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@PUT
	@Path("/{id}")
	@Override
	public Response updateCountry(@PathParam("id") String id, CountryDto countryDto) {
		Response response = null;
		CountryDto updatedCountryDto;
		try {
			updatedCountryDto = this.countryService.updateCountry(countryDto.convertToEntity(), id).getDto();
			response = Response.status(Status.OK).entity(updatedCountryDto).build();
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
	public Response deleteCountry(@PathParam("id") String id) {
		Response response = null;
		try {
			this.countryService.deleteCountry(id);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (InvalidUUIDFormatException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

}
