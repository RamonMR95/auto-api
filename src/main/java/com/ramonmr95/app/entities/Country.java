package com.ramonmr95.app.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import com.ramonmr95.app.dtos.CountryDto;

@Entity
@Table(name = "countries")
public class Country implements Serializable {

	private static final long serialVersionUID = -1210268321794935401L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@NotNull(message = "The name is required")
	@Column(nullable = false, unique = true)
	private String name;

	@NotNull(message = "The isoCode is required")
	@Column(name = "iso_code", nullable = false, unique = true)
	private String isoCode;

	@NotNull(message = "The flagUrl is required")
	@Column(name = "flag_url", nullable = false)
	private String flagUrl;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at;

	public Country() {

	}

	public Country(String name, String isoCode, String flagUrl) {
		this.name = name;
		this.isoCode = isoCode;
		this.flagUrl = flagUrl;
	}

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

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public CountryDto getDto() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, CountryDto.class);
	}

}
