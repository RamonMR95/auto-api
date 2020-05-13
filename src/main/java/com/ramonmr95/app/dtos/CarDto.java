package com.ramonmr95.app.dtos;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramonmr95.app.entities.Car;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * Data Transfer Object used to carry car data between processes.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Schema(name = "Car")
public class CarDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(CarDto.class);

	private UUID id;

	private BrandDto brand;

	private String model;

	private String color;

	private Date registration;

	private CountryDto country;

	@JsonProperty("car_components")
	private Set<String> carComponents;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public BrandDto getBrand() {
		return brand;
	}

	public void setBrand(BrandDto brandDto) {
		this.brand = brandDto;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto countryDto) {
		this.country = countryDto;
	}

	public Set<String> getCarComponents() {
		return carComponents;
	}

	public void setCarComponents(Set<String> carComponents) {
		this.carComponents = carComponents;
	}

	public Car convertToEntity() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, Car.class);
	}

	public static CarDto getCarDtoFromJsonString(String jsonString) {
		CarDto carDto = null;
		try {
			carDto = new ObjectMapper().findAndRegisterModules().readValue(jsonString, CarDto.class);
		} catch (IOException e) {
			log.error("Cannot parse jsonString to CarDto");
		}
		return carDto;
	}

}
