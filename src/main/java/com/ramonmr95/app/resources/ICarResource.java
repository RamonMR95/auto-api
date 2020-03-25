package com.ramonmr95.app.resources;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.ramonmr95.app.entities.Car;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ICarResource {
	
	@Operation(summary = "Get all cars",
			responses = {
					@ApiResponse(
							responseCode = "200", 
							description = "Gets all the cars",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Car.class)))
			}
	)
	public List<Car> getAllCars();
	
	
	@Operation(summary = "Get car by id",
			responses = {
					@ApiResponse(
							responseCode = "200", 
							description = "Gets the car related to an specific id",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Car.class))),
					@ApiResponse(responseCode = "404", description = "There is not any car with the given id")
			}
	)
	public Response getCarById(UUID id);
	
	
	@Operation(summary = "Create a car",
			responses = {
					@ApiResponse(
							responseCode = "201",
							description = "Car created",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Car.class))),
					@ApiResponse(responseCode = "400", description = "Invalid car"),
			}
	)
	public Response createCar(Car car);
	
	
	@Operation(summary = "Update a car",
			responses = {
					@ApiResponse(
						responseCode = "200",
						description = "Car updated",
						content = @Content(mediaType = "application/json",
						schema = @Schema(implementation = Car.class))),
					@ApiResponse(responseCode = "400", description = "Invalid car"),
					@ApiResponse(responseCode = "404", description = "There is not any car with the given id")
			}
	)
	public Response updateCar(UUID id, Car car);
	
	
	@Operation(summary = "Delete a car",
			responses = {
					@ApiResponse(responseCode = "204", description = "The car was deleted"),
					@ApiResponse(responseCode = "404", description = "There is not any car with the given id")
			}
	)
	public Response deleteCar(UUID id);
	
}
