package com.ramonmr95.app.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.services.CarService;

/**
 * 
 * Resource that maps our {@link Car} API endpoints to a method.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Interceptors(LoggingInterceptor.class)
public class CarResourceImpl implements ICarResource {

	@EJB
	private CarService carService;

	@GET
	@Override
	public Response getAllCars(@DefaultValue("1") @QueryParam(value = "page") int page,
			@DefaultValue("5") @QueryParam(value = "size") int size,
			@DefaultValue("") @QueryParam(value = "filterBy") String filterBy,
			@QueryParam(value = "orderBy") String orderBy) {
		Response response = null;
		if (page < 1 || size < 0) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		List<CarDto> list = this.carService.getCars(page, size, filterBy, orderBy).stream().map(car -> car.getDto())
				.collect(Collectors.toList());

		int perPage = list.size();
		int totalCount = this.carService.getCarsCount(filterBy);
		int pageCount = 1;
		if (perPage < totalCount && perPage > 0) {
			pageCount = (totalCount / perPage) + 1;
		}
		if (page > pageCount) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		LinkedHashMap<String, Object> carsMap = new LinkedHashMap<String, Object>();
		carsMap.put("page", page);
		carsMap.put("per_page", perPage);
		carsMap.put("page_count", pageCount);
		carsMap.put("total_count", totalCount);
		carsMap.put("cars", list);
		response = Response.ok(carsMap).build();
		return response;
	}

	@GET
	@Path("/{id}")
	@Override
	public Response getCarById(@PathParam("id") UUID id) {
		Response response = null;
		try {
			CarDto carDto = this.carService.getCar(id).getDto();
			response = Response.status(Status.OK).entity(carDto).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}

	@POST
	@Override
	public Response createCar(CarDto carDto) {
		Response response = null;
		try {
			CarDto createdDto = this.carService.createCar(carDto.convertToEntity()).getDto();
			response = Response.status(Status.CREATED).entity(createdDto).build();
		} catch (EntityValidationException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return response;
	}

	@PUT
	@Path("/{id}")
	@Override
	public Response updateCar(@PathParam("id") UUID id, CarDto carDto) {
		Response response = null;
		try {
			CarDto updatedcarDto = this.carService.updateCar(carDto.convertToEntity(), id).getDto();
			response = Response.status(Status.OK).entity(updatedcarDto).build();
		} catch (EntityValidationException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}

	@DELETE
	@Path("/{id}")
	@Override
	public Response deleteCar(@PathParam("id") UUID id) {
		Response response = null;
		try {
			this.carService.deleteCar(id);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}

}