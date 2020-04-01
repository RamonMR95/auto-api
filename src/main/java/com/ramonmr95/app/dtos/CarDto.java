package com.ramonmr95.app.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;

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

	private UUID id;
	private String brand;
	private Date registration;
	private String country;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Car convertToEntity() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, Car.class);
	}

}
