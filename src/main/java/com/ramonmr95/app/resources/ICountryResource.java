package com.ramonmr95.app.resources;

import java.util.UUID;

import javax.ws.rs.core.Response;

import com.ramonmr95.app.dtos.CountryDto;
import com.ramonmr95.app.entities.Country;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ICountryResource {

	/**
	 * 
	 * Gets a list with all of the {@link Country} entities.
	 * 
	 * @return response Response that contains the {@link List&lt;Country&gt;} with
	 *         all of the countries.
	 */
	@Operation(summary = "Get all countries", responses = {
			@ApiResponse(responseCode = "200", description = "Gets all the countries", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDto.class))) })
	public Response getAllCountries();

	/**
	 * 
	 * Gets a {@link CountryDto} given its id.
	 * 
	 * @param id Id of the country as string fullfilling the {@link UUID} format.
	 * @return response Response that contains either the country if it exists in
	 *         the Database or status code 404 if it does not.
	 */
	@Operation(summary = "Get country by id", responses = {
			@ApiResponse(responseCode = "200", description = "Gets the country related to an specific id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDto.class))),
			@ApiResponse(responseCode = "404", description = "There is not any country with the given id") })
	public Response getCountryById(@Parameter(description = "Country id", required = true) String id);

	/**
	 * 
	 * Creates a {@link CountryDto} received from the request body.
	 * 
	 * @param country {@link CountryDto} to create.
	 * @return response Response that contains either the created {@link CountryDto}
	 *         and status code 201 if there are not any validation errors or the
	 *         validations errors and status code 400 if there are any validation
	 *         errors.
	 */
	@Operation(summary = "Create a country", responses = {
			@ApiResponse(responseCode = "201", description = "Country created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid country"), })
	public Response createCountry(
			@RequestBody(description = "Country to create", required = true, content = @Content(schema = @Schema(implementation = CountryDto.class))) CountryDto countryDto);

	/**
	 * 
	 * Updates a {@link CountryDto} given its id and the {@link CountryDto} from the
	 * request body.
	 * 
	 * @param id      Id of the country as string fullfilling the {@link UUID}
	 *                format.
	 * @param country {@link CountryDto} to update.
	 * @return response Response that if there are not any {@link Country}
	 *         validation errors contains the updated {@link Country} and the status
	 *         code 200. If the {@link Country} contains validation errors the
	 *         response will contain the status code 400. If the given id does not
	 *         match any {@link Country} from the Database, the response will
	 *         contain the status code 404.
	 */
	@Operation(summary = "Update a country", responses = {
			@ApiResponse(responseCode = "200", description = "Country updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid country"),
			@ApiResponse(responseCode = "404", description = "There is not any country with the given id") })
	public Response updateCountry(@Parameter(description = "Country id", required = true) String id,
			@RequestBody(description = "Updated Country", required = true, content = @Content(schema = @Schema(implementation = CountryDto.class))) CountryDto countryDto);

	/**
	 * 
	 * Deletes a {@link Country} from the database given its id.
	 * 
	 * @param id Id of the country as string fullfilling the {@link UUID} format.
	 * @return response Response that if the given id matches any {@link Country} of
	 *         the database will return a status code of 204. If the id does not
	 *         match any id the status code will be 404.
	 */
	@Operation(summary = "Delete a country", responses = {
			@ApiResponse(responseCode = "204", description = "The country was deleted"),
			@ApiResponse(responseCode = "404", description = "There is not any country with the given id") })
	public Response deleteCountry(@Parameter(description = "Country id", required = true) String id);
}
