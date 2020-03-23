package com.ramonmr95.app.services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.ramonmr95.app.entities.Car;

@Startup
@Singleton
public class DBMigration {
	
	@Inject
	private CarService carService;
	
	@PostConstruct
	public void setup() {
		List<Car> cars = Arrays.asList(new Car("BMW", "Germany"),
				new Car("Mercedes", "Germany"), 
				new Car("Seat", "Spain"));
		
		for (Car car : cars) {
			this.carService.createCar(car);
		}
	}

}
