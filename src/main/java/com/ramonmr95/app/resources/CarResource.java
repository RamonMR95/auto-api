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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.services.CarService;
import com.ramonmr95.app.utils.CarNotFoundException;



@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource implements ICarResource {
	
	private static final Logger log = LogManager.getLogger(CarResource.class);

	@EJB
	private CarService carService;

	@GET
	@Override
	public List<Car> getAllCars() {
		log.info("Entering getAllCars!");
		List<Car> cars = this.carService.getCars();
		log.info("Exiting getAllCars!");
		return cars;
	}

	@GET
	@Path("/{id}")
	@Override
	public Response getCarById(@PathParam("id") UUID id) {
		log.info("Entering getCarById!");
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
			log.error("Error: Car not found!");
		}
		log.info("Exiting getCarById!");
		return response;
	}
	
	@POST
	@Override
	public Response createCar(@Valid Car car) {
		log.info("Entering createCar!");
		this.carService.createCar(car);
		
		log.info("Exiting createCar!");
		return Response.status(Status.CREATED)
				.entity(car)
				.build();
	}

	@PUT
	@Path("/{id}")
	@Override
	public Response updateCar(@PathParam("id") UUID id, @Valid Car car) {
		log.info("Entering updateCar!");
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
		catch (CarNotFoundException e) {
			response =  Response.status(Status.NOT_FOUND)
					.build();
			log.error("Error: Car not found!");
		} 
		log.info("Exiting updateCar!");
		return response;
	}

	@DELETE
	@Path("/{id}")
	@Override
	public Response deleteCar(@PathParam("id") UUID id) {
		log.info("Entering deleteCar!");
		Response response = null;
		
		try {
			this.carService.deleteCar(id);
			
			response =  Response.status(Status.NO_CONTENT)
					.build();
		}
		catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND)
					.build();
			log.error("Error: Car not found!");
		} 
		log.info("Exiting deleteCar!");
		return response;
	}

}