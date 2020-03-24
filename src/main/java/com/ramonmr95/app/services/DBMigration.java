package com.ramonmr95.app.services;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.ramonmr95.app.entities.Car;

@Startup
@Singleton
public class DBMigration {
	
	@EJB
	private CarService carService;
	
	@PostConstruct
	public void setup() {
		List<Car> cars = Arrays.asList(new Car("BMW", "Germany", new Timestamp(new Date().getTime())),
				new Car("Mercedes", "Germany", new Timestamp(new Date().getTime())), 
				new Car("Seat", "Spain", new Timestamp(new Date().getTime())));
		
		for (Car car : cars) {
			this.carService.createCar(car);
		}
	}

}
