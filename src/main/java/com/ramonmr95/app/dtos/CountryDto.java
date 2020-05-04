package com.ramonmr95.app.dtos;

import java.io.Serializable;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.ramonmr95.app.entities.Country;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * Data Transfer Object used to carry country data between processes.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Schema(name = "Country")
public class CountryDto implements Serializable {

	private static final long serialVersionUID = -4530821148279387656L;

	private UUID id;
	private String name;
	private String isoCode;
	private String flagUrl;

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

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getFlagUrl() {
		return flagUrl;
	}

	public void setFlagUrl(String flagUrl) {
		this.flagUrl = flagUrl;
	}

	public Country convertToEntity() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, Country.class);
	}

}
