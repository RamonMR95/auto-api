package com.ramonmr95.app.resources;

import java.util.UUID;

import javax.ws.rs.core.Response;

import com.ramonmr95.app.entities.Car;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ICarResource {

	/**
	 * 
	 * Gets a list with all of the {@link Car} entities of the database by using
	 * {@link CarService} getCars method.
	 * 
	 * @return response Response that contains the list with all of the cars.
	 */
	@Operation(summary = "Get all cars", responses = {
			@ApiResponse(responseCode = "200", description = "Gets all the cars", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))) })
	public Response getAllCars();

	/**
	 * 
	 * Gets a {@link Car} given its id.
	 * 
	 * @param id Id of the car.
	 * @return response Response that contains either the car if it exists in the
	 *         Database or status code 404 if it does not.
	 */
	@Operation(summary = "Get car by id", responses = {
			@ApiResponse(responseCode = "200", description = "Gets the car related to an specific id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response getCarById(@Parameter(description = "Car id", required = true) UUID id);

	/**
	 * 
	 * Creates a {@link Car} received from the request body.
	 * 
	 * @param car {@link Car} to create.
	 * @return response Response that contains either the created {@link Car} and
	 *         status code 201 if there are not any validation errors or the
	 *         validations errors and status code 400 if there are any validation
	 *         errors.
	 */
	@Operation(summary = "Create a car", responses = {
			@ApiResponse(responseCode = "201", description = "Car created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
			@ApiResponse(responseCode = "400", description = "Invalid car"), })
	public Response createCar(
			@RequestBody(description = "Car to create", required = true, content = @Content(schema = @Schema(implementation = Car.class))) Car car);

	/**
	 * 
	 * Updates a {@link Car} given its id and the {@link Car} from the request body.
	 * 
	 * @param id  Id of the car.
	 * @param car {@link Car} to update.
	 * @return response Response that if there are not any {@link Car} validation
	 *         errors contains the updated {@link Car} and the status code 200. If
	 *         the {@link Car} contains validation errors the response will contain the
	 *         status code 400. If the given id does not match any {@link Car} from
	 *         the Database, the response will contain the status code 404.
	 */
	@Operation(summary = "Update a car", responses = {
			@ApiResponse(responseCode = "200", description = "Car updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
			@ApiResponse(responseCode = "400", description = "Invalid car"),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response updateCar(@Parameter(description = "Car id", required = true) UUID id,
			@RequestBody(description = "Updated Car", required = true, content = @Content(schema = @Schema(implementation = Car.class))) Car car);

	/**
	 * 
	 * Deletes a {@link Car} from the database given its id.
	 * 
	 * @param id Id of the car.
	 * @return response Response that if the given id matches any {@link Car} of the
	 *         database will return a status code of 204. If the id does not match
	 *         any id the status code will be 404.
	 */
	@Operation(summary = "Delete a car", responses = {
			@ApiResponse(responseCode = "204", description = "The car was deleted"),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response deleteCar(@Parameter(description = "Car id", required = true) UUID id);

}
