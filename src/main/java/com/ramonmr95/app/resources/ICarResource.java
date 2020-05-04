package com.ramonmr95.app.resources;

import java.util.UUID;

import javax.ws.rs.core.Response;

import com.ramonmr95.app.dtos.CarDto;
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
	 * Gets a list with all of the {@link CarDto} of the database by using
	 * {@link CarService} getCars method with pagination.
	 * 
	 * @param page     page number of the pagination. (Default = 1)
	 * @param size     number of cars. (Default = 5)
	 * @param filterBy {@link CarDto} Field to be filtered by.
	 * @param orderBy  {@link CarDto} Field to be ordered by.
	 * @return response Response that contains the {@link List&lt;CarDto&gt;} with
	 *         all of the cars.
	 */
	@Operation(summary = "Get all cars", responses = {
			@ApiResponse(responseCode = "200", description = "Gets all the cars", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarDto.class))) })
	public Response getAllCars(@Parameter(description = "page number") int page,
			@Parameter(description = "number of cars") int size,
			@Parameter(description = "Used to filter the cars by name and registration") String filterBy,
			@Parameter(description = "Used to order the cars by name and registration") String orderBy);

	/**
	 * 
	 * Gets a {@link CarDto} given its id.
	 * 
	 * @param id Id of the car as string fullfilling the {@link UUID} format.
	 * @return response Response that contains either the car if it exists in the
	 *         Database or status code 404 if it does not.
	 */
	@Operation(summary = "Get car by id", responses = {
			@ApiResponse(responseCode = "200", description = "Gets the car related to an specific id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarDto.class))),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response getCarById(@Parameter(description = "Car id", required = true) String id);

	/**
	 * 
	 * Creates a {@link CarDto} received from the request body.
	 * 
	 * @param car {@link CarDto} to create.
	 * @return response Response that contains either the created {@link CarDto} and
	 *         status code 201 if there are not any validation errors or the
	 *         validations errors and status code 400 if there are any validation
	 *         errors.
	 */
	@Operation(summary = "Create a car", responses = {
			@ApiResponse(responseCode = "201", description = "Car created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid car"), })
	public Response createCar(
			@RequestBody(description = "Car to create", required = true, content = @Content(schema = @Schema(implementation = CarDto.class))) CarDto carDto);

	/**
	 * 
	 * Updates a {@link CarDto} given its id and the {@link CarDto} from the request
	 * body.
	 * 
	 * @param id  Id of the car as string fullfilling the {@link UUID} format.
	 * @param car {@link CarDto} to update.
	 * @return response Response that if there are not any {@link Car} validation
	 *         errors contains the updated {@link Car} and the status code 200. If
	 *         the {@link Car} contains validation errors the response will contain
	 *         the status code 400. If the given id does not match any {@link Car}
	 *         from the Database, the response will contain the status code 404.
	 */
	@Operation(summary = "Update a car", responses = {
			@ApiResponse(responseCode = "200", description = "Car updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid car"),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response updateCar(@Parameter(description = "Car id", required = true) String id,
			@RequestBody(description = "Updated Car", required = true, content = @Content(schema = @Schema(implementation = CarDto.class))) CarDto carDto);

	/**
	 * 
	 * Deletes a {@link Car} from the database given its id.
	 * 
	 * @param id Id of the car as string fullfilling the {@link UUID} format.
	 * @return response Response that if the given id matches any {@link Car} of the
	 *         database will return a status code of 204. If the id does not match
	 *         any id the status code will be 404.
	 */
	@Operation(summary = "Delete a car", responses = {
			@ApiResponse(responseCode = "204", description = "The car was deleted"),
			@ApiResponse(responseCode = "404", description = "There is not any car with the given id") })
	public Response deleteCar(@Parameter(description = "Car id", required = true) String id);

}
