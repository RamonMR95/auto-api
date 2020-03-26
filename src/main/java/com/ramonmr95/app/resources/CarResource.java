package com.ramonmr95.app.resources;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.services.CarService;
import com.ramonmr95.app.utils.CarNotFoundException;
import com.ramonmr95.app.utils.EntityValidationException;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class CarResource implements ICarResource {

	@EJB
	private CarService carService;

	@GET
	@Override
	public List<Car> getAllCars() {
		List<Car> cars = this.carService.getCars();
		return cars;
	}

	@GET
	@Path("/{id}")
	@Override
	public Response getCarById(@PathParam("id") UUID id) {
		Response response = null;
		try {
			Car car = this.carService.getCar(id);
			response = Response.status(Status.OK)
					.entity(car)
					.build();
		} 
		catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND)
					.build();
		}
		return response;
	}

	@POST
	@Override
	public Response createCar(Car car) {
		Response response = null;
		
		try {
			this.carService.createCar(car);
			response = Response.status(Status.CREATED)
					.entity(car)
					.build();
		} 
		catch (EntityValidationException e) {
			response = Response.status(Status.BAD_REQUEST)
					.entity(this.carService.getCarValidationErrors(car))
					.build();
		}
		return response;
	}

	@PUT
	@Path("/{id}")
	@Override
	public Response updateCar(@PathParam("id") UUID id, Car car) {
		Response response = null;
		
		try {
			Car newCar = this.carService.getCar(id);
			newCar.setBrand(car.getBrand());
			newCar.setCountry(car.getCountry());
			newCar.setRegistration(car.getRegistration());

			this.carService.updateCar(newCar);

			response = Response.status(Status.OK)
					.entity(newCar)
					.build();
		} 
		catch (EntityValidationException e) {
			response = Response.status(Status.BAD_REQUEST)
					.entity(this.carService.getCarValidationErrors(car))
					.build();
		}
		catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND)
					.build();
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
			response = Response.status(Status.NO_CONTENT)
					.build();
		} 
		catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND)
					.build();
		}
		return response;
	}
	
}