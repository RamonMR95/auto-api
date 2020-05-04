package com.ramonmr95.app.resources;

import java.util.UUID;

import javax.ws.rs.core.Response;

import com.ramonmr95.app.dtos.BrandDto;
import com.ramonmr95.app.entities.Brand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface IBrandResource {

	/**
	 * 
	 * Gets a list with all of the {@link Brand} entities.
	 * 
	 * @return response Response that contains the {@link List&lt;Brand&gt;} with
	 *         all of the brands.
	 */
	@Operation(summary = "Get all brands", responses = {
			@ApiResponse(responseCode = "200", description = "Gets all the brands", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDto.class))) })
	public Response getAllBrands();

	/**
	 * 
	 * Gets a {@link BrandDto} given its id.
	 * 
	 * @param id Id of the brand as string fullfilling the {@link UUID} format.
	 * @return response Response that contains either the brand if it exists in the
	 *         Database or status code 404 if it does not.
	 */
	@Operation(summary = "Get brand by id", responses = {
			@ApiResponse(responseCode = "200", description = "Gets the brand related to an specific id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDto.class))),
			@ApiResponse(responseCode = "404", description = "There is not any brand with the given id") })
	public Response getBrandById(@Parameter(description = "{@link Brand} id", required = true) String id);

	/**
	 * 
	 * Creates a {@link BrandDto} received from the request body.
	 * 
	 * @param brand {@link BrandDto} to create.
	 * @return response Response that contains either the created {@link BrandDto}
	 *         and status code 201 if there are not any validation errors or the
	 *         validations errors and status code 400 if there are any validation
	 *         errors.
	 */
	@Operation(summary = "Create a brand", responses = {
			@ApiResponse(responseCode = "201", description = "Brand created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid brand"), })
	public Response createBrand(
			@RequestBody(description = "Brand to create", required = true, content = @Content(schema = @Schema(implementation = BrandDto.class))) BrandDto brandDto);

	/**
	 * 
	 * Updates a {@link BrandDto} given its id and the {@link BrandDto} from the
	 * request body.
	 * 
	 * @param id      Id of the brand as string fullfilling the {@link UUID} format.
	 * @param brand {@link BrandDto} to update.
	 * @return response Response that if there are not any {@link Brand} validation
	 *         errors contains the updated {@link Brand} and the status code 200. If
	 *         the {@link Brand} contains validation errors the response will
	 *         contain the status code 400. If the given id does not match any
	 *         {@link Brand} from the Database, the response will contain the status
	 *         code 404.
	 */
	@Operation(summary = "Update a brand", responses = {
			@ApiResponse(responseCode = "200", description = "Brand updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid brand"),
			@ApiResponse(responseCode = "404", description = "There is not any brand with the given id") })
	public Response updateBrand(@Parameter(description = "Brand id", required = true) String id,
			@RequestBody(description = "Updated Brand", required = true, content = @Content(schema = @Schema(implementation = BrandDto.class))) BrandDto brandDto);

	public Response deleteBrand(String id);
}
