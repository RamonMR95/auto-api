package com.ramonmr95.app.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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

import com.ramonmr95.app.dtos.CarDto;

/**
 * 
 * Pojo Class that represents the Car entity.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Entity
@Table(name = "cars")
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@NotNull(message = "The brand is required")
	@Column(nullable = false)
	private String brand;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull(message = "The registration date is required")
	private Date registration;

	@NotNull(message = "The country is required")
	@Column(nullable = false)
	private String country;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_at;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updated_at;

	public Car() {

	}

	public Car(String brand, String country, Date registration) {
		this.brand = brand;
		this.country = country;
		this.registration = registration;
		this.created_at = new Timestamp(new Date().getTime());
		this.updated_at = new Timestamp(new Date().getTime());
	}

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

	public CarDto getDto() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, CarDto.class);
	}

}
