package com.ramonmr95.app.dtos;

import java.io.Serializable;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.ramonmr95.app.entities.Brand;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * Data Transfer Object used to carry brand data between processes.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Schema(name = "Brand")
public class BrandDto implements Serializable {

	private static final long serialVersionUID = -2183188406925117095L;

	private UUID id;
	private String name;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Brand convertToEntity() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, Brand.class);
	}
}
