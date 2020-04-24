package com.ramonmr95.app.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull(message = "The brand is required")
	private Brand brand;

	@NotNull(message = "The model is required")
	@Column(nullable = false)
	private String model;

	@NotNull(message = "The color is required")
	@Column(nullable = false)
	private String color;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull(message = "The registration date is required")
	private Date registration;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull(message = "The country is required")
	private Country country;

	@NotNull(message = "The car components are required")
	@ElementCollection
	private Set<String> carComponents = new HashSet<String>();

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_at;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updated_at;

	public Car() {

	}

	public Car(Brand brand, String model, String color, Country country, Date registration) {
		this.brand = brand;
		this.model = model;
		this.color = color;
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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
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

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Set<String> getCarComponents() {
		return carComponents;
	}

	public void setCarComponents(Set<String> carComponents) {
		this.carComponents = carComponents;
	}
	
	public void addComponent(String component) {
		this.carComponents.add(component);
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
