package com.ramonmr95.app.resources;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.validation.Valid;
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

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarResource {

	@EJB
	private CarService carService;

	@GET
	public List<Car> getAllCars() {
		return this.carService.getCars();
	}

	@GET
	@Path("/{id}")
	public Response getCarById(@PathParam("id") UUID id) {
		try {
			Car car = this.carService.getCar(id);
			
			return Response.status(Status.OK)
					.entity(car)
					.build();
		} 
		catch (CarNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.build();
		} 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCar(@Valid Car car) {
		this.carService.createCar(car);
		
		return Response.status(Status.CREATED)
				.entity(car)
				.build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCar(@PathParam("id") UUID id, @Valid Car car) {
		try {
			Car newCar = this.carService.getCar(id);
			newCar.setBrand(car.getBrand());
			newCar.setCountry(car.getCountry());
			newCar.setRegistration(car.getRegistration());
			
			this.carService.updateCar(newCar);
			
			return Response.status(Status.OK)
					.entity(newCar)
					.build();
		}
		catch (CarNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.build();
		} 
	}

	@DELETE
	@Path("/{id}")
	public Response deleteCar(@PathParam("id") UUID id) {
		try {
			this.carService.deleteCar(id);
			
			return Response.status(Status.NO_CONTENT)
					.build();
		}
		catch (CarNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.build();
		} 
	}

}
